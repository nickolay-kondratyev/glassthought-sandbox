**Goal:** Replace automatic graph rotation with manual rotation controlled by clicking and dragging the mouse on the plot. Rotation should occur while the mouse button is held down and moved, and stop upon release.

**Assumptions:**

1.  You are using `matplotlib` and its `mpl_toolkits.mplot3d` for the 3D graph.
2.  The `plan.task-2.basic-graph` involves creating a `Figure`, an `Axes3D` subplot, and plotting some data.
3.  Any existing code for *automatic* rotation (e.g., using `matplotlib.animation.FuncAnimation` or a loop changing `ax.view_init`) needs to be disabled or removed.

**Plan:**

1.  **Prerequisites & Setup:**
    * Ensure `matplotlib` is installed.
    * Import necessary libraries: `matplotlib.pyplot as plt` and `from mpl_toolkits.mplot3d import Axes3D`.
    * Get the code for the basic graph from `plan.task-2.basic-graph`. This typically involves:
        ```python
        # Example basic setup
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        # ... plotting commands (e.g., ax.plot_surface, ax.scatter) ...
        ```
    * **Crucially:** Remove or comment out any existing code that automatically updates the view angles (`ax.view_init`) within a loop or animation function.

2.  **Implement State Variables:**
    * We need variables to keep track of the dragging state and starting positions/angles. These can be global variables or attributes of a class if you prefer a more structured approach.
    ```python
    # Global state variables (or use a class structure)
    dragging = False  # True if mouse button is currently pressed
    start_x, start_y = None, None # Mouse pixel coordinates when drag started
    start_azim, start_elev = None, None # View angles when drag started
    ```

3.  **Create Mouse Event Handler Functions:**
    * Matplotlib's event handling system allows connecting functions to specific events (like mouse clicks, releases, motion). We need three handlers:
        * **`on_press(event)`:** Triggered when a mouse button is pressed down.
            * Check if the event occurred within our 3D axes (`event.inaxes == ax`). If not, ignore.
            * Set `dragging = True`.
            * Record the starting mouse pixel coordinates: `start_x, start_y = event.x, event.y`.
            * Record the current view angles: `start_azim, start_elev = ax.azim, ax.elev`.
        * **`on_motion(event)`:** Triggered when the mouse moves.
            * Check if `dragging` is `True`. If not, ignore.
            * Check if the event occurred within our 3D axes (`event.inaxes == ax`). If not, ignore (or optionally stop dragging).
            * Get current mouse pixel coordinates: `current_x, current_y = event.x, event.y`.
            * Calculate the change in mouse position: `dx = current_x - start_x`, `dy = current_y - start_y`.
            * Calculate new view angles based on the change. You might need a sensitivity factor to control rotation speed. Note that screen Y typically increases downwards, while elevation increases upwards.
                * `new_azim = start_azim + dx * 0.3` # Adjust sensitivity (0.3) as needed
                * `new_elev = start_elev - dy * 0.3` # Note the minus sign for elevation
            * Update the axes view: `ax.view_init(elev=new_elev, azim=new_azim)`.
            * Redraw the canvas efficiently: `fig.canvas.draw_idle()`.
        * **`on_release(event)`:** Triggered when a mouse button is released.
            * Set `dragging = False`.
            * Optionally reset `start_x`, `start_y`, `start_azim`, `start_elev` to `None`.

4.  **Connect Event Handlers to the Figure Canvas:**
    * After creating the figure (`fig`) and axes (`ax`), connect the handlers using `mpl_connect`:
    ```python
    fig.canvas.mpl_connect('button_press_event', on_press)
    fig.canvas.mpl_connect('motion_notify_event', on_motion)
    fig.canvas.mpl_connect('button_release_event', on_release)
    ```

5.  **Display the Plot:**
    * Use `plt.show()` to display the interactive window.

**Example Code Structure (Conceptual):**

```python
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np # For example data

# --- State Variables ---
dragging = False
start_x, start_y = None, None
start_azim, start_elev = None, None
sensitivity = 0.3 # Adjust rotation speed

# --- Event Handlers ---
def on_press(event):
    global dragging, start_x, start_y, start_azim, start_elev
    if event.inaxes != ax:
        return
    dragging = True
    start_x, start_y = event.x, event.y
    start_azim, start_elev = ax.azim, ax.elev

def on_motion(event):
    global dragging, start_x, start_y, start_azim, start_elev, sensitivity
    if not dragging or event.inaxes != ax:
        return

    current_x, current_y = event.x, event.y
    dx = current_x - start_x
    dy = current_y - start_y

    new_azim = (start_azim + dx * sensitivity) % 360 # Keep azimuth in [0, 360]
    new_elev = start_elev - dy * sensitivity
    # Clamp elevation to avoid flipping, e.g., between -90 and 90
    new_elev = np.clip(new_elev, -90, 90)

    ax.view_init(elev=new_elev, azim=new_azim)
    fig.canvas.draw_idle() # Efficient redraw

def on_release(event):
    global dragging
    dragging = False

# --- Basic Graph Setup (from plan.task-2.basic-graph) ---
fig = plt.figure(figsize=(8, 6))
ax = fig.add_subplot(111, projection='3d')

# Example Plot Data (replace with your actual data)
x = np.arange(-5, 5, 0.25)
y = np.arange(-5, 5, 0.25)
X, Y = np.meshgrid(x, y)
R = np.sqrt(X**2 + Y**2)
Z = np.sin(R)
ax.plot_surface(X, Y, Z, cmap='viridis', edgecolor='none')
ax.set_xlabel('X axis')
ax.set_ylabel('Y axis')
ax.set_zlabel('Z axis')
ax.set_title('Click and Drag to Rotate')

# --- Connect Event Handlers ---
fig.canvas.mpl_connect('button_press_event', on_press)
fig.canvas.mpl_connect('motion_notify_event', on_motion)
fig.canvas.mpl_connect('button_release_event', on_release)

# --- Remove any auto-rotation code here ---
# e.g., comment out or delete any `FuncAnimation` calls or loops updating view_init

# --- Display Plot ---
plt.show()
```

**Potential Issues & Refinements:**

* **Sensitivity:** The `sensitivity` factor might need tuning for a comfortable rotation speed.
* **Coordinate Systems:** Ensure you use pixel coordinates (`event.x`, `event.y`) for calculating rotation delta, not data coordinates (`event.xdata`, `event.ydata`).
* **Elevation Clamping:** You might want to clamp the elevation angle (e.g., between -90 and +90 degrees) to prevent the plot from flipping upside down (`new_elev = np.clip(new_elev, -90, 90)`).
* **Azimuth Wrapping:** Use the modulo operator (`% 360`) to keep the azimuth angle within a standard range if desired.
* **State Management:** For more complex applications, encapsulating the state variables and event handlers within a class can lead to cleaner, more maintainable code.
* **Performance:** `draw_idle()` is generally preferred over `draw()` in event handlers for smoother interaction, especially during rapid motion events.
* **Backend:** Ensure you are using an interactive Matplotlib backend (like Qt5Agg, TkAgg, WXAgg, MacOSX). Some non-interactive backends (like Agg used for saving files) won't support this interaction.

This plan provides a clear path to implementing the desired user-controlled rotation feature. Remember to adapt the example code structure to your specific plotting code from `plan.task-2.basic-graph`.
