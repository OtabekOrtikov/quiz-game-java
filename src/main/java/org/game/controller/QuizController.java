package org.game.controller;

import org.game.model.Question;
import org.game.service.QuizService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizController implements Controller {
    private final QuizService service;
    private final Scanner scan = new Scanner(System.in);

    public QuizController(QuizService service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("\n=== Викторина ===");
            System.out.println("1) Добавить вопрос");
            System.out.println("2) Посмотреть вопросы");
            System.out.println("3) Начать игру");
            System.out.println("4) Изменить вопрос");
            System.out.println("5) Выход");
            System.out.print("Выбор: ");

            String cmd = scan.nextLine();
            try {
                switch (cmd) {
                    case "1": addQuestion();    break;
                    case "2": listQuestions();  break;
                    case "3": playQuiz();       break;
                    case "4": editQuestion();   break;
                    case "5": return;
                    default:  System.out.println("Неверная команда.");
                }
            } catch (IOException ioe) {
                System.err.println("I/O ошибка: " + ioe.getMessage());
            } catch (Exception ex) {
                System.err.println("Ошибка: " + ex.getMessage());
            }
        }
    }

    private void addQuestion() throws IOException {
        System.out.print("Текст вопроса: ");
        String text = scan.nextLine();

        List<String> opts = new java.util.ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            System.out.printf("Вариант %d: ", i);
            opts.add(scan.nextLine());
        }

        int idx;
        while (true) {
            System.out.print("Номер правильного (1–3): ");
            String line = scan.nextLine();
            try {
                idx = Integer.parseInt(line) - 1;
                if (idx < 0 || idx >= opts.size()) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректное число от 1 до 3.");
            }
        }

        service.addQuestion(new Question(text, opts, idx));
        System.out.println("Вопрос сохранён.");
    }

    private void listQuestions() {
        List<Question> all = service.getAllQuestions();
        if (all.isEmpty()) {
            System.out.println("Вопросы ещё не добавлены.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                System.out.printf("%d) %s%n", i + 1, all.get(i).getText());
            }
        }
    }

    private void playQuiz() {
        int score = service.conductQuiz();
        int total = service.getAllQuestions().size();
        System.out.printf("%nИгра окончена. Ваш счёт: %d из %d%n", score, total);
    }

    private void editQuestion() throws IOException {
        List<Question> all = service.getAllQuestions();
        if (all.isEmpty()) {
            System.out.println("Вопросов нет.");
            return;
        }

        // выводим список
        for (int i = 0; i < all.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, all.get(i).getText());
        }

        int idx;
        while (true) {
            System.out.print("Номер вопроса для редактирования: ");
            String line = scan.nextLine();
            try {
                idx = Integer.parseInt(line) - 1;
                if (idx < 0 || idx >= all.size()) throw new NumberFormatException();
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректный номер от 1 до " + all.size());
            }
        }

        // запрашиваем новые данные
        System.out.print("Новый текст (Enter — без изменений): ");
        String newText = scan.nextLine().trim();
        if (newText.isEmpty()) newText = all.get(idx).getText();

        List<String> newOpts = new ArrayList<>();
        List<String> oldOpts = all.get(idx).getOptions();
        for (int i = 0; i < oldOpts.size(); i++) {
            System.out.printf("Новый вариант %d (Enter — \"%s\"): ", i + 1, oldOpts.get(i));
            String opt = scan.nextLine().trim();
            newOpts.add(opt.isEmpty() ? oldOpts.get(i) : opt);
        }

        int newCorrect;
        while (true) {
            System.out.printf("Номер правильного (1–%d, Enter — %d): ",
                    newOpts.size(),
                    all.get(idx).getCorrectIndex() + 1);
            String line = scan.nextLine().trim();
            if (line.isEmpty()) {
                newCorrect = all.get(idx).getCorrectIndex();
                break;
            }
            try {
                int c = Integer.parseInt(line) - 1;
                if (c < 0 || c >= newOpts.size()) throw new NumberFormatException();
                newCorrect = c;
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Введите число от 1 до " + newOpts.size());
            }
        }

        // создаём обновлённый объект
        Question updated = new Question(newText, newOpts, newCorrect);
        service.updateQuestion(idx, updated);
        System.out.println("Вопрос обновлён.");
    }
}
