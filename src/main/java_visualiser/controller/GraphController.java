package controller;

import view.GraphPanel;
import service.FileParser;
import javax.swing.*;
import java.awt.*;

public class GraphController extends JFrame {
    private final GraphPanel view;
    private final FileParser service; 
    public GraphController(GraphPanel view, FileParser service) {
        this.view = view;
        this.service = service;

        setTitle("JIMP2 Graph Visualizer");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btn = new JButton("Open C-Generated File");
        btn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    FileParser.GraphContainer data = service.parse(fc.getSelectedFile());
                    view.updateGraph(data.nodes, data.edges);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        add(view, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphPanel graphPanel = new GraphPanel();
            FileParser fileParser = new FileParser();
            
            new GraphController(graphPanel, fileParser).setVisible(true);
        });
    }
}
