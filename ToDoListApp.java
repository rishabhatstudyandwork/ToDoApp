import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ToDoListApp {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private JButton addButton, removeButton, completeButton, saveButton;
    private static final String FILE_NAME = "tasks.txt";

    public ToDoListApp() {
        JFrame frame = new JFrame("To-Do List Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null); // Center the window

        Color deepRed = new Color(139, 0, 0);
        Color offWhite = new Color(245, 245, 245);
        Color buttonColor = new Color(200, 0, 0);

        JPanel panel = new JPanel();
        panel.setBackground(offWhite);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("To-Do List Maker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(deepRed);
        panel.add(titleLabel, BorderLayout.NORTH);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        taskList.setSelectionBackground(deepRed);
        taskList.setSelectionForeground(Color.WHITE);
        loadTasks();

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(deepRed, 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(offWhite);
        taskInput = new JTextField(20);
        taskInput.setFont(new Font("Arial", Font.PLAIN, 16));

        addButton = createStyledButton("Add", buttonColor);
        removeButton = createStyledButton("Remove", buttonColor);
        completeButton = createStyledButton("Complete", buttonColor);
        saveButton = createStyledButton("Save", buttonColor);

        inputPanel.add(taskInput);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(completeButton);
        inputPanel.add(saveButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        completeButton.addActionListener(e -> completeTask());
        saveButton.addActionListener(e -> saveTasks());

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return button;
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            taskListModel.addElement(task);
            taskInput.setText("");
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskListModel.remove(selectedIndex);
        }
    }

    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String completedTask = taskListModel.get(selectedIndex) + " (Completed)";
            taskListModel.set(selectedIndex, completedTask);
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                writer.write(taskListModel.get(i));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Tasks saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    taskListModel.addElement(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}