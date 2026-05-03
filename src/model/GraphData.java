package model;

public class GraphData {
    public static class Node {
        public int id;
        public double x, y;
        public Node(int id, double x, double y) { this.id = id; this.x = x; this.y = y; }
    }

    public static class Edge {
        public int sourceId, targetId;
        public Edge(int s, int t) { this.sourceId = s; this.targetId = t; }
    }
}
