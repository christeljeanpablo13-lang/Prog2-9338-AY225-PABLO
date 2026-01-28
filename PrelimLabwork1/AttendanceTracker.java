// AttendanceTracker.java
// Professional university-style attendance tracker with duplicate prevention
// Author: Christel
// Date: January 7, 2026

// GUI and table imports
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// AWT, events, time/date, and file handling
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.io.*;
import java.nio.file.*;

public class AttendanceTracker {

    // Suggested: keep a single log file name constant
    private static final String LOG_FILE = "attendance_log.txt";

    public static void main(String[] args) {
        // Always build Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> createAndShowUI());
    }

    // Build the full UI
    private static void createAndShowUI() {
        // University branding colors
        Color maroon = new Color(128, 0, 32);
        Color gold = new Color(218, 165, 32);

        // Main frame
        JFrame frame = new JFrame("Attendance Time In");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ---------------- HEADER ----------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(maroon);

        JLabel headerLabel = new JLabel("Attendance Time In", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);

        headerPanel.add(headerLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // ---------------- INPUT PANEL ----------------
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new CompoundBorder(
                new LineBorder(maroon, 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form components
        JLabel nameLabel = new JLabel("Attendance Name:");
        JTextField nameField = new JTextField();

        JLabel courseLabel = new JLabel("Course/Year:");
        JTextField courseField = new JTextField();

        JLabel timeInLabel = new JLabel("Time In:");
        JTextField timeInField = new JTextField();
        timeInField.setEditable(false); // auto-filled

        JLabel signatureLabel = new JLabel("E-Signature:");
        JTextField signatureField = new JTextField();
        signatureField.setEditable(false); // auto-filled

        // Styling for labels
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        nameLabel.setFont(labelFont);
        courseLabel.setFont(labelFont);
        timeInLabel.setFont(labelFont);
        signatureLabel.setFont(labelFont);

        nameLabel.setForeground(maroon);
        courseLabel.setForeground(maroon);
        timeInLabel.setForeground(maroon);
        signatureLabel.setForeground(maroon);

        // Initialize current time and random signature on load
        timeInField.setText(LocalDateTime.now().toString());
        signatureField.setText(UUID.randomUUID().toString());

        // Layout form fields
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(courseLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(timeInLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(timeInField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(signatureLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(signatureField, gbc);

        // Save button
        JButton saveButton = new JButton("Save Attendance");
        saveButton.setBackground(maroon);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        inputPanel.add(saveButton, gbc);

        frame.add(inputPanel, BorderLayout.WEST);

        // ---------------- TABLE LOG ----------------
        String[] columns = {"Name", "Course/Year", "Date", "Time In", "E-Signature"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            // Make table cells non-editable
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable attendanceTable = new JTable(tableModel);

        // Table styling
        attendanceTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        attendanceTable.getTableHeader().setBackground(gold);
        attendanceTable.getTableHeader().setForeground(Color.BLACK);

        // Zebra striping
        attendanceTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // ---------------- FOOTER ----------------
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(gold);
        JLabel footerLabel = new JLabel("© Powered By Christel Pablo` - Attendance Tracker");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerLabel.setForeground(Color.BLACK);
        footerPanel.add(footerLabel);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // ---------------- LOAD/REFRESH TABLE ----------------
        Runnable refreshTable = () -> {
            tableModel.setRowCount(0); // clear existing rows
            File logFile = new File(LOG_FILE);
            if (!logFile.exists()) return;

            try {
                java.util.List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
                String name = "", course = "", date = "", timeInVal = "", signature = "";
                for (String line : lines) {
                    if (line.startsWith("Name:"))              name = line.substring(6);
                    else if (line.startsWith("Course/Year:"))   course = line.substring(13);
                    else if (line.startsWith("Date:"))          date = line.substring(6);
                    else if (line.startsWith("Time In:"))       timeInVal = line.substring(9);
                    else if (line.startsWith("E-Signature:"))   signature = line.substring(13);
                    else if (line.startsWith("-----------------------------")) {
                        // end of record — add row
                        tableModel.addRow(new Object[]{name, course, date, timeInVal, signature});
                        // reset for next record
                        name = course = date = timeInVal = signature = "";
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading records:\n" + ex.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Initial load
        refreshTable.run();

        // ---------------- SAVE BUTTON LOGIC ----------------
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Basic validation
                String name = nameField.getText().trim();
                String course = courseField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Attendance Name.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (course.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Course/Year.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Generate current timestamps and signature
                String time = LocalDateTime.now().toString();
                String signature = UUID.randomUUID().toString();
                String today = LocalDate.now().toString();

                // Duplicate check: same Name + same Date
                try {
                    String content = "";
                    File logFile = new File(LOG_FILE);
                    if (logFile.exists()) {
                        content = new String(Files.readAllBytes(Paths.get(LOG_FILE)));
                    }
                    if (content.contains("Name: " + name) && content.contains("Date: " + today)) {
                        JOptionPane.showMessageDialog(frame,
                                "You are already done time in today!",
                                "Attendance Notice", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    // Append record to the file (UTF-8)
                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(LOG_FILE, true), "UTF-8"))) {
                        writer.write("Name: " + name + "\n");
                        writer.write("Course/Year: " + course + "\n");
                        writer.write("Date: " + today + "\n");
                        writer.write("Time In: " + time + "\n");
                        writer.write("E-Signature: " + signature + "\n");
                        writer.write("-----------------------------\n");
                    }

                    // Confirmation dialog
                    JOptionPane.showMessageDialog(frame,
                            "Attendance Submitted Successfully!\n"
                                    + "Name: " + name + "\n"
                                    + "Course/Year: " + course + "\n"
                                    + "Time In: " + time + "\n"
                                    + "E-Signature: " + signature,
                            "Attendance Confirmed", JOptionPane.INFORMATION_MESSAGE);

                    // Update Time In and Signature fields to reflect action
                    timeInField.setText(time);
                    signatureField.setText(signature);

                    // Refresh table after save
                    refreshTable.run();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving attendance:\n" + ex.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Show the UI
        frame.setVisible(true);
    }
}
