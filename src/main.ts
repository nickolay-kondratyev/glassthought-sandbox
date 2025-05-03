import './style.css'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { VRButton } from 'three/examples/jsm/webxr/VRButton.js'
import ForceGraph3D from '3d-force-graph'
import { GraphData } from './graphTypes'

// Wait for DOM content to be fully loaded
document.addEventListener('DOMContentLoaded', () => {
  console.log('DOM loaded, initializing graph...')
  
  // Container DOM element
  const container = document.getElementById('graph-container')
  
  if (!container) {
    console.error('Could not find graph container element')
    return
  }
  
  console.log('Container found:', container)
  
  try {
    // Initialize the 3D force graph
    console.log('Creating 3D Force Graph instance...')
    const myGraph = ForceGraph3D()
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
    
    console.log('3D Force Graph instance created successfully')
    
    // Make sure Three.js renderer fills the container
    const width = container.clientWidth
    const height = container.clientHeight
    console.log(`Setting dimensions: ${width}x${height}`)
    myGraph.width(width)
    myGraph.height(height)
    
    // Access and configure the internal Three.js objects
    console.log('Accessing Three.js objects...')
    const internalScene = myGraph.scene()
    const internalRenderer = myGraph.renderer()
    const internalCamera = myGraph.camera()
    
    // Add ambient light for better visibility
    console.log('Adding lights...')
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6)
    internalScene.add(ambientLight)
    
    // Add directional light to create shadows
    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
    directionalLight.position.set(1, 1, 1)
    internalScene.add(directionalLight)
    
    // Set up orbit controls for mouse rotation
    console.log('Setting up controls...')
    const controls = myGraph.controls()
    if (controls) {
      controls.enableDamping = true
      controls.dampingFactor = 0.25
    } else {
      console.warn('Controls not available')
    }
    
    // Enable WebXR (VR)
    console.log('Setting up VR...')
    if (internalRenderer) {
      internalRenderer.xr.enabled = true
      document.body.appendChild(VRButton.createButton(internalRenderer))
    }
    
    // Handle window resizing
    console.log('Adding resize handler...')
    window.addEventListener('resize', () => {
      const width = container.clientWidth
      const height = container.clientHeight
      
      myGraph.width(width)
      myGraph.height(height)
    })
    
    // Load graph data
    async function loadGraphData() {
      console.log('Loading graph data...')
      try {
        const response = await fetch('./graphData.json')
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        const data: GraphData = await response.json()
        console.log('Loaded graph data:', data)
        
        // Feed data to the graph
        console.log('Setting graph data...')
        myGraph.graphData(data)
        
        // Configure forces
        console.log('Configuring physics...')
        const linkForce = myGraph.d3Force('link')
        if (linkForce) {
          linkForce.distance(link => 50)
          console.log('Link force configured')
        } else {
          console.warn('Link force not available')
        }
        
        const chargeForce = myGraph.d3Force('charge')
        if (chargeForce) {
          chargeForce.strength(-120)
          console.log('Charge force configured')
        } else {
          console.warn('Charge force not available')
        }
        
        // Center the camera on the graph
        console.log('Centering view...')
        setTimeout(() => {
          myGraph.zoomToFit(1000, 30)
          console.log('View centered')
        }, 1000)
      } catch (error) {
        console.error('Error loading graph data:', error)
      }
    }
    
    // Call the load function
    loadGraphData()
    
    // Make graph accessible globally for debugging
    console.log('Making graph globally accessible for debugging')
    window['myGraph'] = myGraph
    
    // Export for extensibility
    window['graphAPI'] = {
      myGraph,
      internalScene,
      internalRenderer,
      internalCamera
    }
    
    console.log('Graph initialization complete!')
  } catch (error) {
    console.error('ERROR during graph initialization:', error)
  }
})

// Re-export for module usage
export const initGraph = () => {
  console.log('Graph module loaded')
} 