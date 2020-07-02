public class TextStats {
    private String text;
    private int sentences;
    private int words;
    private int characters;
    private int syllables;
    private int polySyllables;

    public TextStats(String text, int sentences, int words, int characters, int syllables, int polySyllables) {
        this.text = text;
        this.sentences = sentences;
        this.words = words;
        this.characters = characters;
        this.syllables = syllables;
        this.polySyllables = polySyllables;
    }

    public double computeAriScore() {
        return 4.71 * ((double) characters / words) + 0.5 * ((double) words / sentences) - 21.43;
    }

    public double computeFkScore() {
        return 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
    }

    public double computeSmogScore() {
        return 1.043 * Math.sqrt(polySyllables * (30.0 / sentences)) + 3.1291;
    }

    public double computeClScore() {
        double l = characters / (words / 100.0);
        double s = sentences / (words / 100.0);
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    public void printStats() {
        System.out.println();
        System.out.printf("Words: %d\n", words);
        System.out.printf("Sentences: %d\n", sentences);
        System.out.printf("Characters: %d\n", characters);
        System.out.printf("Syllables: %d\n", syllables);
        System.out.printf("Polysyllables: %d\n", polySyllables);
    }

    public double averageAge() {
        int age = 0;
        age += getUnderstandingAge(computeAriScore());
        age += getUnderstandingAge(computeFkScore());
        age += getUnderstandingAge(computeSmogScore());
        age += getUnderstandingAge(computeClScore());
        return age / 4.0;
    }

    public int getUnderstandingAge(double score) {
        switch ((int) Math.round(score)) {
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 9;
            case 4:
                return 10;
            case 5:
                return 11;
            case 6:
                return 12;
            case 7:
                return 13;
            case 8:
                return 14;
            case 9:
                return 15;
            case 10:
                return 16;
            case 11:
                return 17;
            case 12:
                return 18;
            case 13:
                return 24;
            case 14:
                return 25;
            default:
                return 0;
        }
    }

    public String getText() {
        return text;
    }
}
