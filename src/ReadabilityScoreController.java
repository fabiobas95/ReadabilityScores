import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadabilityScoreController {
    private static Scanner sc = new Scanner(System.in);
    private TextStats stats;
    private String fileName;

    public ReadabilityScoreController(String fileName) {
        this.fileName = fileName;
    }

    public void mainLoop() {
        try {
            this.stats = computeTextStats(fileName);
            stats.printStats();
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String scoreType = sc.nextLine();
            executeOption(scoreType);
        } catch (FileNotFoundException e) {
            System.out.println("No such file " + fileName);
        }
    }

    private void executeOption(String scoreType) {
        Score score = null;
        try {
            score = Score.valueOf(scoreType.toUpperCase());

            switch (score) {

                case ARI:
                    ariScore();
                    break;
                case FK:
                    fkScore();
                    break;
                case SMOG:
                    smogScore();
                    break;
                case CL:
                    clScore();
                    break;
                case ALL:
                    System.out.println();
                    ariScore();
                    fkScore();
                    smogScore();
                    clScore();
                    System.out.println();
                    System.out.printf("This text should be understood in average by %.2f year olds.", stats.averageAge());
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Unexpected value: " + scoreType);
        }
    }

    private void clScore() {
        double score = stats.computeClScore();
        System.out.printf("Coleman–Liau index: %.2f (about %d year olds).\n"
                , score, stats.getUnderstandingAge(score));
    }

    private void smogScore() {
        double score = stats.computeSmogScore();
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).\n"
                , score, stats.getUnderstandingAge(score));
    }

    private void fkScore() {
        double score = stats.computeFkScore();
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).\n"
                , score, stats.getUnderstandingAge(score));
    }

    private void ariScore() {
        double score = stats.computeAriScore();
        System.out.printf("Automated Readability Index: %.2f (about %d year olds).\n"
                , score, stats.getUnderstandingAge(score));
    }


    private TextStats computeTextStats(String fileName) throws FileNotFoundException {
        String text = readFile(fileName);
        int characters = text.replaceAll(" ", "").length();
        int words = 0;
        int syllables = 0;
        int polySyllables = 0;
        int sentencesNo = 0;
        String[] sentences = text.split("[\\.?!]");
        for (String sentence : sentences) {
            sentence = sentence.strip();
            words += sentence.split(" ").length;
            for (String word : sentence.split(" ")) {
                int wordSyllables = countSyllables(word.replaceAll("e$", ""));
                if (wordSyllables >= 3) {
                    polySyllables++;
                }
                syllables += wordSyllables;
            }

        }
        return new TextStats(text, sentences.length, words, characters, syllables, polySyllables);
    }

    private static String readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        String text = "";
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) {
                String line = scanner.nextLine().replaceAll("[\\'\"]", "");
                sb.append(line.replaceAll("[\\.]{2,}", ""));
                sb.append("\n");
            }
            text = sb.toString();
            System.out.println("File: " + fileName);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        return text;
    }

    private static int countSyllables(String text) {
        int syllables = 0;
        boolean prevIsVowel = false;
        if (text.toLowerCase().matches("[aeiouy]{1,3}")) {
            return 1;
        }
        for (int i = 0; i < text.length(); i++) {
            if (isVowel(text.charAt(i)) && !prevIsVowel) {
                syllables++;
                prevIsVowel = isVowel(text.charAt(i));
            } else if (prevIsVowel) {
                prevIsVowel = false;
            }
        }
        if (text.matches(".*[^aiouy]e")) {
            syllables--;
        }
        return syllables < 1 ? 1 : syllables;
    }

    private static boolean isVowel(char ch) {
        ch = Character.toLowerCase(ch);
        return (ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u' || ch == 'y');
    }

    private enum Score {
        ARI, FK, SMOG, CL, ALL
    }
}
