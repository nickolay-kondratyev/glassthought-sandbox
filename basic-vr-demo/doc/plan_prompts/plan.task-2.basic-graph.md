**Plan: Visualize Basic 3D Graph from JSON Configuration**

**Objective:**

Modify the existing Three.js/WebXR application to parse a predefined JSON object describing a graph (nodes with types and links) and render it in 3D space. The graph nodes will be represented by different geometric shapes (triangle, square, sphere), and links will be represented by lines connecting the corresponding nodes. The entire graph structure should rotate as the cube did previously.

**Technology Stack:**

* **Language:** TypeScript
* **3D Library:** Three.js
* **Build Tool / Dev Server:** Vite
* **VR Integration:** WebXR (via Three.js helpers)
* **Package Manager:** npm

**Prerequisites:**

* The project state after successfully completing the "Create Basic 3D VR Visualization Demo" plan (rotating cube works).
* A command-line interface (shell/terminal).
* Node.js installed (LTS version recommended, includes npm).

**Execution Steps:**

**Step 1: Define Graph Data and Types (in `src/main.ts`)**

1.  **Action:** Define TypeScript interfaces to represent the structure of the graph configuration JSON.
    * Create interfaces `GraphNode`, `GraphLink`, and `GraphData`.
2.  **Action:** Create a constant variable holding the graph data JSON object directly within the `src/main.ts` file.
    * *Note:* For this initial step, we embed the data. In a real application, this might be fetched from a file or API.
    * *Crucially*, add `position` properties (as `{x, y, z}` objects) to the node definitions within the JSON data. Without explicit positions, the nodes would all render at the origin (0,0,0). We will define some simple, hardcoded positions for this example.

    ```typescript
    // Add near the top of src/main.ts, after imports

    interface GraphNode {
      node_type: 'triangle' | 'square' | 'sphere';
      id: string;
      position: { x: number; y: number; z: number };
      // Optional: Add color or other properties later
    }

    interface GraphLink {
      from: string;
      to: string;
      // Optional: Add color, thickness, or other properties later
    }

    interface GraphData {
      nodes: GraphNode[];
      links: GraphLink[];
    }

    // Example Graph Data (with positions)
    const graphData: GraphData = {
      nodes: [
        { node_type: 'triangle', id: 'node-1', position: { x: -2, y: 0, z: 0 } },
        { node_type: 'square', id: 'node-2', position: { x: 0, y: 0, z: 0 } },
        { node_type: 'sphere', id: 'node-3', position: { x: 2, y: 0, z: 0 } },
        // Add more nodes as desired
      ],
      links: [
        { from: 'node-1', to: 'node-2' },
        { from: 'node-2', to: 'node-3' },
        // Add more links as desired
      ]
    };
    ```
    * *(Verification: Check that the interfaces and the `graphData` constant are added to `src/main.ts` and include `position` data for each node.)*

**Step 2: Prepare Scene and Refactor Content Creation (in `src/main.ts`)**

1.  **Action:** Modify the "Content Creation" section.
    * **Remove** the existing code that creates the single cube geometry, material, and mesh (`geometry`, `material`, `cube`, and `scene.add(cube)`).
2.  **Action:** Add basic lighting for better visualization of 3D shapes (optional but recommended).
    ```typescript
    // Add after scene creation
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6); // Soft white light
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.0); // White directional light
    directionalLight.position.set(5, 10, 7.5);
    scene.add(directionalLight);
    ```
3.  **Action:** Create a `THREE.Group` to hold all graph elements (nodes and links). This allows rotating the entire graph easily.
    ```typescript
    // Add before creating graph elements
    const graphGroup = new THREE.Group();
    scene.add(graphGroup); // Add the group to the scene
    ```
4.  **Action:** Create a `Map` to store references to the created node meshes, using their `id` as the key. This is essential for easily finding nodes when creating links.
    ```typescript
    // Add before creating graph elements
    const nodeMeshes = new Map<string, THREE.Mesh>();
    ```
    * *(Verification: Check that the cube creation code is removed, lighting is added (optional), `graphGroup` is created and added to the scene, and `nodeMeshes` Map is declared.)*

**Step 3: Implement Node Visualization (in `src/main.ts`)**

1.  **Action:** Loop through the `graphData.nodes` array. For each node:
    * Determine the geometry based on `node_type`:
        * `'triangle'`: Use `THREE.TetrahedronGeometry(0.5)` (a simple 3D triangle shape).
        * `'square'`: Use `THREE.BoxGeometry(0.8, 0.8, 0.8)`.
        * `'sphere'`: Use `THREE.SphereGeometry(0.5, 32, 16)`.
    * Create a material. Use `THREE.MeshStandardMaterial` for better interaction with lights. Assign different colors based on type or use a default.
        * Example: `new THREE.MeshStandardMaterial({ color: 0xff0000 })` for triangles, `0x00ff00` for squares, `0x0000ff` for spheres.
    * Create the `THREE.Mesh` using the geometry and material.
    * Set the mesh's `position` using the `x`, `y`, `z` values from the node's `position` property in `graphData`.
    * Add the created mesh to the `graphGroup`.
    * Store the mesh in the `nodeMeshes` Map using the node's `id` as the key.

    ```typescript
    // Add within the Content Creation section, after declaring graphGroup and nodeMeshes

    // --- Create Nodes ---
    graphData.nodes.forEach(node => {
        let geometry: THREE.BufferGeometry;
        let material: THREE.Material;

        switch (node.node_type) {
            case 'triangle':
                geometry = new THREE.TetrahedronGeometry(0.5);
                material = new THREE.MeshStandardMaterial({ color: 0xff0000, roughness: 0.5, metalness: 0.1 }); // Red
                break;
            case 'square':
                geometry = new THREE.BoxGeometry(0.8, 0.8, 0.8);
                material = new THREE.MeshStandardMaterial({ color: 0x00ff00, roughness: 0.5, metalness: 0.1 }); // Green
                break;
            case 'sphere':
                geometry = new THREE.SphereGeometry(0.5, 32, 16);
                material = new THREE.MeshStandardMaterial({ color: 0x0000ff, roughness: 0.5, metalness: 0.1 }); // Blue
                break;
            default:
                // Default to a small sphere if type is unknown
                geometry = new THREE.SphereGeometry(0.2, 8, 8);
                material = new THREE.MeshStandardMaterial({ color: 0x888888, roughness: 0.5, metalness: 0.1 }); // Grey
        }

        const mesh = new THREE.Mesh(geometry, material);
        mesh.position.set(node.position.x, node.position.y, node.position.z);

        // Add custom data to easily retrieve the ID if needed later (optional)
        mesh.userData = { id: node.id, type: node.node_type };

        graphGroup.add(mesh); // Add node mesh to the group
        nodeMeshes.set(node.id, mesh); // Store mesh reference by ID
    });
    ```
    * *(Verification: Check that the code iterates through nodes, creates appropriate geometries/materials/meshes, sets positions, adds meshes to `graphGroup`, and populates the `nodeMeshes` Map.)*

**Step 4: Implement Link Visualization (in `src/main.ts`)**

1.  **Action:** Loop through the `graphData.links` array. For each link:
    * Retrieve the 'from' and 'to' node meshes from the `nodeMeshes` Map using `link.from` and `link.to` IDs.
    * Check if both meshes were found.
    * Get the world positions of the 'from' and 'to' meshes (`mesh.position` works here as they are direct children of the group which is at the origin initially).
    * Create an array containing these two `Vector3` positions.
    * Create a `THREE.BufferGeometry` from these points using `setFromPoints()`.
    * Create a `THREE.LineBasicMaterial` for the links (e.g., white or grey).
    * Create a `THREE.Line` object using the geometry and material.
    * Add the line to the `graphGroup`.

    ```typescript
    // Add after the node creation loop

    // --- Create Links ---
    graphData.links.forEach(link => {
        const fromNodeMesh = nodeMeshes.get(link.from);
        const toNodeMesh = nodeMeshes.get(link.to);

        if (fromNodeMesh && toNodeMesh) {
            const points = [
                fromNodeMesh.position.clone(), // Use position relative to the group
                toNodeMesh.position.clone()
            ];

            const linkGeometry = new THREE.BufferGeometry().setFromPoints(points);
            const linkMaterial = new THREE.LineBasicMaterial({ color: 0xffffff, linewidth: 2 }); // White line

            const line = new THREE.Line(linkGeometry, linkMaterial);
            graphGroup.add(line); // Add link line to the group
        } else {
            console.warn(`Could not create link: Node ID ${!fromNodeMesh ? link.from : link.to} not found.`);
        }
    });
    ```
    * *(Verification: Check that the code iterates through links, retrieves node meshes from the map, creates line geometries/materials/objects, and adds lines to `graphGroup`.)*

**Step 5: Update Animation Loop (in `src/main.ts`)**

1.  **Action:** Modify the `renderer.setAnimationLoop` function to rotate the `graphGroup` instead of the individual `cube`.

    ```typescript
    // Modify the existing animation loop

    renderer.setAnimationLoop((timestamp, frame) => {
        // Animate the entire graph group
        graphGroup.rotation.x += 0.005; // Slower rotation might be better
        graphGroup.rotation.y += 0.005;

        // Render the scene from the perspective of the camera
        renderer.render(scene, camera);
    });
    ```
    * *(Verification: Check that the animation loop now targets `graphGroup.rotation`.)*

**Step 6: Provide Complete Updated `src/main.ts`**

1.  **Action:** Replace the entire content of `src/main.ts` with the following code, which incorporates all the changes from Steps 1-5.

    ```typescript
    // src/main.ts
    import * as THREE from 'three';
    import { VRButton } from 'three/examples/jsm/webxr/VRButton.js';

    // --- Define Graph Data Types ---
    interface GraphNode {
      node_type: 'triangle' | 'square' | 'sphere';
      id: string;
      position: { x: number; y: number; z: number };
    }

    interface GraphLink {
      from: string;
      to: string;
    }

    interface GraphData {
      nodes: GraphNode[];
      links: GraphLink[];
    }

    // --- Example Graph Data ---
    const graphData: GraphData = {
      nodes: [
        { node_type: 'triangle', id: 'node-1', position: { x: -2, y: 1, z: 0 } },
        { node_type: 'square', id: 'node-2', position: { x: 0, y: 0, z: 0 } },
        { node_type: 'sphere', id: 'node-3', position: { x: 2, y: 1, z: 0 } },
        { node_type: 'sphere', id: 'node-4', position: { x: 0, y: -1, z: 1 } }, // Added another node
      ],
      links: [
        { from: 'node-1', to: 'node-2' },
        { from: 'node-2', to: 'node-3' },
        { from: 'node-2', to: 'node-4' }, // Added another link
        { from: 'node-1', to: 'node-4' }, // Added another link
      ]
    };

    // --- Basic Scene Setup ---
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(
      75, // Field of View
      window.innerWidth / window.innerHeight, // Aspect Ratio
      0.1, // Near clipping plane
      1000 // Far clipping plane
    );
    camera.position.z = 7; // Move camera back a bit further for graph view

    // --- Lighting ---
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.0);
    directionalLight.position.set(5, 10, 7.5);
    scene.add(directionalLight);

    // --- Renderer Setup ---
    const renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setPixelRatio(window.devicePixelRatio);
    document.body.appendChild(renderer.domElement);

    // --- WebXR Setup ---
    renderer.xr.enabled = true;
    document.body.appendChild(VRButton.createButton(renderer));

    // --- Content Creation ---
    const graphGroup = new THREE.Group(); // Group to hold all graph elements
    scene.add(graphGroup);

    const nodeMeshes = new Map<string, THREE.Mesh>(); // Map to store node meshes by ID

    // --- Create Nodes ---
    graphData.nodes.forEach(node => {
        let geometry: THREE.BufferGeometry;
        let material: THREE.Material;
        const nodeSize = 0.5; // Base size for nodes

        switch (node.node_type) {
            case 'triangle':
                geometry = new THREE.TetrahedronGeometry(nodeSize);
                material = new THREE.MeshStandardMaterial({ color: 0xff0000, roughness: 0.5, metalness: 0.1 }); // Red
                break;
            case 'square':
                // Make box slightly smaller relative to radius-based shapes
                geometry = new THREE.BoxGeometry(nodeSize * 1.2, nodeSize * 1.2, nodeSize * 1.2);
                material = new THREE.MeshStandardMaterial({ color: 0x00ff00, roughness: 0.5, metalness: 0.1 }); // Green
                break;
            case 'sphere':
                geometry = new THREE.SphereGeometry(nodeSize, 32, 16);
                material = new THREE.MeshStandardMaterial({ color: 0x0000ff, roughness: 0.5, metalness: 0.1 }); // Blue
                break;
            default:
                geometry = new THREE.SphereGeometry(nodeSize * 0.4, 8, 8); // Smaller grey sphere for unknown
                material = new THREE.MeshStandardMaterial({ color: 0x888888, roughness: 0.5, metalness: 0.1 }); // Grey
        }

        const mesh = new THREE.Mesh(geometry, material);
        mesh.position.set(node.position.x, node.position.y, node.position.z);
        mesh.userData = { id: node.id, type: node.node_type }; // Store ID for potential interaction later

        graphGroup.add(mesh);
        nodeMeshes.set(node.id, mesh);
    });

    // --- Create Links ---
    graphData.links.forEach(link => {
        const fromNodeMesh = nodeMeshes.get(link.from);
        const toNodeMesh = nodeMeshes.get(link.to);

        if (fromNodeMesh && toNodeMesh) {
            const points = [
                fromNodeMesh.position.clone(),
                toNodeMesh.position.clone()
            ];

            const linkGeometry = new THREE.BufferGeometry().setFromPoints(points);
            // Use LineMaterial for thicker lines if needed (requires extra setup: Line2, LineGeometry from examples)
            // For simplicity, using LineBasicMaterial:
            const linkMaterial = new THREE.LineBasicMaterial({ color: 0xaaaaaa, linewidth: 2 }); // Light grey line

            const line = new THREE.Line(linkGeometry, linkMaterial);
            graphGroup.add(line);
        } else {
            console.warn(`Could not create link: Node ID ${!fromNodeMesh ? link.from : link.to} not found.`);
        }
    });

    // --- Responsiveness ---
    window.addEventListener('resize', () => {
        if (!renderer.xr.isPresenting) {
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        }
    });

    // --- Animation Loop ---
    renderer.setAnimationLoop((timestamp, frame) => {
        // Animate the entire graph group
        graphGroup.rotation.x += 0.002; // Slower rotation
        graphGroup.rotation.y += 0.003; // Asymmetric rotation

        // Render the scene
        renderer.render(scene, camera);
    });

    console.log("Three.js graph visualization initialized successfully.");
    ```
    * *(Verification: Check that the complete code is present in `src/main.ts`, replacing the previous content entirely.)*

**Step 7: Verification Instructions**

1.  **Action:** Ensure the development server is running. If not, start it:
    ```bash
    npm run dev
    ```
2.  **Verification (Desktop):**
    * Open a web browser and navigate to the local URL (e.g., `http://localhost:5173/`).
    * **Expected Result:** You should see a 3D representation of the graph defined in `graphData`. Specifically, a red tetrahedron (`node-1`), a green cube (`node-2`), and blue spheres (`node-3`, `node-4`) positioned according to the coordinates. Grey lines should connect `node-1` to `node-2`, `node-2` to `node-3`, `node-2` to `node-4`, and `node-1` to `node-4`. The entire structure (nodes and links together) should be slowly rotating. The "ENTER VR" button should be visible.
3.  **Verification (VR Headset):**
    * Connect the VR headset to the same network and navigate to the computer's network IP address and port (e.g., `http://<YOUR_COMPUTER_IP>:5173/`).
    * **Expected Result:** You should see the same rotating 3D graph. Clicking the "ENTER VR" button should switch to an immersive view where the graph rotates in front of you, and head movements control the camera.

This plan outlines the necessary steps to transform the rotating cube demo into a basic 3D graph visualization driven by a JSON configuration.
