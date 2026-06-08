package model;

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
        public String label;
        public int sourceId;
        public int targetId;
        public double weight;

        public Edge(String label, int s, int t, double w) {
            this.label = label;
            this.sourceId = s;
            this.targetId = t;
            this.weight = w;
        }
    }
}
