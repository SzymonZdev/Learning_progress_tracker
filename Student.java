package tracker;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student {
    final int ID;
    String name;
    String surname;
    String email;
    boolean correctEntry = true;

    public Student(String entry) {
        String[] allWords = entry.split(" ");
        this.ID = Math.abs(new Random().nextInt(1000, 2000));
        if (allWords.length < 3) {
            this.correctEntry = false;
            System.out.println("Incorrect credentials.");
            return;
        }
        StringBuilder surnameBuilder = new StringBuilder();
        if (!verifyName(allWords[0])) {
            this.correctEntry = false;
            System.out.println("Incorrect first name.");
            return;
        }
        this.name = allWords[0];
        for (int i = 1; i < allWords.length-1; i++) {

            if (verifyName(allWords[i])) {
                surnameBuilder.append(allWords[i]).append(" ");
            } else {
                this.correctEntry = false;
                System.out.println("Incorrect last name.");
                return;
            }
            this.surname = surnameBuilder.toString().trim();
        }
        if (!verifyEmail(allWords[allWords.length-1])) {
            this.correctEntry = false;
            System.out.println("Incorrect email.");
        } else {
            this.email = allWords[allWords.length-1];
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.ID);
    }

    private boolean verifyName(String name) {
        return name.matches("[A-Za-z][A-Za-z-']*[A-Za-z]") && notContainsConsecutive(name);
    }

    private boolean verifyEmail(String name) {
        return name.matches("[A-Za-z0-9_.-]+@[A-Za-z0-9_.-]+\\.[A-Za-z0-9_.-]+");
    }

    private boolean notContainsConsecutive(String name) {
        Pattern pattern = Pattern.compile("(''|--|'-|-')");
        Matcher matcher = pattern.matcher(name);
        return !matcher.find();
    }

    public void notifyComplete(String courseName) {
        System.out.println("To: " + this.email);
        System.out.println("Re: Your Learning Progress");
        System.out.println("Hello, " + this.name + " " + this.surname + "! You have accomplished our " + courseName + " course!");
    }
}
