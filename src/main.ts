import './style.css'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { VRButton } from 'three/examples/jsm/webxr/VRButton.js'
import ForceGraph3D from 'force-graph'
import { GraphData } from './graphTypes'

// Wait for DOM content to be fully loaded
document.addEventListener('DOMContentLoaded', () => {
  // Container DOM element
  const container = document.getElementById('graph-container')
  
  if (!container) {
    console.error('Could not find graph container element')
    return
  }
  
  // Initialize the 3D force graph
  const myGraph = ForceGraph3D({ controlType: 'orbit' })
    (container)
    .nodeLabel(node => node.name || node.id)
    .nodeVal(node => node.val || 1)
    .nodeColor(node => node.color || '#ffffaa')
    .linkLabel(link => `${link.source} → ${link.target}`)
    .linkWidth(link => link.value || 1)
    .linkColor(() => '#ffffff')
    .linkOpacity(0.5)
    .backgroundColor('#000011')
    .showNavInfo(true)
  
  // Make sure Three.js renderer fills the container
  const width = container.clientWidth
  const height = container.clientHeight
  myGraph.width(width)
  myGraph.height(height)
  
  // Access and configure the internal Three.js objects
  const internalScene = myGraph.scene()
  const internalRenderer = myGraph.renderer()
  const internalCamera = myGraph.camera()
  
  // Add ambient light for better visibility
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
  internalScene.add(ambientLight)
  
  // Add directional light to create shadows
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
  directionalLight.position.set(1, 1, 1)
  internalScene.add(directionalLight)
  
  // Set up orbit controls for mouse rotation
  const controls = myGraph.controls()
  if (controls) {
    controls.enableDamping = true
    controls.dampingFactor = 0.25
  }
  
  // Enable WebXR (VR)
  if (internalRenderer) {
    internalRenderer.xr.enabled = true
    document.body.appendChild(VRButton.createButton(internalRenderer))
  }
  
  // Handle window resizing
  window.addEventListener('resize', () => {
    const width = container.clientWidth
    const height = container.clientHeight
    
    myGraph.width(width)
    myGraph.height(height)
  })
  
  // Load graph data
  async function loadGraphData() {
    try {
      const response = await fetch('./graphData.json')
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const data: GraphData = await response.json()
      console.log('Loaded graph data:', data)
      
      // Feed data to the graph
      myGraph.graphData(data)
      
      // Configure forces
      myGraph.d3Force('link')?.distance(link => 50)
      myGraph.d3Force('charge')?.strength(-120)
      
      // Center the camera on the graph
      setTimeout(() => {
        myGraph.zoomToFit(1000, 30)
      }, 1000)
    } catch (error) {
      console.error('Error loading graph data:', error)
    }
  }
  
  // Call the load function
  loadGraphData()
  
  // Make graph accessible globally for debugging
  window['myGraph'] = myGraph
  
  // Export for extensibility
  window['graphAPI'] = {
    myGraph,
    internalScene,
    internalRenderer,
    internalCamera
  }
})

// Re-export for module usage
export const initGraph = () => {
  console.log('Graph module loaded')
} 