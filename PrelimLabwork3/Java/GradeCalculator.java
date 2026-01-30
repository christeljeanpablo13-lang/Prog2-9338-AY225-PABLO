// GradeCalculator.java
// University of Perpetual Help – Prelim Grade Evaluation System
// Author: Christel Pablo
// Date: January 29, 2026

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class GradeCalculator extends JFrame {

    private JTextField attendanceField, lab1Field, lab2Field, lab3Field;
    private JTextArea outputArea;
    private JProgressBar progressBar;

    private final DecimalFormat df = new DecimalFormat("0.00");

    // UPH Color Palette
    private final Color MAROON = new Color(128, 0, 0);
    private final Color GOLD   = new Color(212, 175, 55);
    private final Color YELLOW = new Color(255, 215, 0);
    private final Color WHITE  = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(245, 245, 245);

    public GradeCalculator() {
        setTitle("University of Perpetual Help – Prelim Evaluation");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    // ================= HEADER =================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAROON);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel(
                "<html><center>UNIVERSITY OF PERPETUAL HELP SYSTEM<br>" +
                "<span style='font-size:14px;'>Prelim Grade Evaluation System</span></center></html>",
                SwingConstants.CENTER
        );
        title.setForeground(WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 20));

        header.add(title, BorderLayout.CENTER);
        return header;
    }

    // ================= MAIN CONTENT =================
    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridLayout(1, 2, 20, 20));
        main.setBackground(LIGHT_GRAY);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        main.add(createInputCard());
        main.add(createResultCard());

        return main;
    }

    // ================= INPUT CARD =================
    private JPanel createInputCard() {
        JPanel card = new JPanel(new GridLayout(6, 2, 10, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAROON, 2),
                "Student Academic Inputs"
        ));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);

        card.add(createLabel("Attendance Grade (%)", labelFont));
        attendanceField = new JTextField();
        card.add(attendanceField);

        card.add(createLabel("Lab Work 1 (%)", labelFont));
        lab1Field = new JTextField();
        card.add(lab1Field);

        card.add(createLabel("Lab Work 2 (%)", labelFont));
        lab2Field = new JTextField();
        card.add(lab2Field);

        card.add(createLabel("Lab Work 3 (%)", labelFont));
        lab3Field = new JTextField();
        card.add(lab3Field);

        JButton calculateButton = new JButton("Evaluate Prelim Standing");
        calculateButton.setBackground(MAROON);
        calculateButton.setForeground(WHITE);
        calculateButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        card.add(new JLabel());
        card.add(calculateButton);

        calculateButton.addActionListener(e -> calculateGrade());

        return card;
    }

    // ================= RESULT CARD =================
    private JPanel createResultCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GOLD, 2),
                "Evaluation Result"
        ));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        outputArea.setBackground(WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(MAROON);

        card.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        card.add(progressBar, BorderLayout.SOUTH);

        return card;
    }

    // ================= FOOTER =================
    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(MAROON);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel text = new JLabel(
                "Powered by Christel Pablo | University of Perpetual Help System",
                SwingConstants.CENTER
        );
        text.setForeground(WHITE);
        text.setFont(new Font("SansSerif", Font.ITALIC, 12));

        footer.add(text);
        return footer;
    }

    // ================= LOGIC =================
    private void calculateGrade() {
        try {
            double attendance = Double.parseDouble(attendanceField.getText());
            double lab1 = Double.parseDouble(lab1Field.getText());
            double lab2 = Double.parseDouble(lab2Field.getText());
            double lab3 = Double.parseDouble(lab3Field.getText());

            if (!valid(attendance) || !valid(lab1) || !valid(lab2) || !valid(lab3)) {
                showError("All grades must be between 0% and 100%.");
                return;
            }

            double labAverage = (lab1 + lab2 + lab3) / 3.0;
            double classStanding =
                    (attendance * 0.40) +
                    (labAverage * 0.60);

            double reqPassing =
                    (75 - (classStanding * 0.70)) / 0.30;

            double reqExcellent =
                    (100 - (classStanding * 0.70)) / 0.30;

            outputArea.setForeground(MAROON);
            outputArea.setText(
                    "PRELIM GRADE ANALYSIS\n" +
                    "------------------------------\n" +
                    "Attendance Grade : " + df.format(attendance) + "%\n" +
                    "Lab Work Average : " + df.format(labAverage) + "%\n" +
                    "Class Standing   : " + df.format(classStanding) + "%\n\n" +
                    "Required Prelim Exam:\n" +
                    "• Passing (75%)    : " + df.format(reqPassing) + "%\n" +
                    "• Excellent (100%) : " + df.format(reqExcellent) + "%\n"
            );

            progressBar.setValue((int) Math.round(classStanding));

            if (reqPassing >= 0 && reqPassing <= 100) {
                progressBar.setForeground(GOLD);
                outputArea.append("\nSTATUS: PASSING IS ACHIEVABLE");
            } else {
                progressBar.setForeground(YELLOW.darker());
                outputArea.append("\nSTATUS: PASSING IS NOT ACHIEVABLE");
            }

        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values.");
        }
    }

    private boolean valid(double v) {
        return v >= 0 && v <= 100;
    }

    private void showError(String msg) {
        outputArea.setForeground(MAROON);
        outputArea.setText("INPUT ERROR:\n" + msg);
        progressBar.setValue(0);
        progressBar.setForeground(MAROON);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GradeCalculator().setVisible(true);
        });
    }
}
