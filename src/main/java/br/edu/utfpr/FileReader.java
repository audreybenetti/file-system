package br.edu.utfpr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {

    public static void read(Path path) throws IOException {
        if (path.toString().endsWith(".txt") || path.toString().endsWith(".TXT")) {
            Files.readAllLines(path)
                    .forEach(System.out::println);
        } else throw new UnsupportedOperationException("Apenas arquivos .txt podem ser lidos.");

    }
}
