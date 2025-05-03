package org.game.parser;

public interface Parser<T> {
    T parse(String line);

    String toLine(T object);
}
