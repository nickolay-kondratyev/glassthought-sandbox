// src/main.ts
import * as THREE from 'three';
// Import the VRButton helper utility from Three.js examples
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
camera.position.z = 7;

// --- Lighting ---
const ambientLight = new THREE.AmbientLight(0xffffff, 0.6); // Soft white light
scene.add(ambientLight);
const directionalLight = new THREE.DirectionalLight(0xffffff, 1.0); // White directional light
directionalLight.position.set(5, 10, 7.5);
scene.add(directionalLight);

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

// --- User Rotation Controls ---
// Variables to track mouse position and state
let isDragging = false;
let previousMousePosition = {
    x: 0,
    y: 0
};

// Sensitivity factor for rotation
const rotationSensitivity = 0.01;

// Mouse event listeners
document.addEventListener('mousedown', (event) => {
    isDragging = true;
    previousMousePosition = {
        x: event.clientX,
        y: event.clientY
    };
});

document.addEventListener('mousemove', (event) => {
    if (isDragging) {
        // Calculate mouse movement delta
        const deltaMove = {
            x: event.clientX - previousMousePosition.x,
            y: event.clientY - previousMousePosition.y
        };

        // Rotate the graph group based on mouse movement
        // Vertical movement (y) rotates around X-axis
        // Horizontal movement (x) rotates around Y-axis
        graphGroup.rotation.x += deltaMove.y * rotationSensitivity;
        graphGroup.rotation.y += deltaMove.x * rotationSensitivity;

        // Update previous position for next move event
        previousMousePosition = {
            x: event.clientX,
            y: event.clientY
        };
    }
});

document.addEventListener('mouseup', () => {
    isDragging = false;
});

// Also handle case when mouse leaves the window
document.addEventListener('mouseout', () => {
    isDragging = false;
});

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

// 11. Define the animation loop without automatic rotation
renderer.setAnimationLoop((timestamp, frame) => {
    // Removed automatic rotation:
    // graphGroup.rotation.x += 0.002;
    // graphGroup.rotation.y += 0.003;

    // Render the scene from the perspective of the camera
    // The renderer automatically handles camera updates based on headset pose when in VR
    renderer.render(scene, camera);
});

console.log("Three.js graph visualization initialized successfully."); 