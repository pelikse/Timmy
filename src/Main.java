import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Image;


public class Main {
    private static JTextField timerTextField;
    private static Timer countdownTimer;
    private static TrayIcon trayIcon;
    private static int minutes;
    private static int seconds;
    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.setTitle("Timmy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
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
        label.setBounds(10, 30, 700, 460);
        label.setVerticalAlignment(JLabel.TOP);

        JLabel todoLabel = new JLabel("TO-DO LIST");
        todoLabel.setBounds(30, 180, 250, 30);
        todoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        todoLabel.setForeground(Color.white);

        JButton pomodoro = new JButton("<html>\nPOMODORO <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;25:00\n</html>");
        pomodoro.setOpaque(true);
        pomodoro.setContentAreaFilled(true);
        pomodoro.setBorderPainted(false);
        pomodoro.setFocusPainted(false);
        pomodoro.setBackground(Color.black);
        pomodoro.setForeground(Color.white);
        pomodoro.setBounds(400, 304, 160, 45);
        pomodoro.setFocusable(false);

        JButton longBreak = new JButton("<html>\nLONG BREAK <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;15:00\n</html>");
        longBreak.setOpaque(true);
        longBreak.setContentAreaFilled(true);
        longBreak.setBorderPainted(false);
        longBreak.setFocusPainted(false);
        longBreak.setBackground(Color.black);
        longBreak.setForeground(Color.white);
        longBreak.setBounds(400, 355, 160, 45);
        longBreak.setFocusable(false);

        JButton shortBreak = new JButton("<html>\nSHORT BREAK <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;05:00\n</html>");
        shortBreak.setOpaque(true);
        shortBreak.setContentAreaFilled(true);
        shortBreak.setBorderPainted(false);
        shortBreak.setFocusPainted(false);
        shortBreak.setBackground(Color.black);
        shortBreak.setForeground(Color.white);
        shortBreak.setBounds(400, 406, 160, 45);
        shortBreak.setFocusable(false);

        JButton start = new JButton("START");
        start.setOpaque(true);
        start.setContentAreaFilled(true);
        start.setBorderPainted(false);
        start.setFocusPainted(false);
        start.setBackground(Color.black);
        start.setForeground(Color.white);
        start.setBounds(570, 304, 150, 105);
        start.setFocusable(false);

        JButton end = new JButton("END");
        end.setOpaque(true);
        end.setContentAreaFilled(true);
        end.setBorderPainted(false);
        end.setFocusPainted(false);
        end.setBackground(Color.black);
        end.setForeground(Color.white);
        end.setBounds(570, 415, 150, 35);
        end.setFocusable(false);

        JLabel label2 = new JLabel("Select Mode:");
        label2.setForeground(Color.white);
        label2.setBounds(400, 275, 170, 30);

        timerTextField = new JTextField("25:00");
        timerTextField.setBackground(Color.black);
        timerTextField.setBounds(400, 50, 320, 220);
        timerTextField.setHorizontalAlignment(JLabel.CENTER);
        timerTextField.setFont(new Font("Arial", Font.BOLD, 110));
        timerTextField.setForeground(Color.white);
        timerTextField.setEditable(true);

        DefaultListModel<Item> model = new DefaultListModel<>();

        Connection connection = SQLiteConnection.getConnection();

        try {
            DatabaseHelper db = new DatabaseHelper();

            db.createTable(connection);

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM todos";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String descriptionStr = resultSet.getString("description");
                Boolean done = resultSet.getBoolean("done");
                JLabel description;
                if (done)
                    description = new JLabel("<html><strike>" + descriptionStr + "</strike></html>");
                else
                    description = new JLabel(descriptionStr);
                model.addElement(new Item(id, description, done));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JList<Item> toDoList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(toDoList);
        scrollPane.setBounds(30, 220, 321, 150);
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

        JButton markButton = new JButton("Mark as Done");
        markButton.setOpaque(true);
        markButton.setContentAreaFilled(true);
        markButton.setBorderPainted(false);
        markButton.setFocusPainted(false);
        markButton.setBackground(Color.black);
        markButton.setForeground(Color.white);
        markButton.setBounds(30, 420, 321, 30);
        markButton.setFocusable(false);

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
        frame.add(end);
        frame.add(longBreak);
        frame.add(shortBreak);
        frame.add(pomodoro);
        frame.add(todoLabel);
        frame.add(timerTextField);
        frame.add(label);
        frame.add(addButton);
        frame.add(updateButton);
        frame.add(markButton);
        frame.add(removeButton);
        frame.add(scrollPane);

        addButton.addActionListener(e -> {
            String task = JOptionPane.showInputDialog(frame, "Enter task:");
            if (task != null && !task.trim().isEmpty()) {
                try {
                    String sql = "INSERT INTO todos (description, done) VALUES (?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    statement.setString(1, task);
                    statement.setBoolean(2, false);

                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            model.addElement(new Item(id, new JLabel(task), false));
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
                String initialTask = "";
                Boolean done = false;
                try {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM todos WHERE id = " + id;
                    ResultSet resultSet = statement.executeQuery(sql);

                    while (resultSet.next()) {
                        initialTask = resultSet.getString("description");
                        done = resultSet.getBoolean("done");
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
                            JLabel updatedTaskLabel;
                            if (done)
                                updatedTaskLabel = new JLabel("<html><strike>" + updatedTask+ "</strike></html>");
                            else
                                updatedTaskLabel = new JLabel(updatedTask);
                            model.setElementAt(new Item(id, updatedTaskLabel, done), selectedIndex);
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

        markButton.addActionListener(e -> {
            int selectedIndex = toDoList.getSelectedIndex();
            int id = model.getElementAt(selectedIndex).getId();
            if (selectedIndex != -1) {
                String description = "";
                Boolean done = false;
                try {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM todos WHERE id = " + id;
                    ResultSet resultSet = statement.executeQuery(sql);

                    while (resultSet.next()) {
                        description = resultSet.getString("description");
                        done = resultSet.getBoolean("done");
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                try {
                    String sql = "UPDATE todos SET done = ? WHERE id = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setBoolean(1, !done);
                    statement.setInt(2, id);

                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data marked as done successfully.");
                        JLabel taskLabel;
                        if (!done)
                            taskLabel = new JLabel("<html><strike>" + description + "</strike></html>");
                        else
                            taskLabel = new JLabel(description);
                        model.setElementAt(new Item(id, taskLabel, done), selectedIndex);
                    } else {
                        System.out.println("Failed marking as done");
                    }

                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = toDoList.getSelectedIndex();
            if (selectedIndex != -1) {
                try {
                    String sql = "DELETE FROM todos WHERE id = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);

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

        pomodoro.addActionListener(e -> timerTextField.setText("25:00"));

        longBreak.addActionListener(e -> timerTextField.setText("15:00"));

        shortBreak.addActionListener(e -> timerTextField.setText("05:00"));

        end.addActionListener(e -> {
            if (countdownTimer != null && countdownTimer.isRunning()) {
                endCountDownTimer(start, frame, longBreak, shortBreak, pomodoro);
            }
        });

        start.addActionListener(e -> {
            if (timerTextField.getText().equals("00:00")) {
                return;
            }

            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop();
                start.setText("RESUME");
                longBreak.setEnabled(true);
                shortBreak.setEnabled(true);
                pomodoro.setEnabled(true);
                return;
            }

            if (SystemTray.isSupported() && countdownTimer == null) {
                PopupMenu popupMenu = new PopupMenu();

                MenuItem openItem = new MenuItem("Open");
                openItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.toFront();
                        frame.repaint();
                    }
                });

                MenuItem toggleItem = new MenuItem("Pause");
                toggleItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (timerTextField.getText().equals("00:00")) {
                            return;
                        }
                        if (countdownTimer.isRunning()) {
                            toggleItem.setLabel("Resume");
                            countdownTimer.stop();
                        } else {
                            toggleItem.setLabel("Pause");
                            countdownTimer.start();
                        }
                    }
                });

                MenuItem endItem = new MenuItem("End");
                endItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (countdownTimer.isRunning()) {
                            endCountDownTimer(start, frame, longBreak, shortBreak, pomodoro);
                        }
                    }
                });

                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });

                popupMenu.add(openItem);
                popupMenu.addSeparator();
                popupMenu.add(toggleItem);
                popupMenu.add(endItem);
                popupMenu.addSeparator();
                popupMenu.add(exitItem);

                SystemTray tray = SystemTray.getSystemTray();
                trayIcon = new TrayIcon(getImageForTime("00:00"), "Countdown Timer", popupMenu);

                try {
                    tray.add(trayIcon);

                    countdownTimer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (seconds > 0) {
                                seconds--;
                            } else {
                                if (minutes > 0) {
                                    minutes--;
                                    seconds = 59;
                                } else {
                                    countdownTimer.stop();
                                    longBreak.setEnabled(true);
                                    shortBreak.setEnabled(true);
                                    pomodoro.setEnabled(true);
                                    start.setText("START");
                                    trayIcon.setToolTip("Countdown finished!");
                                    trayIcon.displayMessage("Countdown Finished", "The countdown has ended.", TrayIcon.MessageType.INFO);
                                    ((Timer) e.getSource()).stop();
                                    JOptionPane.showMessageDialog(frame, "Timer Finished!");
                                }
                            }
                            String formattedTime = String.format("%02d:%02d", minutes, seconds);
                            trayIcon.setImage(getImageForTime(formattedTime));
                            timerTextField.setText(formattedTime);
                        }
                    });

                    countdownTimer.start();
                } catch (AWTException exception) {
                    System.err.println("Error adding tray icon.");
                }
            } else if (countdownTimer != null) {
                countdownTimer.start();
            } else {
                System.err.println("System tray not supported.");
            }

            String time = timerTextField.getText();
            String[] timeSplit = time.split(":");
            minutes = Integer.parseInt(timeSplit[0]);
            seconds = Integer.parseInt(timeSplit[1]);

            longBreak.setEnabled(false);
            shortBreak.setEnabled(false);
            pomodoro.setEnabled(false);
            start.setText("PAUSE");
        });
    }

    private static void endCountDownTimer(JButton start, JFrame frame, JButton longBreak, JButton shortBreak, JButton pomodoro) {
        countdownTimer.stop();
        timerTextField.setText("00:00");
        start.setText("START");
        JOptionPane.showMessageDialog(frame, "Timer Finished!");
        longBreak.setEnabled(true);
        shortBreak.setEnabled(true);
        pomodoro.setEnabled(true);
        trayIcon.setImage(getImageForTime("00:00"));
        return;
    }

    private static java.awt.Image getImageForTime(String time) {
        java.awt.Image originalImage = Toolkit.getDefaultToolkit().getImage("tray_icon.png");
        BufferedImage bufferedImage = new BufferedImage(464, 345, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.setFont(new Font("Arial", Font.BOLD, 180));
        graphics.setColor(Color.white);
        graphics.drawString(time, 0, 230);
        graphics.dispose();

        return bufferedImage;
    }
}

