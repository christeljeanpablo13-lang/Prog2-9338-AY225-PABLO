/*
 Programmer: Christel Pablo - 25-0224-644
 Course: Coding Blitz CRUD Exam
 Description: Java Swing Student Record System with CSV File I/O
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class StudentRecordSystem extends JFrame {

    DefaultTableModel model;
    JTable table;
    JTextField txtId, txtFirstName, txtLastName, txtLab1, txtLab2, txtLab3, txtPrelim, txtAttendance;
    JButton btnAdd, btnDelete;

    public StudentRecordSystem() {
        setTitle("Student Records - Christel Pablo 25-0224-644");
        setSize(1100, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Match columns to CSV file
        model = new DefaultTableModel(
            new String[]{"StudentID", "First Name", "Last Name", "Lab1", "Lab2", "Lab3", "Prelim Exam", "Attendance"}, 
            0
        );
        table = new JTable(model);

        // Load data from CSV
        loadCSV();

        // ✅ Input fields
        txtId = new JTextField(8);
        txtFirstName = new JTextField(8);
        txtLastName = new JTextField(8);
        txtLab1 = new JTextField(5);
        txtLab2 = new JTextField(5);
        txtLab3 = new JTextField(5);
        txtPrelim = new JTextField(5);
        txtAttendance = new JTextField(5);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("ID")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("First")); inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last")); inputPanel.add(txtLastName);
        inputPanel.add(new JLabel("Lab1")); inputPanel.add(txtLab1);
        inputPanel.add(new JLabel("Lab2")); inputPanel.add(txtLab2);
        inputPanel.add(new JLabel("Lab3")); inputPanel.add(txtLab3);
        inputPanel.add(new JLabel("Prelim")); inputPanel.add(txtPrelim);
        inputPanel.add(new JLabel("Attendance")); inputPanel.add(txtAttendance);
        inputPanel.add(btnAdd); inputPanel.add(btnDelete);

        // ✅ Add new row
        btnAdd.addActionListener(e -> {
            if (!txtId.getText().isEmpty() && !txtFirstName.getText().isEmpty() && !txtLastName.getText().isEmpty()) {
                model.addRow(new Object[]{
                    txtId.getText(),
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtLab1.getText(),
                    txtLab2.getText(),
                    txtLab3.getText(),
                    txtPrelim.getText(),
                    txtAttendance.getText()
                });

                txtId.setText(""); txtFirstName.setText(""); txtLastName.setText("");
                txtLab1.setText(""); txtLab2.setText(""); txtLab3.setText("");
                txtPrelim.setText(""); txtAttendance.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in ID, First Name, and Last Name.");
            }
        });

        // ✅ Delete selected row
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    // ✅ Load CSV correctly
    private void loadCSV() {
        try {
            // Try multiple possible locations
            String[] possiblePaths = {
                "c:\\Users\\cj\\OneDrive\\Desktop\\2nd Semester Programming 2\\Prog2-9338-AY225-PABLO\\PrelimExam\\MOCK_DATA.csv",
                System.getProperty("user.dir") + "\\MOCK_DATA.csv",
                System.getProperty("user.dir") + "\\..\\MOCK_DATA.csv",
                "MOCK_DATA.csv"
            };

            File csvFile = null;
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    csvFile = f;
                    break;
                }
            }

            if (csvFile == null || !csvFile.exists()) {
                JOptionPane.showMessageDialog(
                    this,
                    "❌ CSV file not found!\n\n" +
                    "Please create MOCK_DATA.csv in:\n" +
                    "c:\\Users\\cj\\OneDrive\\Desktop\\2nd Semester Programming 2\\Prog2-9338-AY225-PABLO\\PrelimExam\\",
                    "File Not Found",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            br.readLine(); // skip header row

            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    model.addRow(new Object[]{
                        data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(),
                        data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim()
                    });
                    rowCount++;
                }
            }
            br.close();

            JOptionPane.showMessageDialog(
                this,
                "✅ CSV loaded successfully!\n" + rowCount + " student records loaded.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "❌ Error loading CSV: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ✅ Entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentRecordSystem().setVisible(true);
        });
    }
}