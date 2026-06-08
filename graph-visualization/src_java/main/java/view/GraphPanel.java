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
    
    private boolean showLabels = true;
    private boolean showWeights = true;
    private double zoomFactor = 1.0;

    public GraphPanel() {
        setBackground(Color.WHITE);
    }

    public void updateGraph(List<GraphData.Node> nodes, List<GraphData.Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        repaint();
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }

    public void setShowWeights(boolean showWeights) {
        this.showWeights = showWeights;
        repaint();
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }

    public double getZoomFactor() { return zoomFactor; }
    public List<GraphData.Node> getNodes() { return nodes; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (nodes.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawString("Wybierz plik wejściowy i wygeneruj układ za pomocą panelu u góry.", 20, 40);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // przesunięcie (0,0) z C na środek okna w Javie
        g2.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2.scale(zoomFactor, zoomFactor);

        // rysowanie połączeń
        g2.setStroke(new BasicStroke(1.2f));
        for (GraphData.Edge edge : edges) {
            GraphData.Node s = findNode(edge.sourceId);
            GraphData.Node t = findNode(edge.targetId);
            if (s != null && t != null) {
                g2.setColor(Color.GRAY);
                g2.draw(new Line2D.Double(s.x, s.y, t.x, t.y));

                if (showWeights) {
                    g2.setColor(new Color(153, 0, 76));
                    g2.setFont(new Font("Arial", Font.ITALIC, 10));
                    double midX = (s.x + t.x) / 2.0;
                    double midY = (s.y + t.y) / 2.0;
                    String text = String.format("%s (%.2f)", edge.label, edge.weight);
                    g2.drawString(text, (int) midX + 4, (int) midY - 4);
                }
            }
        }

        // rysowanie wierzchołków
        for (GraphData.Node node : nodes) {
            g2.setColor(new Color(0, 102, 204));
            g2.fill(new Ellipse2D.Double(node.x - 8, node.y - 8, 16, 16));
            
            if (showLabels) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(String.valueOf(node.id), (int) node.x + 10, (int) node.y + 5);
            }
        }
    }

    private GraphData.Node findNode(int id) {
        return nodes.stream().filter(n -> n.id == id).findFirst().orElse(null);
    }
}
