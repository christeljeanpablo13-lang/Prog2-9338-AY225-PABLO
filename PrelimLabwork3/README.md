PrelimLabWork3

Prelim Grade Computation – Java & JavaScript
Christel Jean Pablo_BSIT-GD

Description

This project implements a Prelim Grade computation program using Java and JavaScript.
Both versions follow the same grading logic, formulas, and output, ensuring consistency across platforms.

The program calculates the Class Standing and determines the required Prelim Exam score needed to:

Pass the Prelim period (75)

Achieve an Excellent grade (100)

Grading Breakdown

Prelim Exam – 30%

Class Standing – 70%

Class Standing Composition

Attendance – 40%

Lab Work Average – 60%

Lab Works included:

Lab Work 1

Lab Work 2

Lab Work 3

Formulas Used

Lab Work Average

(Lab1 + Lab2 + Lab3) / 3


Class Standing

(Attendance × 0.40) + (Lab Average × 0.60)


Required Prelim Exam Score

(Target Grade − (Class Standing × 0.70)) / 0.30

Program Features

Both Java and JavaScript programs:

Accept user inputs for attendance and lab grades

Compute lab average and class standing

Determine required Prelim Exam scores for:

Passing (75)

Excellent (100)

Display all computed values with clear remarks

Project Structure
PrelimLabWork3/
├── Java/
│   ├── PrelimGradeScanner.java
│   └── (Optional) GradeCalculator-Swing.java
└── JavaScript/
    ├── index.html
    └── script.js
    └── style.css
    └── errorsound
    └── heavenly-music

How to Run
Java
javac PrelimGradeScanner.java
java PrelimGradeScanner

JavaScript

Open index.html in a web browser.

Notes

Both implementations use the same formulas and logic

Input values are validated

The program meets all requirements stated in the lab instructions