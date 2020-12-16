package com.javamentor;


import java.util.*;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        // Random variables
        String randomFrom = "..."; // Некоторая случайная строка. Можете выбрать ее самостоятельно.
        String randomTo = "...";  // Некоторая случайная строка. Можете выбрать ее самостоятельно.
        int randomSalary = 100;  // Некоторое случайное целое положительное число. Можете выбрать его самостоятельно.

// Создание списка из трех почтовых сообщений.
        MailMessage<String> firstMessage = new MailMessage<>(
                "Robert Howard",
                "H.P. Lovecraft",
                "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
        );

        assert firstMessage.getFrom().equals("Robert Howard") : "Wrong firstMessage from address";
        assert firstMessage.getTo().equals("H.P. Lovecraft") : "Wrong firstMessage to address";
        assert firstMessage.getContent().endsWith("Howard!") : "Wrong firstMessage content ending";

        MailMessage<String> secondMessage = new MailMessage<>(
                "Jonathan Nolan",
                "Christopher Nolan",
                "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!"
        );

        MailMessage<String> thirdMessage = new MailMessage<>(
                "Stephen Hawking",
                "Christopher Nolan",
                "Я так и не понял Интерстеллар."
        );

        List<MailMessage<String>> messages = Arrays.asList(
                firstMessage, secondMessage, thirdMessage
        );

// Создание почтового сервиса.
        MailService<String> mailService = new MailService<>();

// Обработка списка писем почтовым сервисом
        messages.stream().forEachOrdered(mailService); // todo здесь раньше тоже подсвечивало, а теперь работает:) от этого только страннее. было как во втором туду

// Получение и проверка словаря "почтового ящика",
//   где по получателю можно получить список сообщений, которые были ему отправлены
        Map<String, List<String>> mailBox = mailService.getMailBox();

        assert mailBox.get("H.P. Lovecraft").equals(
                Arrays.asList(
                        "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
                )
        ) : "wrong mailService mailbox content (1)";

        System.out.println(mailBox.get("H.P. Lovecraft").equals(
                Arrays.asList(
                        "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
                )
        ));

        assert mailBox.get("Christopher Nolan").equals(
                Arrays.asList(
                        "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!",
                        "Я так и не понял Интерстеллар."
                )
        ) : "wrong mailService mailbox content (2)";

        System.out.println(mailBox.get("Christopher Nolan").equals(
                Arrays.asList(
                        "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!",
                        "Я так и не понял Интерстеллар."
                )
        ));

        assert mailBox.get(randomTo).equals(Collections.<String>emptyList()) : "wrong mailService mailbox content (3)";
        System.out.println("Strange test 1: " + mailBox.get(randomTo));
        System.out.println("Strange test 2: " + mailBox.get(randomTo).equals(Collections.<String>emptyList()));

// Создание списка из трех зарплат.
        Salary salary1 = new Salary("Facebook", "Mark Zuckerberg", 1);
        Salary salary2 = new Salary("FC Barcelona", "Lionel Messi", Integer.MAX_VALUE);
        Salary salary3 = new Salary(randomFrom, randomTo, randomSalary);

// Создание почтового сервиса, обрабатывающего зарплаты.
        MailService<Integer> salaryService = new MailService<>();

// Обработка списка зарплат почтовым сервисом
        Arrays.asList(salary1, salary2, salary3).forEach(salaryService::accept);
//        Arrays.asList(salary1, salary2, salary3).forEach(salaryService); //todo так как в этой строке НЕ работает, а так как на строчку выше -- работает

// Получение и проверка словаря "почтового ящика",
//   где по получателю можно получить список зарплат, которые были ему отправлены.
        Map<String, List<Integer>> salaries = salaryService.getMailBox();
        assert salaries.get(salary1.getTo()).equals(Arrays.asList(1)) : "wrong salaries mailbox content (1)";
        System.out.println(salaries.get(salary1.getTo()).equals(Arrays.asList(1)));
        assert salaries.get(salary2.getTo()).equals(Arrays.asList(Integer.MAX_VALUE)) : "wrong salaries mailbox content (2)";
        System.out.println(salaries.get(salary2.getTo()).equals(Arrays.asList(Integer.MAX_VALUE)));
        assert salaries.get(randomTo).equals(Arrays.asList(randomSalary)) : "wrong salaries mailbox content (3)";
        System.out.println(salaries.get(randomTo).equals(Arrays.asList(randomSalary)));
    }


    public static class MailMessage<T extends String> extends Sendable<T> {
        public MailMessage(String from, String to, T content) {
            super(from, to, content);
        }
    }

    public static class Salary<T extends Number> extends Sendable<T> {
        public Salary(String from, String to, T content) {
            super(from, to, content);
        }
    }

    public static class MailService<T> implements Consumer<Sendable<T>> {
        private Map<String, List<T>> mailBox = new HashMap<>(){
            @Override
            public List<T> get(Object key) {
                if (super.get(key) == null) {
                    return new ArrayList<>();
                }
                return super.get(key);
            }
        };

        public Map<String, List<T>> getMailBox() {
            return mailBox;
        }

        @Override
        public void accept(Sendable<T> sendable) {
            List<T> list = mailBox.get(sendable.getTo());
            list.add(sendable.getContent());
            mailBox.put(sendable.getTo(), list);
        }

    }

    public static abstract class Sendable<T> {
        private String from;
        private String to;
        private T content;

        public Sendable(String from, String to, T content) {
            this.from = from;
            this.to = to;
            this.content = content;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public T getContent() {
            return content;
        }
    }

}
