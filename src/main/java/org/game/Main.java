package org.game;

import org.game.controller.Controller;
import org.game.controller.QuizController;
import org.game.dao.CSVQuestionDAO;
import org.game.dao.QuestionDAO;
import org.game.service.QuizService;
import org.game.service.QuizServiceImpl;

public class Main {
    public static void main(String[] args) {
        try {
            QuestionDAO dao = new CSVQuestionDAO("questions.csv");
            QuizService service = new QuizServiceImpl(dao);
            Controller ctl = new QuizController(service);
            ctl.run();
        } catch (Exception e) {
            System.err.println("Fatal: " + e.getMessage());
        }
    }
}
