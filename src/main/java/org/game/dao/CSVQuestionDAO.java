package org.game.dao;

import org.game.model.Question;
import org.game.parser.CSVQuestionParser;

import java.io.*;
import java.nio.file.*;

public class CSVQuestionDAO extends AbstractCSVDAO<Question> implements QuestionDAO {
    private static final String HEADER = "text,option1,option2,option3,correctIndex";

    public CSVQuestionDAO(String fileName) {
        super(initExternalFile(fileName), new CSVQuestionParser(), HEADER);
    }

    private static String initExternalFile(String externalFileName) {
        Path path = Paths.get(externalFileName);

        if (Files.notExists(path)) {
            try (InputStream in = CSVQuestionDAO.class
                    .getClassLoader()
                    .getResourceAsStream("questions.csv")) {

                if (in != null) {
                    Files.copy(in, path);
                    return externalFileName;
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Не удалось скопировать questions.csv из ресурсов", e);
            }
        }
        return externalFileName;
    }
}
