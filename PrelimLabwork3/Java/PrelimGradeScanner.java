// GradeCalculator.java
// University of Perpetual Help – Prelim Grade Evaluation System
// Author: Christel Pablo
// Date: January 29, 2026

import java.util.Scanner;

public class PrelimGradeScanner {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Attendance Score (0-100): ");
        double attendance = sc.nextDouble();

        System.out.print("Enter Lab Work 1 Score (0-100): ");
        double lab1 = sc.nextDouble();

        System.out.print("Enter Lab Work 2 Score (0-100): ");
        double lab2 = sc.nextDouble();

        System.out.print("Enter Lab Work 3 Score (0-100): ");
        double lab3 = sc.nextDouble();

        // Computations
        double labAverage = (lab1 + lab2 + lab3) / 3;
        double classStanding = (attendance * 0.40) + (labAverage * 0.60);

        double requiredPass = (75 - (classStanding * 0.70)) / 0.30;
        double requiredExcellent = (100 - (classStanding * 0.70)) / 0.30;

        // Output
        System.out.println("\n========== PRELIM GRADE COMPUTATION ==========");
        System.out.printf("Attendance Score      : %.2f%%%n%n", attendance);

        System.out.printf("Lab Work 1             : %.2f%%%n", lab1);
        System.out.printf("Lab Work 2             : %.2f%%%n", lab2);
        System.out.printf("Lab Work 3             : %.2f%%%n", lab3);
        System.out.printf("Lab Work Average       : %.2f%%%n%n", labAverage);

        System.out.printf("Class Standing         : %.2f%%%n", classStanding);

        System.out.println("\n---------------------------------------------");
        System.out.println("Required Prelim Exam Score");
        System.out.printf("To PASS (75)           : %.2f%%%n", requiredPass);
        System.out.printf("To be EXCELLENT (100)  : %.2f%%%n%n", requiredExcellent);

        System.out.println("Remarks:");
        System.out.println(requiredPass <= 100
                ? "- Passing the prelim period is achievable."
                : "- Passing the prelim period is NOT achievable.");

        System.out.println(requiredExcellent <= 100
                ? "- Excellent grade is achievable."
                : "- Excellent grade is NOT achievable based on current standing.");

        System.out.println("=============================================");

        sc.close();
    }
}
