package org.game.service;

import org.game.model.Question;

import java.io.IOException;
import java.util.List;

public interface QuizService {
    List<Question> getAllQuestions();
    void addQuestion(Question q) throws IOException;
    int conductQuiz();
    void updateQuestion(int index, Question updated) throws IOException;
}
