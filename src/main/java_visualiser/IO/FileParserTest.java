package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    private FileParser parser;

    @BeforeEach
    void setUp() {
        parser = new FileParser();
    }

    @Test
    void shouldReturnTrueForCorrectTextGraphParsing(@TempDir Path tempDir) throws Exception {
        File tempFile = tempDir.resolve("correct_graph.txt").toFile();
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write("NODE 0 150.0 200.0\n");
            fw.write("NODE 1 300.0 100.0\n");
            fw.write("EDGE 0 1\n");
        }

        FileParser.GraphContainer result = parser.parse(tempFile);

        assertNotNull(result, "Kontener wyniku nie powinien być wartością null");
        assertEquals(2, result.nodes.size(), "Parser powinien wykryć dokładnie 2 węzły");
        assertEquals(1, result.edges.size(), "Parser powinien wykryć dokładnie 1 krawędź");
        
        assertEquals(0, result.nodes.get(0).id);
        assertEquals(150.0, result.nodes.get(0).x);
        assertEquals(200.0, result.nodes.get(0).y);
        
        assertEquals(0, result.edges.get(0).sourceId);
        assertEquals(1, result.edges.get(0).targetId);
    }

    @Test
    void shouldThrowExceptionWhenParsingInvalidDataType(@TempDir Path tempDir) {
        File badFile = tempDir.resolve("corrupted_graph.txt").toFile();
        try (FileWriter fw = new FileWriter(badFile)) {
            fw.write("NODE 0 tekst_zamiast_liczby 200.0\n");
        } catch (Exception ignored) {}
        assertThrows(NumberFormatException.class, () -> {
            parser.parse(badFile);
        }, "Parser powinien rzucić NumberFormatException w przypadku wykrycia tekstu zamiast wartości liczbowej");
    }

    @Test
    void shouldReturnEmptyContainerWhenFileIsEmpty(@TempDir Path tempDir) throws Exception {
        File emptyFile = tempDir.resolve("empty_graph.txt").toFile();
        try (FileWriter fw = new FileWriter(emptyFile)) {
            fw.write("");
        }
        FileParser.GraphContainer result = parser.parse(emptyFile);
        assertNotNull(result);
        assertTrue(result.nodes.isEmpty(), "Lista węzłów powinna być pusta dla pustego pliku");
        assertTrue(result.edges.isEmpty(), "Lista krawędzi powinna być pusta dla pustego pliku");
    }
}
