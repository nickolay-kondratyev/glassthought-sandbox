## Detailed Plan: 3D Force-Directed Graph Visualization (From Scratch)

**Objective:** Generate the code for a fully functioning 3D force-directed graph visualization. The application will be built using `Vite`, `TypeScript`, and `three.js`. It will load graph data (nodes and relationships) from a JSON file, display it using a force-directed layout, allow users to rotate the view with the mouse, be extensible for adding more elements, and include basic setup for future VR usage.

**Phase 1: Environment Setup & Project Initialization**

1.  **Task 1.1: Setup Node.js Environment**
    * Ensure NVM (Node Version Manager) is installed.
    * Install Node.js version 20: `nvm install 20`
    * Set Node.js version 20 as the version to use for this project: `nvm use 20`
    * Verify Node (`node -v`) and npm (`npm -v`) versions.
2.  **Task 1.2: Initialize Vite + TypeScript Project**
    * Choose a directory for your project.
    * Run the Vite scaffolding command:
        ```bash
        # Make sure you are using Node 20 (nvm use 20)
        npm create vite@latest my-3d-graph -- --template vanilla-ts
        ```
    * Navigate into the newly created project directory:
        ```bash
        cd my-3d-graph
        ```
3.  **Task 1.3: Install Dependencies**
    * Install core `three.js` and necessary types:
        ```bash
        npm install three@0.176.0
        npm install --save-dev @types/three
        ```
    * Install the recommended library for integrating `d3-force-3d` with `three.js`:
        ```bash
        npm install force-graph
        ```
    * Ensure development dependencies specified in the prompt context are present (Vite should handle this, but verify `typescript` and `vite` versions in `package.json` align if needed, e.g., `npm install --save-dev typescript@~5.7.2 vite@^6.3.1`). Run `npm install` again if `package.json` was modified.
4.  **Task 1.4: Basic HTML Structure (`index.html`)**
    * Modify the `index.html` file in the project root.
    * Ensure it has a `<body>` tag.
    * Add a container `div` for the visualization, e.g., `<div id="graph-container"></div>`.
    * Ensure the `main.ts` script is included (`<script type="module" src="/src/main.ts"></script>`).
    * Add basic CSS in `style.css` to make the container visible and potentially full-screen (e.g., remove margins, set width/height).
5.  **Task 1.5: Setup `main.ts` Entry Point (`src/main.ts`)**
    * Clear the default Vite template content from `src/main.ts`.
    * Add basic imports for `three.js` to confirm setup (e.g., `import * as THREE from 'three';`).

**Phase 2: Core 3D Scene & Controls**

*(Implement these within `src/main.ts` or dedicated modules)*

1.  **Task 2.1: Initialize `three.js` Core Components**
    * Create a `THREE.Scene`.
    * Create a `THREE.PerspectiveCamera` (set FOV, aspect ratio, near/far). Position it slightly away from the origin (e.g., `camera.position.z = 1000;`).
    * Create a `THREE.WebGLRenderer` (enable `antialias`). Set its size to match the container dimensions. Append `renderer.domElement` to the container div (`document.getElementById('graph-container')`).
2.  **Task 2.2: Add Basic Lighting**
    * Add `THREE.AmbientLight` to the scene for general illumination.
    * Add one or more `THREE.DirectionalLight`s to create shadows and highlights, position them appropriately.
3.  **Task 2.3: Implement Rendering Loop**
    * Create an animation loop function (e.g., `animate`).
    * Inside the loop:
        * Call `requestAnimationFrame(animate)`.
        * Render the scene: `renderer.render(scene, camera)`.
        * *(Will add controls update and graph update later)*
    * Start the loop by calling `animate()` once.
4.  **Task 2.4: Implement Mouse Rotation Controls**
    * Import `OrbitControls`: `import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';`
    * Instantiate `OrbitControls`, passing the `camera` and `renderer.domElement`.
    * Enable damping for smoother interaction: `controls.enableDamping = true;`
    * If damping is enabled, call `controls.update()` inside the `animate` loop.
5.  **Task 2.5: Handle Window Resizing**
    * Add an event listener for the `resize` event on the `window`.
    * In the handler:
        * Update the renderer size (`renderer.setSize`).
        * Update the camera's aspect ratio (`camera.aspect`).
        * Update the camera's projection matrix (`camera.updateProjectionMatrix`).

**Phase 3: Data Handling & Graph Structure**

1.  **Task 3.1: Define TypeScript Interfaces (`src/graphTypes.ts`)**
    * Create a new file `src/graphTypes.ts`.
    * Define interfaces for nodes, links, and the overall graph data structure:
        ```typescript
        export interface NodeData {
          id: string | number;
          name?: string; // Example property
          val?: number;  // Example property for sizing
          color?: string;// Example property
          [key: string]: any;
        }

        export interface LinkData {
          source: string | number;
          target: string | number;
          value?: number; // Example property for link strength/width
          [key: string]: any;
        }

        export interface GraphData {
          nodes: NodeData[];
          links: LinkData[];
        }
        ```
2.  **Task 3.2: Define and Load JSON Input Data**
    * Create a sample JSON file (e.g., `public/graphData.json` or `src/graphData.json`) using the structure defined by `GraphData`. Populate it with a few nodes and links.
        ```json
        // Example graphData.json
        {
          "nodes": [
            {"id": "A", "name": "Node A", "val": 10},
            {"id": "B", "name": "Node B", "val": 5},
            {"id": "C", "name": "Node C", "val": 8}
          ],
          "links": [
            {"source": "A", "target": "B"},
            {"source": "A", "target": "C"},
            {"source": "B", "target": "C", "value": 2}
          ]
        }
        ```
    * In your main script (`main.ts` or a new `graphLoader.ts`), implement logic to load this JSON. Use the `Workspace` API or direct import:
        ```typescript
        import { GraphData } from './graphTypes';
        // If in /public:
        // fetch('./graphData.json')
        //   .then(res => res.json())
        //   .then((data: GraphData) => { /* process data */ });
        // Or if in /src:
        import graphJson from './graphData.json';
        const initialGraphData: GraphData = graphJson;
        // Now use initialGraphData
        ```

**Phase 4: Force-Directed Graph Implementation**

1.  **Task 4.1: Integrate `force-graph` Library (`main.ts`)**
    * Import the 3D graph component: `import ForceGraph3D from 'force-graph';`
2.  **Task 4.2: Initialize and Configure `ForceGraph3D`**
    * Instantiate the graph: `const myGraph = ForceGraph3D();`
    * Associate it with the container div: `myGraph(document.getElementById('graph-container')!)`
3.  **Task 4.3: Define Node and Link Appearance**
    * Use `force-graph`'s accessor functions to control how nodes/links look. Return `three.js` objects for custom visuals if needed, or use basic configurations.
    * **Nodes:**
        * `myGraph.nodeVal(node => node.val || 1)` // Size nodes by 'val' property
        * `myGraph.nodeLabel(node => node.name || node.id)` // Show 'name' or 'id' on hover
        * `myGraph.nodeColor(node => node.color || '#ffffaa')` // Use 'color' property or a default
        * *(Optional Custom Node Geometry)* `myGraph.nodeThreeObject(node => { const mesh = new THREE.Mesh(...); return mesh; })`
    * **Links:**
        * `myGraph.linkLabel(link => `${link.source.id} > ${link.target.id}`)`
        * `myGraph.linkWidth(link => link.value || 1)` // Control width by 'value'
        * `myGraph.linkColor(() => '#ffffff')` // Default link color
        * *(Optional Custom Link Geometry)* `myGraph.linkThreeObject(...)`
4.  **Task 4.4: Pass Graph Data to `force-graph`**
    * After loading/parsing the JSON data (`initialGraphData`), feed it to the graph instance:
        ```typescript
        myGraph.graphData(initialGraphData);
        ```
5.  **Task 4.5: Configure Forces (Optional Refinement)**
    * Access the underlying D3 simulation to tune forces if needed:
        ```typescript
        myGraph.d3Force('link')?.distance(link => 50); // Example: Set link distance
        myGraph.d3Force('charge')?.strength(-120);   // Example: Set charge strength
        ```

**Phase 5: Extensibility & Dynamic Updates**

1.  **Task 5.1: Implement Functionality to Update Graph Data Dynamically**
    * Create functions or event handlers that can acquire new `GraphData`.
    * When new data is available, simply call `myGraph.graphData(newGraphData);` again. `force-graph` will intelligently update the visualization.
2.  **Task 5.2: Allow Adding Other `three.js` Elements to the Scene**
    * Access the scene managed by `force-graph`: `const internalScene = myGraph.scene();`
    * Create standard `three.js` objects (e.g., `THREE.Mesh`, `THREE.Sprite`, `THREE.Group`).
    * Add these objects directly to the `internalScene`: `internalScene.add(myCustomObject);`

**Phase 6: VR Readiness**

1.  **Task 6.1: Enable WebXR in the Renderer**
    * Access the renderer used by `force-graph`: `const internalRenderer = myGraph.renderer();`
    * Enable XR: `internalRenderer.xr.enabled = true;`
2.  **Task 6.2: Add VR Button**
    * Import the button: `import { VRButton } from 'three/examples/jsm/webxr/VRButton.js';`
    * Append the button to the document, associating it with the renderer:
        ```typescript
        document.body.appendChild(VRButton.createButton(internalRenderer));
        ```
3.  **Task 6.3: Adapt Render Loop for VR Compatibility**
    * `force-graph` generally handles its own render loop. Verify if it automatically uses `setAnimationLoop` when XR is enabled. If manual control over the loop was implemented earlier (Phase 2.3), ensure it's replaced or integrated with `renderer.setAnimationLoop()` for proper VR frame handling. `force-graph` likely handles this internally, so this step might just be verification.

**Phase 7: Development, Testing & Build**

1.  **Task 7.1: Development Workflow**
    * Run the Vite development server: `npm run dev`
    * Access the application in your browser (usually `http://localhost:5173`). Vite provides Hot Module Replacement (HMR) for faster development.
2.  **Task 7.2: Testing**
    * **Crucially, ensure you are running the development server and browser tests using Node.js v20.** If your terminal session changes, re-run `nvm use 20` before executing `npm run dev` or other npm scripts.
    * Test mouse rotation (`OrbitControls`).
    * Verify nodes and links are displayed according to `graphData.json`.
    * Test node/link hover labels.
    * Test dynamic updates if implemented.
    * Test VR entry using the VR button (requires a WebXR-compatible browser/device/emulator).
3.  **Task 7.3: Code Organization & Refinement**
    * Refactor code into logical modules (e.g., `setupScene.ts`, `graphVisualization.ts`, `dataLoader.ts`) for better maintainability.
    * Add comments explaining complex sections.
    * Optimize performance if needed (e.g., check `force-graph` options, consider simplifications for very large graphs).
4.  **Task 7.4: Production Build**
    * Generate optimized static assets for deployment: `npm run build`
    * The output will be in the `dist` folder. This folder can be served by any static file server.

---
This comprehensive plan provides a step-by-step guide from project setup to a functional, extensible 3D graph visualization ready for further development and VR integration, including the specific Node.js version requirement for testing.
