package service;

import model.GraphData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

interface GraphParser {
    FileParser.GraphContainer parse(File file) throws Exception;
}

class TextGraphParser implements GraphParser {
    @Override
    public FileParser.GraphContainer parse(File file) throws Exception {
        FileParser.GraphContainer container = new FileParser.GraphContainer();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\s+");
                if (p.length < 2) continue;
                if (p[0].equalsIgnoreCase("NODE")) {
                    container.nodes.add(new GraphData.Node(Integer.parseInt(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3])));
                } else if (p[0].equalsIgnoreCase("EDGE")) {
                    container.edges.add(new GraphData.Edge(Integer.parseInt(p[1]), Integer.parseInt(p[2])));
                }
            }
        }
        return container;
    }
}

class BinaryGraphParser implements GraphParser {
    @Override
    public FileParser.GraphContainer parse(File file) throws Exception {
        FileParser.GraphContainer container = new FileParser.GraphContainer();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            if (dis.available() == 0) return container;

            int nodeCount = Integer.reverseBytes(dis.readInt());
            int edgeCount = Integer.reverseBytes(dis.readInt());

            for (int i = 0; i < nodeCount; i++) {
                int id = Integer.reverseBytes(dis.readInt());
                double x = Double.longBitsToDouble(Long.reverseBytes(dis.readLong()));
                double y = Double.longBitsToDouble(Long.reverseBytes(dis.readLong()));
                container.nodes.add(new GraphData.Node(id, x, y));
            }

            for (int i = 0; i < edgeCount; i++) {
                int s = Integer.reverseBytes(dis.readInt());
                int t = Integer.reverseBytes(dis.readInt());
                dis.readLong(); 
                container.edges.add(new GraphData.Edge(s, t));
            }
        }
        return container;
    }
}

public class FileParser {
    public static class GraphContainer {
        public List<GraphData.Node> nodes = new ArrayList<>();
        public List<GraphData.Edge> edges = new ArrayList<>();
    }

    public GraphContainer parse(File file) throws Exception {
        GraphParser strategy;
        if (file.getName().endsWith(".bin")) {
            strategy = new BinaryGraphParser();
        } else {
            strategy = new TextGraphParser();
        }
        return strategy.parse(file);
    }
}
