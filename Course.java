package tracker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Course {
    String name;
    int completePoints;
    List<Integer> submissions;
    Map<Student, Boolean> studentsCompleted;
    Map<Student, Integer> studentsAssigned;
    public Course(String name, int completePoints) {
        this.name = name;
        this.completePoints = completePoints;
        this.submissions = new ArrayList<>();
        this.studentsAssigned = new HashMap<>();
        this.studentsCompleted = new HashMap<>();
    }

    public void addPoints(Student student, int points) {
        if (points > 0) {
            if (studentsAssigned.containsKey(student)) {
                submissions.add(points);
                studentsAssigned.put(student, studentsAssigned.get(student) + points);
            } else {
                submissions.add(points);
                studentsAssigned.put(student, points);
            }
            if (studentsAssigned.get(student) >= completePoints) {
                studentsCompleted.put(student, false);
            }
        }
    }

    public List<Student> getSortedStudentList() {
        // Sort student lists by the total number of points in descending order and then by the ID in ascending order.
        Comparator<Map.Entry<Student, Integer>> comparator = (o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return Integer.compare(o1.getKey().ID, o2.getKey().ID);
            } else {
                return -o1.getValue().compareTo(o2.getValue());
            }
        };
        List<Student> students = new ArrayList<>();
        studentsAssigned.entrySet().stream().sorted(comparator).forEach(e -> students.add(e.getKey()));

        return students;
    }

    public double getAvgScore() {
        int sum = 0;
        for (int submission: submissions
             ) {
            sum += submission;
        }
        return (double) sum / submissions.size();
    }

    public String getPercentageComplete(Student student) {
        double points = studentsAssigned.get(student);
        double completePoints = this.completePoints;

        Double percentage =  ((points/completePoints)) * 100;
        DecimalFormat format = new DecimalFormat("0.0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(percentage) + "%";
    }

    public int getPoints(Student student) {
        return Objects.requireNonNullElse(studentsAssigned.get(student), 0);
    }

    public void notifyComplete(String courseName) {
        for (Student student: studentsCompleted.keySet()
             ) {
            if (!studentsCompleted.get(student)) {
                student.notifyComplete(courseName);
                studentsCompleted.remove(student);
                studentsCompleted.put(student, true);
            }
        }
    }
}
