package service;

import model.GraphData;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public List<GraphData.Node> parseOutputTxt(File file) throws Exception {
        List<GraphData.Node> nodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) return nodes;
            
            line = line.trim();
            // pomijanie naglowka z C
            if (!line.contains(" ") && !line.contains("\t")) {
                
            } else {
                parseNodeLine(line, nodes);
            }

            while ((line = br.readLine()) != null) {
                parseNodeLine(line, nodes);
            }
        }
        return nodes;
    }

    private void parseNodeLine(String line, List<GraphData.Node> nodes) {
        String[] p = line.trim().split("\\s+");
        if (p.length >= 3) {
            int id = Integer.parseInt(p[0]);
            double x = Double.parseDouble(p[1]);
            double y = Double.parseDouble(p[2]);
            nodes.add(new GraphData.Node(id, x, y));
        }
    }

    public List<GraphData.Node> parseOutputBin(File file) throws Exception {
        List<GraphData.Node> nodes = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            if (dis.available() == 0) return nodes;

            byte[] intBuffer = new byte[4];
            dis.readFully(intBuffer);
            int nodeCount = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).getInt();

            byte[] nodeBuffer = new byte[20]; // 4B int + 8B double + 8B double
            for (int i = 0; i < nodeCount; i++) {
                dis.readFully(nodeBuffer);
                ByteBuffer bb = ByteBuffer.wrap(nodeBuffer).order(ByteOrder.LITTLE_ENDIAN);
                int id = bb.getInt();
                double x = bb.getDouble();
                double y = bb.getDouble();
                nodes.add(new GraphData.Node(id, x, y));
            }
        }
        return nodes;
    }

    public List<GraphData.Edge> parseInputEdges(File file) throws Exception {
        List<GraphData.Edge> edges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\s+");
                if (p.length >= 4) {
                    String label = p[0];
                    int src = Integer.parseInt(p[1]);
                    int tgt = Integer.parseInt(p[2]);
                    double weight = Double.parseDouble(p[3]);
                    edges.add(new GraphData.Edge(label, src, tgt, weight));
                }
            }
        }
        return edges;
    }
}
