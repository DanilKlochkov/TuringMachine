package org.turing;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static List<String> alphabet = new ArrayList<>();
    private static List<String> stateAlphabet = new ArrayList<>();
    private static List<Rule> rules = new ArrayList<>();
    private static Pair<Integer, String> currentState;

    public static void main(String[] args) throws IOException {
        List<String> tape = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Машина Тьюринга");
        System.out.println("Для начала работы введите алфавит машины\nВводите символы через запятую");
        while (alphabet.isEmpty()) {
            try {
                String sybmols = br.readLine();
                alphabet = Arrays.asList(sybmols.split(","));
            } catch (Exception e) {
                System.out.println("Ошибка, попробуйте снова!");
            }
        }
        System.out.println("Введите алфавит состояний автоматов\nБуквой z отметьте конечные состояний\nВводите символы через запятую");
        while (stateAlphabet.isEmpty()) {
            try {
                String sybmols = br.readLine();
                stateAlphabet = Arrays.asList(sybmols.split(","));
            } catch (Exception e) {
                System.out.println("Ошибка, попробуйте снова!");
            }
        }
        System.out.println("Введите правила для машины. Пример: q1 0 -> qz 1 R [R/L/E]\nДля того чтобы окончить ввод введите /q");
        while (true) {
            String line = br.readLine();
            if ("/q".equals(line)) {
                break;
            }
            try {
                String[] rule = line.split("->");
                String[] left = rule[0].trim().split(" ");
                if (!stateAlphabet.contains(left[0]) || !alphabet.contains(left[1])) {
                    throw new RuntimeException();
                }
                String[] right = rule[1].trim().split(" ");
                if (!stateAlphabet.contains(right[0]) || !alphabet.contains(right[1]) || !Arrays.asList("R", "L", "E").contains(right[2])) {
                    throw new RuntimeException();
                }

                rules.add(new Rule(new Rule.Key(left[0], left[1]), new Rule.Value(right[0], right[1], right[2])));
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте снова");
            }
        }
        System.out.println("Введите ленту через пробел");
        while (tape.isEmpty()) {
            String line = br.readLine();
            try {
                List<String> elements = Arrays.asList(line.split(" "));
                if (!new HashSet<>(alphabet).containsAll(elements)) {
                    throw new RuntimeException();
                }
                tape.addAll(elements);
            } catch (Exception e) {
                System.out.println("Не все элементы ленты присутствуют в исходном алфавите!\nПопробуйте снова");
            }
        }
        System.out.println("Введите индекс начального элемента на ленте и его состояние через пробел");
        while (Objects.isNull(currentState)) {
            String line = br.readLine();
            try {
                String[] tmp = line.split(" ");
                if (!stateAlphabet.contains(tmp[1])) {
                    throw new RuntimeException();
                }
                currentState = new Pair<>(Integer.parseInt(tmp[0]), tmp[1]);
            } catch (NumberFormatException e) {
                System.out.println("Индекс должен быть числом!");
            } catch (RuntimeException e) {
                System.out.println("Такого состояния не представлена в вашем алфавите состояний!");
            }
        }
        while (true) {
            String curr = tape.get(currentState.getKey());
            if ("$".equals(curr)) {
                System.out.println("Конец ленты!");
                break;
            }
            String rule = currentState.getValue();
            System.out.println(rule + " : " + curr);
            List<Rule> turingRules = rules.stream().filter(x -> x.getKey().getState().equals(rule) && x.getKey().getSymbol().equals(curr)).collect(Collectors.toList());
            Rule turingRule = turingRules.get(0);
            tape.set(currentState.getKey(), turingRule.getValue().getNewValue());
            System.out.println(String.join(" ", tape));
            if ("R".equals(turingRule.getValue().getDirection())) {
                if (currentState.getKey() == tape.size() - 1)
                    tape.add("$");
                currentState = new Pair<>(currentState.getKey() + 1, turingRule.getValue().getNewState());
            } else if ("L".equals(turingRule.getValue().getDirection())) {
                if (currentState.getKey() == 0) {
                    tape.add(0, "$");
                    currentState = new Pair<>(0, turingRule.getValue().getNewState());
                } else {
                    currentState = new Pair<>(currentState.getKey() - 1, turingRule.getValue().getNewState());
                }
            } else {
                currentState = new Pair<>(currentState.getKey(), turingRule.getValue().getNewState());
            }
            System.out.println("New state: " + currentState.getKey() + " : " + currentState.getValue());
            System.out.println();
            if (currentState.getValue().contains("z")) {
                System.out.println("Конец программы");
                break;
            }
        }
        System.out.println(String.join(" ", tape));
    }
}