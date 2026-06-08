package service;

import model.GraphData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileParserTest {

    private FileParser parser;

    @BeforeEach
    public void setUp() {
        parser = new FileParser();
    }

    @Test
    public void testParseInputEdgesValid(@TempDir Path tempDir) throws Exception {
        File txtFile = tempDir.resolve("input.txt").toFile();
        try (FileOutputStream fos = new FileOutputStream(txtFile)) {
            fos.write("E8_9 8 9 1.446\nE7_10 7 10 9.968\n".getBytes());
        }

        List<GraphData.Edge> edges = parser.parseInputEdges(txtFile);
        assertEquals(2, edges.size());
        assertEquals("E8_9", edges.get(0).label);
        assertEquals(8, edges.get(0).sourceId);
        assertEquals(9, edges.get(0).targetId);
        assertEquals(1.446, edges.get(0).weight);
    }

    @Test
    public void testParseOutputTxtWithHeader(@TempDir Path tempDir) throws Exception {
        File txtFile = tempDir.resolve("output.txt").toFile();
        try (FileOutputStream fos = new FileOutputStream(txtFile)) {
            fos.write("2\n1 10.50 -20.30\n2 45.00 90.15\n".getBytes());
        }

        List<GraphData.Node> nodes = parser.parseOutputTxt(txtFile);
        assertEquals(2, nodes.size());
        assertEquals(1, nodes.get(0).id);
        assertEquals(10.50, nodes.get(0).x);
        assertEquals(-20.30, nodes.get(0).y);
    }

    @Test
    public void testParseOutputBinLittleEndian(@TempDir Path tempDir) throws Exception {
        File binFile = tempDir.resolve("output.bin").toFile();

        int totalSize = 4 + 20; // 4B nagłówek + 20B jeden wierzchołek
        ByteBuffer buffer = ByteBuffer.allocate(totalSize).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(1);      // nodeCount = 1
        buffer.putInt(3);      // id = 3
        buffer.putDouble(5.5); // x = 5.5
        buffer.putDouble(-6.5);// y = -6.5

        try (FileOutputStream fos = new FileOutputStream(binFile)) {
            fos.write(buffer.array());
        }

        List<GraphData.Node> nodes = parser.parseOutputBin(binFile);
        assertEquals(1, nodes.size());
        assertEquals(3, nodes.get(0).id);
        assertEquals(5.5, nodes.get(0).x);
        assertEquals(-6.5, nodes.get(0).y);
    }
}
