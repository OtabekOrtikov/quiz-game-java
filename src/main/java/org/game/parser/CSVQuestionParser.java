package org.game.parser;

import org.game.model.Question;

import java.util.*;

public class CSVQuestionParser implements Parser<Question> {
    private static final String DELIM = ",";

    @Override
    public Question parse(String line) {
        String[] parts = line.split(DELIM);
        String text = parts[0];
        List<String> opts = Arrays.asList(parts[1], parts[2], parts[3]);
        int correctIdx = Integer.parseInt(parts[4]);
        return new Question(text, opts, correctIdx);
    }

    @Override
    public String toLine(Question q) {
        String t = q.getText().replace(",", " ");
        String o1 = q.getOptions().get(0).replace(",", " ");
        String o2 = q.getOptions().get(1).replace(",", " ");
        String o3 = q.getOptions().get(2).replace(",", " ");
        return String.join(DELIM, t, o1, o2, o3, String.valueOf(q.getCorrectIndex()));
    }
}
