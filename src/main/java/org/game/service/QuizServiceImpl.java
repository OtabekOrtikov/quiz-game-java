package org.game.service;

import org.game.dao.QuestionDAO;
import org.game.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class QuizServiceImpl implements QuizService {
    private final QuestionDAO dao;
    private List<Question> questions;
    private final Scanner scan = new Scanner(System.in);

    public QuizServiceImpl(QuestionDAO dao) throws IOException {
        this.dao = dao;
        this.questions = dao.findAll();
    }

    @Override
    public List<Question> getAllQuestions() {
        return questions;
    }

    @Override
    public void addQuestion(Question q) throws IOException {
        dao.save(q);
        questions.add(q);
    }

    @Override
    public int conductQuiz() {
        List<Question> quizList = new ArrayList<>(questions);
        Collections.shuffle(quizList);

        int score = 0;
        for (Question q : quizList) {
            System.out.println("\n" + q.getText());
            List<String> opts = q.getOptions();
            for (int i = 0; i < opts.size(); i++) {
                System.out.printf("  %d) %s%n", i + 1, opts.get(i));
            }

            int choice;
            while (true) {
                System.out.print("Ваш ответ (1–" + opts.size() + "): ");
                String line = scan.nextLine();
                try {
                    choice = Integer.parseInt(line);
                    if (choice < 1 || choice > opts.size()) {
                        throw new NumberFormatException();
                    }
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Пожалуйста, введите число от 1 до " + opts.size() + ".");
                }
            }

            if (q.isCorrect(choice)) {
                System.out.println("✔ Правильно!");
                score++;
            } else {
                System.out.println("✘ Неправильно. Правильный ответ: " +
                        opts.get(q.getCorrectIndex()));
            }
        }

        return score;
    }

    @Override
    public void updateQuestion(int index, Question updated) throws IOException {
        questions.set(index, updated);
        dao.saveAll(questions);
    }
}
