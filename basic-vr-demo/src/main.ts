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