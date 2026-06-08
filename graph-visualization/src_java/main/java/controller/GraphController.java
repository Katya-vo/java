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
    // tworzenie paska menu
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
            List<GraphData.Edge> edges = service.parseInputEdges(selectedInputFile);
            
            // pobieramy unikalne ID wierzchołków
            java.util.List<Integer> uniqueNodes = edges.stream()
                    .flatMapToInt(e -> java.util.stream.IntStream.of(e.sourceId, e.targetId))
                    .distinct()
                    .boxed()
                    .toList();
            
            int vCount = uniqueNodes.size();
            int eCount = edges.size();
            boolean isPlanar = true;
            String reason = "";

            // sprawdzanie czy graf jest planarny 
            if (vCount >= 3 && eCount > (3 * vCount - 6)) {
                isPlanar = false;
                reason = "Liczba krawędzi (E=" + eCount + ") przekracza limit dla " + vCount + " wierzchołków (3V-6=" + (3*vCount-6) + ").";
            }

            
            if (isPlanar && vCount >= 3) {
                boolean hasTriangle = false;
                
                
                for (int i = 0; i < uniqueNodes.size(); i++) {
                    for (int j = i + 1; j < uniqueNodes.size(); j++) {
                        for (int k = j + 1; k < uniqueNodes.size(); k++) {
                            int n1 = uniqueNodes.get(i);
                            int n2 = uniqueNodes.get(j);
                            int n3 = uniqueNodes.get(k);

                            
                            if (hasEdge(edges, n1, n2) && hasEdge(edges, n2, n3) && hasEdge(edges, n3, n1)) {
                                hasTriangle = true;
                                break;
                            }
                        }
                        if (hasTriangle) break;
                    }
                    if (hasTriangle) break;
                }

                
                if (!hasTriangle && eCount > (2 * vCount - 4)) {
                    isPlanar = false;
                    reason = "Wykryto graf beztrójkątowy (np. dwudzielny K_3,3). Liczba krawędzi (E=" + eCount + ") przekracza limit 2V-4=" + (2*vCount-4) + ".";
                }
            }

            
            if (isPlanar && vCount >= 5) {
                for (int i = 0; i < uniqueNodes.size(); i++) {
                    for (int j = i+1; j < uniqueNodes.size(); j++) {
                        for (int k = j+1; k < uniqueNodes.size(); k++) {
                            for (int l = k+1; l < uniqueNodes.size(); l++) {
                                for (int m = l+1; m < uniqueNodes.size(); m++) {
                                    int n1 = uniqueNodes.get(i), n2 = uniqueNodes.get(j), n3 = uniqueNodes.get(k), n4 = uniqueNodes.get(l), n5 = uniqueNodes.get(m);
                                    
                                    if (hasEdge(edges, n1, n2) && hasEdge(edges, n1, n3) && hasEdge(edges, n1, n4) && hasEdge(edges, n1, n5) &&
                                        hasEdge(edges, n2, n3) && hasEdge(edges, n2, n4) && hasEdge(edges, n2, n5) &&
                                        hasEdge(edges, n3, n4) && hasEdge(edges, n3, n5) &&
                                        hasEdge(edges, n4, n5)) {
                                        isPlanar = false;
                                        reason = "Wykryto ukrytą strukturę Kuratowskiego K_5 (podgraf pełny 5 wierzchołków).";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // blokada jak bedzie wykryta nieplanarność
            if (!isPlanar) {
                JOptionPane.showMessageDialog(this, 
                    "Błąd walidacji: Wczytany graf jest NIEPLANARNY!\n" + reason + "\nPrzetwarzanie przerwane.", 
                    "Wykryto brak planarności", 
                    JOptionPane.ERROR_MESSAGE);
                return; 
            }

            // uruchamiamy C
            String executable = "./graph_viz";
            File exeFile = new File(executable);
            File exeFileWin = new File(executable + ".exe");
            if (!exeFile.exists() && exeFileWin.exists()) executable = "./graph_viz.exe";
            else if (!exeFile.exists() && !exeFileWin.exists()) throw new Exception("Nie znaleziono pliku binarnego C.");

            ProcessBuilder pb = new ProcessBuilder(executable, selectedInputFile.getAbsolutePath(), outputFileName, layout, format);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) throw new Exception("Program w C zakończył się błędem.");

            File outputFile = new File(outputFileName);
            List<GraphData.Node> nodes = "bin".equals(format) ? service.parseOutputBin(outputFile) : service.parseOutputTxt(outputFile);
            view.updateGraph(nodes, edges);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd integracji procesów:\n" + ex.getMessage(), "Błąd wykonania", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasEdge(List<GraphData.Edge> edges, int u, int v) {
        return edges.stream().anyMatch(e -> (e.sourceId == u && e.targetId == v) || (e.sourceId == v && e.targetId == u));
    }
        // przesuwanie wierzchołków myszą
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
    // funkcja uruchomieniowa
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphPanel panel = new GraphPanel();
            FileParser parser = new FileParser();
            new GraphController(panel, parser).setVisible(true);
        });
    }
}
