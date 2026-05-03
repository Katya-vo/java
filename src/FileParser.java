package service;

import model.GraphData;
import java.io.*;
import java.util.*;

public class FileParser {
    public static class GraphContainer {
        public List<GraphData.Node> nodes = new ArrayList<>();
        public List<GraphData.Edge> edges = new ArrayList<>();
    }

    public GraphContainer parse(File file) throws Exception {
        GraphContainer container = new GraphContainer();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\s+");
                if (p.length < 2) continue;
                if (p[0].equalsIgnoreCase("NODE")) 
                    container.nodes.add(new GraphData.Node(Integer.parseInt(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3])));
                else if (p[0].equalsIgnoreCase("EDGE"))
                    container.edges.add(new GraphData.Edge(Integer.parseInt(p[1]), Integer.parseInt(p[2])));
            }
        }
        return container;
    }
}
