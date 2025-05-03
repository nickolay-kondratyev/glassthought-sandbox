# 3D Force-Directed Graph Visualization

A fully functioning 3D force-directed graph visualization built with Vite, TypeScript, and Three.js.

## Features

- 3D visualization of nodes and relationships using force-directed layout
- Mouse rotation and zoom controls
- Dynamic graph updates via API
- VR compatibility (WebXR)
- Customizable node and link appearance

## Prerequisites

- Node.js version 20 or later

## Installation

1. Ensure you have Node.js v20 installed:
   ```bash
   nvm use 20
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Development

Run the development server:
```bash
npm run dev
```

Visit `http://localhost:5173` in your browser to see the application.

## Building for Production

Generate a production build:
```bash
npm run build
```

The output will be in the `dist` directory, which can be served by any static file server.

## Usage

### Graph Data Format

The application loads graph data from `public/graphData.json`. The format is:

```json
{
  "nodes": [
    {"id": "1", "name": "Node A", "val": 10, "color": "#ff6347"},
    ...
  ],
  "links": [
    {"source": "1", "target": "2", "value": 3},
    ...
  ]
}
```

### API

The application provides an API for dynamic graph updates:

```typescript
// Import utility functions
import { 
  addNode, 
  addLink, 
  removeNode, 
  updateNode, 
  getGraphData, 
  updateGraphData, 
  centerGraph 
} from './src/graphUtils';

// Add a new node
addNode({ id: 'newNode', name: 'New Node', val: 5, color: '#ff0000' });

// Add a new link
addLink({ source: 'existingNode', target: 'newNode', value: 2 });

// Remove a node (and its connected links)
removeNode('nodeToRemove');

// Update node properties
updateNode('existingNode', { color: '#00ff00', val: 8 });

// Get current graph data
const data = getGraphData();

// Update entire graph with new data
updateGraphData(newData);

// Center the graph view
centerGraph(1000, 30);
```

### VR Support

The application includes basic VR support via WebXR. Click the VR button in the bottom right corner to enter VR mode (requires a compatible VR headset and browser).

## Extending

You can access the internal Three.js components for further customization:

```typescript
import { internalScene, internalRenderer, internalCamera } from './src/main';

// Add custom Three.js objects to the scene
const customObject = new THREE.Mesh(
  new THREE.SphereGeometry(5),
  new THREE.MeshStandardMaterial({ color: 0xff0000 })
);
customObject.position.set(100, 0, 0);
internalScene.add(customObject);
``` 