export interface NodeData {
  id: string | number;
  name?: string;
  val?: number;  // Used for node sizing
  color?: string;
  [key: string]: any;
}

export interface LinkData {
  source: string | number;
  target: string | number;
  value?: number; // Used for link strength/width
  [key: string]: any;
}

export interface GraphData {
  nodes: NodeData[];
  links: LinkData[];
} 