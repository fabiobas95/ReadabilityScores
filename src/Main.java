import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReadabilityScoreController controller = null;
        if (args.length == 0) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the file name:");
            controller = new ReadabilityScoreController(sc.nextLine());
        } else {
            controller = new ReadabilityScoreController(args[0]);
        }
        controller.mainLoop();
    }
}
