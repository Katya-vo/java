package controller;

import view.GraphPanel;
import model.GraphData;
import service.FileParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class GraphController extends JFrame {
    private final GraphPanel view;
    private final FileParser service;

    private File selectedInputFile = null;
    private JLabel fileStatusLabel;
    private JComboBox<String> layoutCombo;
    private JComboBox<String> formatCombo;
    private GraphData.Node draggedNode = null;

    public GraphController(GraphPanel view, FileParser service) {
        this.view = view;
        this.service = service;

        setTitle("Wizualizator Grafu Planarnego - Integracja C & Java");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(createTopPanel(), BorderLayout.NORTH);
        add(view, BorderLayout.CENTER);

        initMouseListeners();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnSelectFile = new JButton("Wybierz plik wejściowy...");
        fileStatusLabel = new JLabel(" Brak pliku ");
        fileStatusLabel.setForeground(Color.RED);

        JLabel layoutLabel = new JLabel(" Układ: ");
        String[] layouts = {"circle", "random"};
        layoutCombo = new JComboBox<>(layouts);

        JLabel formatLabel = new JLabel(" Format pośredni: ");
        String[] formats = {"txt", "bin"};
        formatCombo = new JComboBox<>(formats);

        JButton btnRun = new JButton("Uruchom silnik w C i Generuj");
        btnRun.setBackground(new Color(40, 167, 69));
        btnRun.setForeground(Color.WHITE);

        JCheckBox chkLabels = new JCheckBox("Etykiety", true);
        JCheckBox chkWeights = new JCheckBox("Wagi i nazwy", true);

        JLabel zoomLabel = new JLabel(" Zoom: ");
        JSlider zoomSlider = new JSlider(10, 300, 100);

        btnSelectFile.addActionListener(e -> selectFile());
        btnRun.addActionListener(e -> processGraphInC());
        chkLabels.addActionListener(e -> view.setShowLabels(chkLabels.isSelected()));
        chkWeights.addActionListener(e -> view.setShowWeights(chkWeights.isSelected()));
        zoomSlider.addChangeListener(e -> view.setZoomFactor(zoomSlider.getValue() / 100.0));

        toolBar.add(btnSelectFile);
        toolBar.add(fileStatusLabel);
        toolBar.addSeparator();
        toolBar.add(layoutLabel);
        toolBar.add(layoutCombo);
        toolBar.add(formatLabel);
        toolBar.add(formatCombo);
        toolBar.addSeparator();
        toolBar.add(btnRun);
        toolBar.addSeparator();
        toolBar.add(chkLabels);
        toolBar.add(chkWeights);
        toolBar.addSeparator();
        toolBar.add(zoomLabel);
        toolBar.add(zoomSlider);

        topPanel.add(toolBar, BorderLayout.CENTER);
        return topPanel;
    }

    private void selectFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedInputFile = fc.getSelectedFile();
            fileStatusLabel.setText(" " + selectedInputFile.getName() + " ");
            fileStatusLabel.setForeground(new Color(0, 128, 0));
        }
    }

    private void processGraphInC() {
        if (selectedInputFile == null) {
            JOptionPane.showMessageDialog(this, "Najpierw wybierz plik wejściowy grafu!", "Brak pliku", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String layout = (String) layoutCombo.getSelectedItem();
        String format = (String) formatCombo.getSelectedItem();
        String outputFileName = "output_positions." + format;

        try {
            // WYBÓR PLIKU WYKONYWALNEGO (Dostosuj pod Windows/Linux)
            String executable = "./graph_viz";
            File exeFile = new File(executable);
            File exeFileWin = new File(executable + ".exe");
            
            if (!exeFile.exists() && exeFileWin.exists()) {
                executable = "./graph_viz.exe";
            } else if (!exeFile.exists() && !exeFileWin.exists()) {
                throw new Exception("Nie znaleziono pliku binarnego C ('graph_viz'). Uruchom 'make' w folderze projektu.");
            }

            // Uruchomienie C z argumentami: <input.txt> <output> <layout> <format>
            ProcessBuilder pb = new ProcessBuilder(
                    executable,
                    selectedInputFile.getAbsolutePath(),
                    outputFileName,
                    layout,
                    format
            );

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new Exception("Program w C zakończył się błędem. Kod: " + exitCode);
            }

            File outputFile = new File(outputFileName);
            List<GraphData.Node> nodes;
            
            if ("bin".equals(format)) {
                nodes = service.parseOutputBin(outputFile);
            } else {
                nodes = service.parseOutputTxt(outputFile);
            }

            List<GraphData.Edge> edges = service.parseInputEdges(selectedInputFile);
            view.updateGraph(nodes, edges);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd integracji procesów:\n" + ex.getMessage(), "Błąd wykonania", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initMouseListeners() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double scale = view.getZoomFactor();
                double mouseX = (e.getX() - view.getWidth() / 2.0) / scale;
                double mouseY = (e.getY() - view.getHeight() / 2.0) / scale;

                draggedNode = view.getNodes().stream()
                        .filter(n -> Math.hypot(n.x - mouseX, n.y - mouseY) <= 12.0)
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public void mouseReleased(MouseEvent e) { draggedNode = null; }
        });

        view.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    double scale = view.getZoomFactor();
                    draggedNode.x = (e.getX() - view.getWidth() / 2.0) / scale;
                    draggedNode.y = (e.getY() - view.getHeight() / 2.0) / scale;
                    view.repaint();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphPanel panel = new GraphPanel();
            FileParser parser = new FileParser();
            new GraphController(panel, parser).setVisible(true);
        });
    }
}
