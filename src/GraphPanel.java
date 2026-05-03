package view;

import model.GraphData;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    private List<GraphData.Node> nodes = new ArrayList<>();
    private List<GraphData.Edge> edges = new ArrayList<>();

    public void updateGraph(List<GraphData.Node> nodes, List<GraphData.Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.LIGHT_GRAY);
        for (GraphData.Edge edge : edges) {
            GraphData.Node s = find(edge.sourceId);
            GraphData.Node t = find(edge.targetId);
            if (s != null && t != null) g2.draw(new Line2D.Double(s.x, s.y, t.x, t.y));
        }

        g2.setColor(new Color(0, 102, 204));
        for (GraphData.Node node : nodes) {
            g2.fill(new Ellipse2D.Double(node.x - 6, node.y - 6, 12, 12));
            g2.drawString(String.valueOf(node.id), (int)node.x + 8, (int)node.y);
        }
    }

    private GraphData.Node find(int id) {
        return nodes.stream().filter(n -> n.id == id).findFirst().orElse(null);
    }
}
