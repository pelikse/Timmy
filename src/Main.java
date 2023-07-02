import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {
    private static JLabel timerLabel;
    private static Timer countdownTimer;
    private static int minutes;
    private static int seconds;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Timmy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 470);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel();
        label.setIcon(image);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setBounds(0, 20, 700, 460);
        label.setVerticalAlignment(JLabel.TOP);

        JLabel todoLabel = new JLabel("TO-DO LIST");
        todoLabel.setBounds(40, 180, 250, 30);
        todoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        todoLabel.setForeground(Color.white);

        JButton pomodoro = new JButton("<html>\nPOMODORO <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;25:00\n</html>");
        pomodoro.setOpaque(true);
        pomodoro.setContentAreaFilled(true);
        pomodoro.setBorderPainted(false);
        pomodoro.setFocusPainted(false);
        pomodoro.setBackground(Color.black);
        pomodoro.setForeground(Color.white);
        pomodoro.setBounds(450, 260, 150, 40);
        pomodoro.setFocusable(false);

        JButton longBreak = new JButton("<html>\nLONG BREAK <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;15:00\n</html>");
        longBreak.setOpaque(true);
        longBreak.setContentAreaFilled(true);
        longBreak.setBorderPainted(false);
        longBreak.setFocusPainted(false);
        longBreak.setBackground(Color.black);
        longBreak.setForeground(Color.white);
        longBreak.setBounds(450, 312, 150, 40);
        longBreak.setFocusable(false);

        JButton shortBreak = new JButton("<html>\nSHORT BREAK <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;05:00\n</html>");
        shortBreak.setOpaque(true);
        shortBreak.setContentAreaFilled(true);
        shortBreak.setBorderPainted(false);
        shortBreak.setFocusPainted(false);
        shortBreak.setBackground(Color.black);
        shortBreak.setForeground(Color.white);
        shortBreak.setBounds(450, 365, 150, 40);
        shortBreak.setFocusable(false);

        JButton start = new JButton("START");
        start.setOpaque(true);
        start.setContentAreaFilled(true);
        start.setBorderPainted(false);
        start.setFocusPainted(false);
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.setBounds(620, 260, 150, 150);
        start.setFocusable(false);

        JLabel label2 = new JLabel("Select Mode:");
        label2.setForeground(Color.white);
        label2.setBounds(452, 225, 150, 30);

        timerLabel = new JLabel("25:00");
        timerLabel.setBackground(Color.black);
        timerLabel.setBounds(500, 100, 200, 100);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 70));
        timerLabel.setForeground(Color.white);

        DefaultListModel<Item> model = new DefaultListModel<>();

        Connection connection = MySQLConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM todos";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                model.addElement(new Item(id, description));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JList<Item> toDoList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(toDoList);
        scrollPane.setBounds(40, 220, 300, 150);
        toDoList.setBackground(Color.gray);
        toDoList.setForeground(Color.white);
        toDoList.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton addButton = new JButton("Add");
        addButton.setOpaque(true);
        addButton.setContentAreaFilled(true);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setBackground(Color.black);
        addButton.setForeground(Color.white);
        addButton.setBounds(30, 380, 100, 30);
        addButton.setFocusable(false);

        JButton updateButton = new JButton("Update");
        updateButton.setOpaque(true);
        updateButton.setContentAreaFilled(true);
        updateButton.setBorderPainted(false);
        updateButton.setFocusPainted(false);
        updateButton.setBackground(Color.black);
        updateButton.setForeground(Color.white);
        updateButton.setBounds(140, 380, 100, 30);
        updateButton.setFocusable(false);

        JButton removeButton = new JButton("Remove");
        removeButton.setOpaque(true);
        removeButton.setContentAreaFilled(true);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.setBackground(Color.black);
        removeButton.setForeground(Color.white);
        removeButton.setBounds(250, 380, 100, 30);
        removeButton.setFocusable(false);

        frame.getContentPane().setBackground(new Color(52, 47, 48, 255));
        frame.setLayout(null);

        frame.add(label2);
        frame.add(start);
        frame.add(longBreak);
        frame.add(shortBreak);
        frame.add(pomodoro);
        frame.add(todoLabel);
        frame.add(timerLabel);
        frame.add(label);
        frame.add(addButton);
        frame.add(updateButton);
        frame.add(removeButton);
        frame.add(scrollPane);

        addButton.addActionListener(e -> {
            String task = JOptionPane.showInputDialog(frame, "Enter task:");
            if (task != null && !task.trim().isEmpty()) {
                Connection connection1 = MySQLConnection.getConnection();

                try {
                    String sql = "INSERT INTO todos (id, description) VALUES (DEFAULT, ?)";
                    PreparedStatement statement = connection1.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    statement.setString(1, task);

                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            model.addElement(new Item(id, task));
                        }
                        generatedKeys.close();

                        System.out.println("Data inserted successfully.");
                    } else {
                        System.out.println("Failed to insert data.");
                    }

                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

            }
        });

        updateButton.addActionListener(e -> {
            int selectedIndex = toDoList.getSelectedIndex();
            int id = model.getElementAt(selectedIndex).getId();
            if (selectedIndex != -1) {
                String initialTask = new String();
                try {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM todos WHERE id = " + id;
                    ResultSet resultSet = statement.executeQuery(sql);

                    while (resultSet.next()) {
                        initialTask = resultSet.getString("description");
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                String updatedTask = JOptionPane.showInputDialog(null, "Enter updated task:", "Input Dialog",
                        JOptionPane.PLAIN_MESSAGE, null, null, initialTask).toString();
                if (updatedTask != null && !updatedTask.trim().isEmpty()) {
                    try {
                        String sql = "UPDATE todos SET description = ? WHERE id = ?";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        statement.setString(1, updatedTask);
                        statement.setInt(2, id);

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Data updated successfully.");
                            model.setElementAt(new Item(id, updatedTask), selectedIndex);
                        } else {
                            System.out.println("No data found or failed to update data.");
                        }

                        statement.close();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = toDoList.getSelectedIndex();
            if (selectedIndex != -1) {
                Connection connection12 = MySQLConnection.getConnection();

                try {
                    String sql = "DELETE FROM todos WHERE id = ?";
                    PreparedStatement statement = connection12.prepareStatement(sql);

                    statement.setInt(1, model.getElementAt(selectedIndex).getId());

                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data removed successfully.");
                    } else {
                        System.out.println("No data found or failed to remove data.");
                    }

                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                model.remove(selectedIndex);
            }
        });

        pomodoro.addActionListener(e -> timerLabel.setText("25:00"));

        longBreak.addActionListener(e -> timerLabel.setText("15:00"));

        shortBreak.addActionListener(e -> timerLabel.setText("00:01"));

        start.addActionListener(e -> {
            String time = timerLabel.getText();
            String[] timeSplit = time.split(":");
            minutes = Integer.parseInt(timeSplit[0]);
            seconds = Integer.parseInt(timeSplit[1]);

            countdownTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (seconds > 0) {
                        seconds--;
                    } else {
                        if (minutes > 0) {
                            minutes--;
                            seconds = 59;
                        } else {
                            countdownTimer.stop();
                            JOptionPane.showMessageDialog(frame, "Timer Finished!");
                            start.setText("START");
                        }
                    }

                    String formattedTime = String.format("%02d:%02d", minutes, seconds);
                    timerLabel.setText(formattedTime);
                }
            });

            countdownTimer.start();
            start.setText("PAUSE");
    });
}}
