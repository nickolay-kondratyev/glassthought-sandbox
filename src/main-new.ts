import './style.css'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { VRButton } from 'three/examples/jsm/webxr/VRButton.js'
import ForceGraph3D from '3d-force-graph'
import { GraphData } from './graphTypes'

// Simple debug version
console.log('3D Force Graph Module loading...')

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
    
    console.log('3D Force Graph instance created successfully')
    
    // Sample data - directly in code to simplify testing
    const graphData = {
      nodes: [
        {"id": "1", "name": "Node A", "val": 10, "color": "#ff6347"},
        {"id": "2", "name": "Node B", "val": 8, "color": "#4682b4"},
        {"id": "3", "name": "Node C", "val": 6, "color": "#32cd32"},
        {"id": "4", "name": "Node D", "val": 5, "color": "#ffd700"}
      ],
      links: [
        {"source": "1", "target": "2", "value": 3},
        {"source": "1", "target": "3", "value": 2},
        {"source": "2", "target": "3", "value": 1},
        {"source": "2", "target": "4", "value": 4}
      ]
    }
    
    // Feed data to the graph
    console.log('Setting graph data directly...')
    myGraph.graphData(graphData)
    
    // Make sure Three.js renderer fills the container
    const width = container.clientWidth
    const height = container.clientHeight
    console.log(`Setting dimensions: ${width}x${height}`)
    myGraph.width(width)
    myGraph.height(height)
    
    // Make graph accessible globally for debugging
    console.log('Making graph globally accessible for debugging')
    window['myGraph'] = myGraph
    
    console.log('Graph initialization complete!')
  } catch (error) {
    console.error('ERROR during graph initialization:', error)
  }
})

// Re-export for module usage
export const initGraph = () => {
  console.log('Graph module loaded')
} 