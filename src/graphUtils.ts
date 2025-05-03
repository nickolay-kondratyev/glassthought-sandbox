import { GraphData, NodeData, LinkData } from './graphTypes';

/**
 * Helper to get the graph instance from the global scope
 */
function getGraph() {
  if (typeof window !== 'undefined' && window['myGraph']) {
    return window['myGraph'];
  }
  console.error('Graph not initialized yet');
  return null;
}

/**
 * Adds a new node to the graph
 * @param node The node to add
 * @returns The updated graph data
 */
export function addNode(node: NodeData): GraphData | null {
  const myGraph = getGraph();
  if (!myGraph) return null;
  
  const currentData = myGraph.graphData();
  const updatedData = {
    nodes: [...currentData.nodes, node],
    links: [...currentData.links]
  };
  myGraph.graphData(updatedData);
  return updatedData;
}

/**
 * Adds a new link to the graph
 * @param link The link to add
 * @returns The updated graph data
 */
export function addLink(link: LinkData): GraphData | null {
  const myGraph = getGraph();
  if (!myGraph) return null;
  
  const currentData = myGraph.graphData();
  const updatedData = {
    nodes: [...currentData.nodes],
    links: [...currentData.links, link]
  };
  myGraph.graphData(updatedData);
  return updatedData;
}

/**
 * Removes a node and all its connected links from the graph
 * @param nodeId The ID of the node to remove
 * @returns The updated graph data
 */
export function removeNode(nodeId: string | number): GraphData | null {
  const myGraph = getGraph();
  if (!myGraph) return null;
  
  const currentData = myGraph.graphData();
  const updatedData = {
    nodes: currentData.nodes.filter(node => node.id !== nodeId),
    links: currentData.links.filter(
      link => link.source !== nodeId && link.target !== nodeId
    )
  };
  myGraph.graphData(updatedData);
  return updatedData;
}

/**
 * Updates the graph with completely new data
 * @param data The new graph data
 */
export function updateGraphData(data: GraphData): void {
  const myGraph = getGraph();
  if (!myGraph) return;
  
  myGraph.graphData(data);
}

/**
 * Updates the properties of an existing node
 * @param nodeId The ID of the node to update
 * @param properties The properties to update
 * @returns The updated graph data
 */
export function updateNode(nodeId: string | number, properties: Partial<NodeData>): GraphData | null {
  const myGraph = getGraph();
  if (!myGraph) return null;
  
  const currentData = myGraph.graphData();
  const updatedData = {
    nodes: currentData.nodes.map(node => 
      node.id === nodeId ? { ...node, ...properties } : node
    ),
    links: [...currentData.links]
  };
  myGraph.graphData(updatedData);
  return updatedData;
}

/**
 * Gets the current graph data
 * @returns The current graph data
 */
export function getGraphData(): GraphData | null {
  const myGraph = getGraph();
  if (!myGraph) return null;
  
  return myGraph.graphData();
}

/**
 * Centers the graph view
 * @param duration Animation duration in milliseconds
 * @param padding Padding around the centered view
 */
export function centerGraph(duration: number = 1000, padding: number = 30): void {
  const myGraph = getGraph();
  if (!myGraph) return;
  
  myGraph.zoomToFit(duration, padding);
} 