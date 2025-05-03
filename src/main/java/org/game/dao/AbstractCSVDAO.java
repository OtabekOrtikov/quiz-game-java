package org.game.dao;

import org.game.parser.Parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCSVDAO<T> implements GenericDAO<T> {
    protected final Path filePath;
    protected final Parser<T> parser;
    protected final String headerLine;

    public AbstractCSVDAO(String fileName, Parser<T> parser, String headerLine) {
        this.filePath = Paths.get(fileName);
        this.parser = parser;
        this.headerLine = headerLine;

        if (Files.notExists(filePath)) {
            try {
                Files.write(filePath,
                        Collections.singletonList(headerLine),
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public List<T> findAll() throws IOException {
        List<T> list = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        for (int i = 1; i < lines.size(); i++) {
            list.add(parser.parse(lines.get(i)));
        }
        return list;
    }

    @Override
    public void save(T object) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.newLine();
            writer.write(parser.toLine(object));
        }
    }

    @Override
    public void saveAll(List<T> objects) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write(headerLine);
            for (T obj : objects) {
                writer.newLine();
                writer.write(parser.toLine(obj));
            }
        }
    }
}
