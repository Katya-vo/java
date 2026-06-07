package model;

import java.util.ArrayList;
import java.util.List;

public class GraphData {

    public static class Node {
        public int id;
        public double x, y;

        public Node(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    public static class Edge {
        public int sourceId, targetId;

        public Edge(int s, int t) {
            this.sourceId = s;
            this.targetId = t;
        }
    }

    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addNode(Node node) {
        if (node != null) nodes.add(node);
    }

    public void addEdge(Edge edge) {
        if (edge != null) edges.add(edge);
    }

    public List<Node> getNodes() { return nodes; }
    public List<Edge> getEdges() { return edges; }
    
    public void updateNodePosition(int id, double x, double y) {
        for (Node n : nodes) {
            if (n.id == id) {
                n.x = x;
                n.y = y;
                break;
            }
        }
    }
}
