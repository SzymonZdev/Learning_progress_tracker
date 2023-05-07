package tracker;

import java.util.*;

public class Data {
    private final Map<String, Course> courses;
    private final Set<Student> students;
    public Data() {
        courses = new HashMap<>();
        students = new HashSet<>();
        // add each course here when constructing the object
        courses.put("Java", new Course("Java", 600));
        courses.put("DSA", new Course("DSA", 400));
        courses.put("Databases", new Course("Databases", 480));
        courses.put("Spring", new Course("Spring", 550));
    }
    // put here the methods that interact with the classes and the students
    public void addJavaPoints(Student student, int points) {
        courses.get("Java").addPoints(student, points);
    }
    public void addDSAPoints(Student student, int points) {
        courses.get("DSA").addPoints(student, points);
    }
    public void addDatabasesPoints(Student student, int points) {
        courses.get("Databases").addPoints(student, points);
    }
    public void addSpringPoints(Student student, int points) {
        courses.get("Spring").addPoints(student, points);
    }

    // add students method
    public void addStudents() {
        String entry = UserInteraction.scanner.nextLine();
        int count = 0;
        while (!entry.equals("back")) {
            Student student = new Student(entry);
            if (student.correctEntry) {
                if (emailNotTaken(student.email)) {
                    System.out.println("The student has been added.");
                    students.add(student);
                    count++;
                } else {
                    System.out.println("This email is already taken.");
                }
            }
            entry = UserInteraction.scanner.nextLine();
        }
        System.out.println("Total " + count + " students have been added.");
    }
    // list students
    public void listStudents() {
        if (students.size() == 0) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            students.forEach(System.out::println);
        }
    }
    // add points
    public void addPoints() {
        String entry = UserInteraction.scanner.nextLine();
        if (entry.equals("back")) {
            return;
        }
        String[] entryArr = entry.split(" ");
        if (entryArr.length != 5) {
            System.out.println("Incorrect points format.");
            addPoints();
        }
        int studentID = 0;
        try {
            studentID = Integer.parseInt(entryArr[0]);
        } catch (NumberFormatException e) {
            System.out.printf("No student is found for id=%s.\n", entryArr[0]);
            addPoints();
        }
        int javaPoints = getInt(entryArr[1]);
        int DSAPoints = getInt(entryArr[2]);
        int dbPoints = getInt(entryArr[3]);
        int springPoints = getInt(entryArr[4]);

        Student student = getByID(studentID);
        if (student == null) {
            System.out.printf("No student is found for id=%d.\n", studentID);
            UserInteraction.scanner.nextLine();
            addPoints();
        } else {
            if (javaPoints != Integer.MAX_VALUE && DSAPoints != Integer.MAX_VALUE && dbPoints != Integer.MAX_VALUE && springPoints != Integer.MAX_VALUE) {
                addJavaPoints(student, javaPoints);
                addDSAPoints(student, DSAPoints);
                addDatabasesPoints(student, dbPoints);
                addSpringPoints(student, springPoints);
                System.out.println("Points updated.");
                addPoints();
            } else {
                System.out.println("Incorrect points format.");
                addPoints();
            }
        }
    }

    // statistics + details after naming
    public void printStatistics() {
        System.out.println("Most popular: " + getMostPopularCourse());
        System.out.println("Least popular: " + getLeastPopularCourse());
        System.out.println("Highest activity: " + getHighestActivityCourse());
        System.out.println("Lowest activity: " + getLowestActivityCourse());
        System.out.println("Easiest course: " + getEasiestCourse());
        System.out.println("Hardest course: " + getHardestCourse());

        printDetails();
    }

    private void printDetails() {
        String choice = UserInteraction.scanner.nextLine();

        if (!choice.equals("back")) {
            if (courses.keySet().stream().anyMatch(e -> e.equalsIgnoreCase(choice))) {
                generateDetails(courses.get(getNameOfCourse(choice)));
            } else {
                System.out.println("Unknown course.");
            }
            printDetails();
        }
    }

    private String getNameOfCourse(String choice) {
        for (String course: courses.keySet()
             ) {
            if (course.equalsIgnoreCase(choice)) {
                return course;
            }
        }
        return null;
    }

    private void generateDetails(Course course) {
        List<Student> sortedList = course.getSortedStudentList();
        String leftAlignFormat = "%-6s %-6d %-7s %n";

        System.out.println(course.name);
        System.out.format(" id      points   completed   %n");
        for (Student student: sortedList) {
            System.out.format(leftAlignFormat, student.ID, course.studentsAssigned.get(student), course.getPercentageComplete(student));
        }
    }

    private String getHardestCourse() {
        StringBuilder hardestCourse = new StringBuilder();
        double hardestCourseNumber = Double.MAX_VALUE;
        for (String course: this.courses.keySet()
        ) {
            double courseAvgScore = courses.get(course).getAvgScore();
            if (courseAvgScore > 0 && courseAvgScore < hardestCourseNumber && !getEasiestCourse().contains(course)) {
                hardestCourseNumber = courseAvgScore;
                hardestCourse = new StringBuilder();
                hardestCourse.append(course);
            } else if (courseAvgScore == hardestCourseNumber) {
                hardestCourse.append(", ").append(course);
            }
        }
        if (hardestCourseNumber == Double.MAX_VALUE) {
            return "n/a";
        }
        return hardestCourse.toString();
    }

    private String getEasiestCourse() {
        StringBuilder easiestCourse = new StringBuilder();
        double easiestCourseNumber = 0.0;
        for (String course: this.courses.keySet()
        ) {
            double courseAvgScore = courses.get(course).getAvgScore();
            if (courseAvgScore > easiestCourseNumber) {
                easiestCourseNumber = courseAvgScore;
                easiestCourse = new StringBuilder();
                easiestCourse.append(course);
            } else if (courseAvgScore == easiestCourseNumber && easiestCourseNumber != 0.0) {
                easiestCourse.append(", ").append(course);
            }
        }
        if (easiestCourseNumber == 0.0) {
            return "n/a";
        }
        return easiestCourse.toString();
    }

    private String getHighestActivityCourse() {
        StringBuilder mostSubmissions = new StringBuilder();
        int mostSubmissionsNumber = 0;
        for (String course: this.courses.keySet()
        ) {
            int numberOfSubmissions = courses.get(course).submissions.size();
            if (numberOfSubmissions > mostSubmissionsNumber) {
                mostSubmissionsNumber = numberOfSubmissions;
                mostSubmissions = new StringBuilder();
                mostSubmissions.append(course);
            } else if (numberOfSubmissions == mostSubmissionsNumber && mostSubmissionsNumber != 0) {
                mostSubmissions.append(", ").append(course);
            }
        }
        if (mostSubmissionsNumber == 0) {
            return "n/a";
        }
        return mostSubmissions.toString();
    }

    private String getLowestActivityCourse() {
        StringBuilder leastSubmissions = new StringBuilder();
        int leastSubmissionsNumber = Integer.MAX_VALUE;
        for (String course: this.courses.keySet()
        ) {
            int numberOfStudents = courses.get(course).studentsAssigned.keySet().size();
            if (numberOfStudents > 0 && numberOfStudents < leastSubmissionsNumber && !getHighestActivityCourse().contains(course)) {
                leastSubmissionsNumber = numberOfStudents;
                leastSubmissions = new StringBuilder();
                leastSubmissions.append(course);
            } else if (numberOfStudents == leastSubmissionsNumber) {
                leastSubmissions.append(", ").append(course);
            }
        }
        if (leastSubmissionsNumber == Integer.MAX_VALUE) {
            return "n/a";
        }
        return leastSubmissions.toString();
    }

    private String getMostPopularCourse() {
        StringBuilder mostPopular = new StringBuilder();
        int mostPopularNumber = 0;
        for (String course: this.courses.keySet()
             ) {
            int numberOfStudents = courses.get(course).studentsAssigned.keySet().size();
            if (numberOfStudents > mostPopularNumber) {
                mostPopularNumber = numberOfStudents;
                mostPopular = new StringBuilder();
                mostPopular.append(course);
            } else if (numberOfStudents == mostPopularNumber && mostPopularNumber != 0) {
                mostPopular.append(", ").append(course);
            }
        }
        if (mostPopularNumber == 0) {
            return "n/a";
        }
        return mostPopular.toString();
    }

    private String getLeastPopularCourse() {
        StringBuilder leastPopular = new StringBuilder();
        int leastPopularNumber = Integer.MAX_VALUE;
        for (String course: this.courses.keySet()
             ) {
            int numberOfStudents = courses.get(course).studentsAssigned.keySet().size();
            if (numberOfStudents > 0 && numberOfStudents < leastPopularNumber && !getMostPopularCourse().contains(course)) {
                leastPopularNumber = numberOfStudents;
                leastPopular = new StringBuilder();
                leastPopular.append(course);
            } else if (numberOfStudents == leastPopularNumber) {
                leastPopular.append(", ").append(course);
            }
        }
        if (leastPopularNumber == Integer.MAX_VALUE) {
            return "n/a";
        }
        return leastPopular.toString();
    }

    //helpers
    private boolean emailNotTaken(String email) {
        return students.stream().noneMatch(e -> e.email.equals(email));
    }
    private int getInt(String stringInt) {
        try {
            int parsed = Integer.parseInt(stringInt);
            if (parsed >= 0) {
                return parsed;
            } else {
                return Integer.MAX_VALUE;
            }
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
    private Student getByID(int id) {
        for (Student student: students
             ) {
            if (student.ID == id) {
                return student;
            }
        }
        return null;
    }

    public void findStudent() {
        String choice = UserInteraction.scanner.nextLine();
        if (choice.equals("back")) {
            return;
        }
        Student student = getByID(Integer.parseInt(choice));
        if (student == null) {
            System.out.println("No student found for id:" + choice);
            findStudent();
        } else {
            System.out.print("\n" + student.ID + " points: ");
            for (String course: courses.keySet()
                 ) {
                System.out.print(course + "=");
                System.out.print(courses.get(course).getPoints(student));
                System.out.print("; ");
            }
            System.out.print("\n");
            findStudent();
        }
    }

    public void notifyComplete() {
        List<Student> studentsNotified = new ArrayList<>();
        for (Course course: courses.values()){
            for (Student student: course.studentsCompleted.keySet()
                 ) {
                if (!course.studentsCompleted.get(student) && !studentsNotified.contains(student)) {
                    studentsNotified.add(student);
                }
            }
            course.notifyComplete(course.name);
        }
        System.out.println("Total " + studentsNotified.size() + " students have been notified.");
    }
}
