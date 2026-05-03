package controller;

import view.GraphPanel;
import service.FileParser;
import javax.swing.*;
import java.awt.*;

public class GraphController extends JFrame {
    private GraphPanel view = new GraphPanel();
    private FileParser service = new FileParser();

    public GraphController() {
        setTitle("Java Module");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btn = new JButton("Open C File");
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
        SwingUtilities.invokeLater(() -> new GraphController().setVisible(true));
    }
}
