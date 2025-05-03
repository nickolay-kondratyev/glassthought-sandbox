**Plan: Create Basic 3D VR Visualization Demo with TypeScript, Vite, and Three.js**

**Objective:**

To set up and run a minimal web application that displays a rotating 3D cube using Three.js, implemented in TypeScript. The application will include basic WebXR support via a button to enable viewing in VR headsets like the Meta Quest 3. The project will be scaffolded and run using the Vite build tool.

**Technology Stack:**

* **Language:** TypeScript
* **3D Library:** Three.js
* **Build Tool / Dev Server:** Vite
* **VR Integration:** WebXR (via Three.js helpers)
* **Package Manager:** npm (Node Package Manager)

**Prerequisites:**

* A command-line interface (shell/terminal).
* Node.js installed (LTS version recommended, includes npm). Verify with `node -v` and `npm -v`.

**Execution Steps:**

**Step 1: Initialize Project Directory and Vite Setup**

1.  **Action:** Execute the Vite scaffolding command to create a new project using the Vanilla TypeScript template. Replace `basic-vr-demo` with the desired directory name if preferred.
    ```bash
    npm create vite@latest basic-vr-demo --template vanilla-ts
    ```
    * *(Expected Output: Vite will create the directory and scaffold basic files. It might output instructions to `cd` into the directory and run `npm install`.)*
2.  **Action:** Navigate into the newly created project directory.
    ```bash
    cd basic-vr-demo
    ```
    * *(Expected Output: Command prompt path changes to the `basic-vr-demo` directory.)*

**Step 2: Install Dependencies**

1.  **Action:** Install the default dependencies listed in `package.json` (mainly TypeScript and Vite itself).
    ```bash
    npm install
    ```
    * *(Expected Output: npm downloads and installs packages, creating a `node_modules` directory and potentially a `package-lock.json` file. May show warnings about optional dependencies, which are usually safe to ignore.)*
2.  **Action:** Install the Three.js library.
    ```bash
    npm install three
    ```
    * *(Expected Output: npm downloads and installs the `three` package. The `package.json` and `package-lock.json` files will be updated.)*

**Step 3: Configure `index.html`**

1.  **Action:** Overwrite the contents of the `index.html` file in the project root directory with the following markup. This provides the basic HTML structure and links the TypeScript entry point.
    ```html
    <!doctype html>
    <html lang="en">
      <head>
        <meta charset="UTF-8" />
        <link rel="icon" type="image/svg+xml" href="/vite.svg" /> <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Basic Three.js VR Demo</title>
        <link rel="stylesheet" href="/src/style.css">
        <script type="module" src="/src/main.ts"></script>
      </head>
      <body>
        </body>
    </html>
    ```
    * *(Verification: Check the contents of `index.html` match the code above.)*

**Step 4: Configure `src/style.css`**

1.  **Action:** Overwrite the contents of the `src/style.css` file with the following CSS rules. This ensures the rendering canvas fills the browser window without margins or scrollbars.
    ```css
    /* src/style.css */
    body {
      margin: 0;
      overflow: hidden; /* Prevent scrollbars */
      background-color: #111; /* Dark background */
    }

    canvas {
      display: block; /* Prevent potential layout shifts */
    }
    ```
    * *(Verification: Check the contents of `src/style.css` match the code above.)*

**Step 5: Implement TypeScript Logic in `src/main.ts`**

1.  **Action:** Overwrite the contents of the `src/main.ts` file with the following TypeScript code. This code sets up the Three.js scene, camera, renderer, creates a rotating cube, enables WebXR, adds the VR entry button, and starts the animation loop.

    ```typescript
    // src/main.ts
    import * as THREE from 'three';
    // Import the VRButton helper utility from Three.js examples
    import { VRButton } from 'three/examples/jsm/webxr/VRButton.js';

    // --- Basic Scene Setup ---

    // 1. Scene: Container for all objects, lights, cameras.
    const scene = new THREE.Scene();

    // 2. Camera: Defines the viewpoint. PerspectiveCamera mimics human eye view.
    const camera = new THREE.PerspectiveCamera(
      75, // Field of View (degrees)
      window.innerWidth / window.innerHeight, // Aspect Ratio
      0.1, // Near clipping plane (objects closer won't be rendered)
      1000 // Far clipping plane (objects further won't be rendered)
    );
    // Position the camera slightly back from the origin
    camera.position.z = 5;

    // 3. Renderer: Draws the scene using WebGL.
    const renderer = new THREE.WebGLRenderer({
        antialias: true // Enable anti-aliasing for smoother edges
    });
    renderer.setSize(window.innerWidth, window.innerHeight); // Set size to full window
    renderer.setPixelRatio(window.devicePixelRatio); // Adjust for high-DPI screens

    // Append the renderer's canvas element to the HTML body
    document.body.appendChild(renderer.domElement);

    // --- WebXR (VR) Setup ---

    // 4. Enable WebXR capabilities in the renderer
    renderer.xr.enabled = true;

    // 5. Add the VR Button: Creates a button to enter/exit VR session
    // It automatically handles compatibility checks and session requests.
    document.body.appendChild(VRButton.createButton(renderer));

    // --- Content Creation ---

    // 6. Geometry: Defines the shape of an object (a 1x1x1 cube)
    const geometry = new THREE.BoxGeometry(1, 1, 1);

    // 7. Material: Defines the appearance (a basic green color, unaffected by lights)
    const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });

    // 8. Mesh: Combines Geometry and Material to create the final object
    const cube = new THREE.Mesh(geometry, material);

    // 9. Add the cube to the scene
    scene.add(cube);

    // --- Responsiveness ---

    // 10. Handle window resize events to update camera and renderer
    window.addEventListener('resize', () => {
        // Only resize if not currently presenting in VR
        if (!renderer.xr.isPresenting) {
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix(); // Update camera projection matrix
            renderer.setSize(window.innerWidth, window.innerHeight); // Update renderer size
        }
    });

    // --- Animation Loop ---

    // 11. Define the animation function using renderer.setAnimationLoop
    // This replaces requestAnimationFrame for WebXR compatibility.
    renderer.setAnimationLoop((timestamp, frame) => {
        // Animate the cube (simple rotation)
        // Rotation values are in radians
        cube.rotation.x += 0.01;
        cube.rotation.y += 0.01;

        // Render the scene from the perspective of the camera
        // The renderer automatically handles camera updates based on headset pose when in VR
        renderer.render(scene, camera);
    });

    console.log("Three.js application initialized successfully.");
    ```
    * *(Verification: Check the contents of `src/main.ts` match the code above.)*

**Step 6: Start Development Server**

1.  **Action:** Run the Vite development server using the npm script defined in `package.json`.
    ```bash
    npm run dev
    ```
    * *(Expected Output: Vite builds the project quickly and starts a local server. It will print the local network URL, typically like `Local: http://localhost:5173/` and possibly a network URL.)*

**Step 7: Verification Instructions**

1.  **Verification (Desktop):**
    * Open a web browser on the machine running the server.
    * Navigate to the local URL provided by Vite (e.g., `http://localhost:5173/`).
    * **Expected Result:** You should see a green cube rotating in the center of the browser window. An "ENTER VR" button should be visible (it might appear greyed out or say "VR NOT SUPPORTED" if the desktop browser doesn't support/detect WebXR).
2.  **Verification (VR Headset - e.g., Quest 3):**
    * Ensure the VR headset is on the same local network as the machine running the dev server.
    * Find the IP address of the machine running the dev server (e.g., using `ipconfig` on Windows or `ip addr` / `ifconfig` on Linux/macOS).
    * Open the browser inside the VR headset (e.g., Meta Quest Browser).
    * Navigate to the network URL of the dev server (e.g., `http://<YOUR_COMPUTER_IP>:5173/` - replace the IP and port if different).
    * **Expected Result:** You should see the same rotating green cube. The "ENTER VR" button should be active. Clicking the button should transition the view into an immersive, stereoscopic VR mode where head movements control the camera perspective.

