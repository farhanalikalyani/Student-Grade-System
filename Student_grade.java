/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.student_grade;

/**
 *
 * @author Naveed
 */

// Student_grade.java
// Author: Naveed... | Reg No: 548376 | Date: April 2026
//
// This class handles student records including add, display,
// statistics, searching, updating, deletion, and exporting data.

import java.util.*;
import java.io.*;

public class Student_grade {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Instantiate GradeBook (the class that actually has
        // insertStudent/showAll/findStudent/etc.), not Student_grade.
        GradeBook gradeBook = new GradeBook();

        while (true) {

            System.out.println("\n========================================");
            System.out.println("      STUDENT GRADEBOOK SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Marks");
            System.out.println("5. Delete Student");
            System.out.println("6. Class Statistics");
            System.out.println("7. Student Ranking");
            System.out.println("8. Export Report");
            System.out.println("9. Total Students");
            System.out.println("0. Exit");
            System.out.print("Enter Choice: ");

            // Validated integer read instead of a bare input.nextInt(),
            // so bad input (letters, blank line, etc.) can't crash the program.
            int choice = readInt(input, "Enter Choice: ");

            switch (choice) {

                case 1:

                    System.out.print("Student Name: ");
                    String name = input.nextLine();

                    Subject[] subjects =
                            new Subject[GradeBook.SUBJECT_COUNT];

                    for (int i = 0; i < GradeBook.SUBJECT_COUNT; i++) {

                        int marksIn;
                        while (true) {
                            marksIn = readInt(input, "Enter "
                                    + GradeBook.SUBJECT_TITLES[i]
                                    + " Marks: ");

                            // Bounds check marks (0-100)
                            if (marksIn >= 0 && marksIn <= 100) break;
                            System.out.println("Marks must be between 0 and 100.");
                        }

                        subjects[i] = new Subject(
                                GradeBook.SUBJECT_TITLES[i],
                                marksIn);
                    }

                    gradeBook.insertStudent(name, subjects);
                    break;

                case 2:

                    gradeBook.showAll();
                    break;

                case 3:

                    System.out.print("Enter Name to Search: ");
                    gradeBook.findStudent(input.nextLine());
                    break;

                case 4:

                    System.out.print("Student Name: ");
                    String student = input.nextLine();

                    System.out.println("\nSubjects");

                    for (int i = 0;
                         i < GradeBook.SUBJECT_COUNT;
                         i++) {

                        System.out.println(i + " = "
                                + GradeBook.SUBJECT_TITLES[i]);
                    }

                    int index = readInt(input, "Subject Index: ");
                    int marks = readInt(input, "New Marks: ");

                    if (index < 0 || index >= GradeBook.SUBJECT_COUNT) {
                        System.out.println("Invalid subject index.");
                        break;
                    }

                    if (marks < 0 || marks > 100) {
                        System.out.println("Marks must be between 0 and 100.");
                        break;
                    }

                    if (gradeBook.modifyScore(student, index, marks))
                        System.out.println("Updated Successfully.");
                    else
                        System.out.println("Student Not Found.");

                    break;

                case 5:

                    System.out.print("Student Name: ");

                    Student removed =
                            gradeBook.deleteStudent(input.nextLine());

                    if (removed != null)
                        System.out.println("Deleted Successfully.");
                    else
                        System.out.println("Student Not Found.");

                    break;

                case 6:

                    gradeBook.classReport();
                    break;

                case 7:

                    gradeBook.showRanking();
                    break;

                case 8:

                    try {

                        gradeBook.saveToFile();
                        System.out.println(
                                "Report exported successfully.");

                    } catch (IOException e) {

                        System.out.println(
                                "Unable to save file.");
                    }

                    break;

                case 9:

                    System.out.println("Total Students = "
                            + GradeBook.getTotalEntries());

                    break;

                case 0:

                    System.out.println("Thank You.");
                    input.close();
                    return;

                default:

                    System.out.println("Invalid Choice.");
            }
        }
    }

    // Safe integer reader: re-prompts on non-numeric input instead of
    // throwing an uncaught InputMismatchException and killing the program.
    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}

//============================================================

class GradeBook {

    public static final int SUBJECT_COUNT = 4;

    public static final String[] SUBJECT_TITLES =
            {"Math", "English", "Science", "CS"};

    private static int totalEntries = 0;

    private ArrayList<Student> recordList;

    public GradeBook() {

        recordList = new ArrayList<>();
    }

    public static int getTotalEntries() {

        return totalEntries;
    }

    public void insertStudent(String name,
                              Subject[] subjects) {

        Student st = new Student(name, subjects);

        recordList.add(st);

        totalEntries++;

        System.out.println("\nStudent Added Successfully.");
        System.out.printf("Average : %.2f\n",
                st.getAverage());

        System.out.println("Grade : "
                + st.getGrade());
    }

    public void showAll() {

        if (recordList.isEmpty()) {

            System.out.println("No Student Found.");
            return;
        }

        System.out.println();

        System.out.printf("%-20s",
                "Student");

        for (String s : SUBJECT_TITLES)
            System.out.printf("%10s", s);

        System.out.printf("%10s%10s\n",
                "Average", "Grade");

        System.out.println(
                "---------------------------------------------------------------");

        for (Student st : recordList) {

            System.out.printf("%-20s",
                    st.getName());

            for (Subject sb :
                    st.getSubjects()) {

                System.out.printf("%10d",
                        sb.getScore());
            }

            System.out.printf("%10.2f%10c\n",
                    st.getAverage(),
                    st.getGrade());
        }
    }

    public void classReport() {

        if (recordList.isEmpty()) {

            System.out.println("No Data.");
            return;
        }

        double total = 0;

        for (Student s : recordList)
            total += s.getAverage();

        System.out.printf("\nClass Average : %.2f\n",
                total / recordList.size());

        System.out.println();

        for (int i = 0; i < SUBJECT_COUNT; i++) {

            System.out.printf("%-10s %.2f\n",
                    SUBJECT_TITLES[i],
                    calculateSubjectAverage(i));
        }
    }

    public double calculateSubjectAverage(int index) {

        double total = 0;

        for (Student s : recordList)
            total += s.getSubjects()[index].getScore();

        return total / recordList.size();
    }

    public void showRanking() {

        if (recordList.isEmpty()) {

            System.out.println("No Students.");
            return;
        }

        ArrayList<Student> temp =
                new ArrayList<>(recordList);

        for (int i = 0;
             i < temp.size() - 1;
             i++) {

            int highest = i;

            for (int j = i + 1;
                 j < temp.size();
                 j++) {

                if (temp.get(j).getAverage()
                        >
                        temp.get(highest).getAverage())

                    highest = j;
            }

            Student t = temp.get(i);

            temp.set(i,
                    temp.get(highest));

            temp.set(highest,
                    t);
        }

        System.out.println();

        for (int i = 0;
             i < temp.size();
             i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + temp.get(i).getName()
                            + "  "
                            + temp.get(i).getAverage()
                            + "  "
                            + temp.get(i).getGrade());
        }
    }

    public void findStudent(String text) {

        boolean found = false;

        for (Student st : recordList) {

            if (st.getName().toLowerCase().contains(text.toLowerCase())) {

                found = true;

                System.out.println("\nStudent Name : " + st.getName());

                for (Subject sb : st.getSubjects()) {

                    System.out.println(sb.getName()
                            + " : "
                            + sb.getScore()
                            + " Grade : "
                            + sb.getLetterGrade());
                }

                System.out.printf("Average : %.2f\n",
                        st.getAverage());

                System.out.println("Overall Grade : "
                        + st.getGrade());
            }
        }

        if (!found)
            System.out.println("Student Not Found.");
    }

    public boolean modifyScore(String name,
                               int subjectIndex,
                               int marks) {

        for (Student st : recordList) {

            if (st.getName().equalsIgnoreCase(name)) {

                st.updateSubjectScore(subjectIndex,
                        marks);

                return true;
            }
        }

        return false;
    }

    public Student deleteStudent(String name) {

        for (int i = 0; i < recordList.size(); i++) {

            if (recordList.get(i).getName()
                    .equalsIgnoreCase(name)) {

                totalEntries--;

                return recordList.remove(i);
            }
        }

        return null;
    }

    // try-with-resources so the file handle is always closed,
    // even if an error happens partway through writing.
    public void saveToFile() throws IOException {

        try (FileWriter fw = new FileWriter("Grade_Report.txt")) {

            fw.write("========== GRADE REPORT ==========\n\n");

            for (Student st : recordList) {

                fw.write("Name : "
                        + st.getName()
                        + "\n");

                for (Subject sb : st.getSubjects()) {

                    fw.write(sb.getName()
                            + " : "
                            + sb.getScore()
                            + " ("
                            + sb.getLetterGrade()
                            + ")\n");
                }

                fw.write(String.format(
                        "Average : %.2f\n",
                        st.getAverage()));

                fw.write("Overall Grade : "
                        + st.getGrade());

                fw.write("\n\n");
            }
        }
    }

}

//====================================================

class Student {

    private String name;

    private Subject[] subjects;

    public Student(String name,
                   Subject[] subjects) {

        this.name = name;

        this.subjects = subjects;
    }

    public String getName() {

        return name;
    }

    public Subject[] getSubjects() {

        return subjects;
    }

    public void updateSubjectScore(int index,
                                   int marks) {

        subjects[index].setScore(marks);
    }

    public double getAverage() {

        int total = 0;

        for (Subject s : subjects)
            total += s.getScore();

        return total / (double) subjects.length;
    }

    public char getGrade() {

        double avg = getAverage();

        if (avg >= 90)
            return 'A';

        if (avg >= 80)
            return 'B';

        if (avg >= 70)
            return 'C';

        if (avg >= 60)
            return 'D';

        return 'F';
    }
}

//====================================================

class Subject {

    private String name;

    private int score;

    public Subject(String name,
                   int score) {

        this.name = name;

        this.score = score;
    }

    public String getName() {

        return name;
    }

    public int getScore() {

        return score;
    }

    public void setScore(int score) {

        this.score = score;
    }

    public char getLetterGrade() {

        if (score >= 90)
            return 'A';

        if (score >= 80)
            return 'B';

        if (score >= 70)
            return 'C';

        if (score >= 60)
            return 'D';

        return 'F';
    }
}