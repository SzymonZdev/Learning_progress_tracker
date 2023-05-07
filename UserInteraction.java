package tracker;

import java.util.*;

public class UserInteraction {
    boolean interactionOver = false;
    static Scanner scanner = new Scanner(System.in);
    Data data;
    public UserInteraction() {
        data = new Data();
        startInteraction();
    }

    private void startInteraction() {
        System.out.println("Learning Progress Tracker");
        while (!interactionOver) {
            String command = scanner.nextLine();
            switch (command.trim()) {
                case "" -> System.out.println("No input.");
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                case "list" -> data.listStudents();
                case "add points" -> {
                    System.out.println("Enter an id and points or 'back' to return:");
                    data.addPoints();
                }
                case "find" -> {
                    System.out.println("Enter an id and points or 'back' to return:");
                    data.findStudent();
                }
                case "add students" -> {
                    System.out.println("Enter student credentials or 'back' to return:");
                    data.addStudents();
                }
                case "statistics" -> {
                    System.out.println("Type the name of a course to see details or 'back' to quit:");
                    data.printStatistics();
                }
                case "notify" -> {
                    data.notifyComplete();
                }
                case "exit" -> {
                    System.out.println("Bye!");
                    interactionOver = true;
                }
                default -> System.out.println("Error: unknown command!");
            }
        }
    }
}
