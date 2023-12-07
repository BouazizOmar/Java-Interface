/*import Library  */

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hotel extends JFrame {
    Connection con = Connect.getConnection();
    private int currentUserId;
    
    public int getCurrentUserId() {
        return currentUserId;
    }
    /*
        function Hotel avis Interface
     */
    public Hotel() {
        setTitle("Avis_Hotel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu homeMenu = new JMenu("Home");
        JMenuItem connectItem = new JMenuItem("Connect to Database");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem dashboard = new JMenuItem("Dashboard");

        connectItem.addActionListener(e -> showLoginDialog());

        dashboard.addActionListener(e -> {
           new Dashboard(this);
        });

        exitItem.addActionListener(e -> System.exit(0));

        menuBar.add(homeMenu);
        menuBar.add(connectItem);
        menuBar.add(dashboard);
        menuBar.add(exitItem);

        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel rateLabel = new JLabel("Rate :");
        JComboBox<Integer> rateComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel timeLabel = new JLabel("Date :");
        JDateChooser jDateChooser1 = new JDateChooser();

        JLabel typeLabel = new JLabel("Type of Trip:");
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox typeCheckBox1 = new JCheckBox("SOLO");
        JCheckBox typeCheckBox2 = new JCheckBox("Couple");
        JCheckBox typeCheckBox3 = new JCheckBox("Family");
        JCheckBox typeCheckBox4 = new JCheckBox("Job");
        JCheckBox typeCheckBox5 = new JCheckBox("Friends");
        checkboxPanel.add(typeCheckBox1);
        checkboxPanel.add(typeCheckBox2);
        checkboxPanel.add(typeCheckBox3);
        checkboxPanel.add(typeCheckBox4);
        checkboxPanel.add(typeCheckBox5);

        JLabel securiteLabel = new JLabel("Security");
        JComboBox<Integer> securiteComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel commentLabel = new JLabel("Your Comment!:");
        JTextArea commentTextArea = new JTextArea();

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(50, 150, 50));
        submitButton.setForeground(Color.WHITE);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback(rateComboBox, jDateChooser1, typeCheckBox1, typeCheckBox2, typeCheckBox3, typeCheckBox4, typeCheckBox5, securiteComboBox, commentTextArea);
            }
        });

        JButton effaceButton = new JButton("Clear !");
        effaceButton.setFont(new Font("Arial", Font.BOLD, 14));
        effaceButton.setBackground(new Color(200, 50, 50));
        effaceButton.setForeground(Color.WHITE);
        effaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effacerFeedback(rateComboBox, jDateChooser1, typeCheckBox1, typeCheckBox2, typeCheckBox3, typeCheckBox4, typeCheckBox5, securiteComboBox, commentTextArea);
            }
        });

        mainPanel.add(rateLabel);
        mainPanel.add(rateComboBox);
        mainPanel.add(timeLabel);
        mainPanel.add(jDateChooser1);
        mainPanel.add(typeLabel);
        mainPanel.add(checkboxPanel);
        mainPanel.add(securiteLabel);
        mainPanel.add(securiteComboBox);
        mainPanel.add(commentLabel);
        mainPanel.add(commentTextArea);
        mainPanel.add(submitButton);
        mainPanel.add(effaceButton);

        add(mainPanel, BorderLayout.CENTER);

        con = Connect.getConnection();

        setVisible(true);
    }


    /*part submit if not logeed IN */
    
    private void submitFeedback(JComboBox<Integer> rateComboBox, JDateChooser jDateChooser1, JCheckBox typeCheckBox1,
            JCheckBox typeCheckBox2, JCheckBox typeCheckBox3, JCheckBox typeCheckBox4,
            JCheckBox typeCheckBox5, JComboBox<Integer> securiteComboBox, JTextArea commentTextArea) {
        if (currentUserId == 0) { 
         
            showLoginDialog(() -> {submitFeedbackAfterLogin(rateComboBox, jDateChooser1, typeCheckBox1, typeCheckBox2,
                        typeCheckBox3, typeCheckBox4, typeCheckBox5, securiteComboBox, commentTextArea);
            });
        } else {
            submitFeedbackAfterLogin(rateComboBox, jDateChooser1, typeCheckBox1, typeCheckBox2,
                    typeCheckBox3, typeCheckBox4, typeCheckBox5, securiteComboBox, commentTextArea);
        }
    }
    
    private void showLoginDialog(Runnable afterLoginCallback) {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);

        if (loginDialog.isConnectPressed()) {
            try {
              Connection con = Connect.getConnection();

            if (con != null) {
                String username = loginDialog.getUsername();
                String password = loginDialog.getPassword();

                String checkIfExistsQuery = "SELECT ID FROM citoyen WHERE username = ? AND password = ?";
                try (PreparedStatement checkIfExistsStatement = con.prepareStatement(checkIfExistsQuery)) {
                    checkIfExistsStatement.setString(1, username);
                    checkIfExistsStatement.setString(2, password);

                    try (ResultSet rs = checkIfExistsStatement.executeQuery()) {
                        if (rs.next()) {
                            currentUserId = rs.getInt("ID");
                            System.out.println("Connected User ID: " + currentUserId);
                        } else {
                            // User doesn't exist, insert a new record
                            try (PreparedStatement st = con.prepareStatement("INSERT INTO citoyen(username, password) VALUES (?, ?)")) {
                                st.setString(1, username);
                                st.setString(2, password);

                                int rowsAffected = st.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Citoyen ajouté avec succès à la base de données.");
                                
                                    currentUserId = getLastInsertedId(con);
                                    System.out.println("Connected User ID: " + currentUserId);
                                } else {
                                    System.out.println("Échec de l'ajout du citoyen à la base de données.");
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
                afterLoginCallback.run();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /*part submit normal  */
 

    private void submitFeedbackAfterLogin(JComboBox<Integer> rateComboBox, JDateChooser jDateChooser1, JCheckBox typeCheckBox1,
            JCheckBox typeCheckBox2, JCheckBox typeCheckBox3, JCheckBox typeCheckBox4,
            JCheckBox typeCheckBox5, JComboBox<Integer> securiteComboBox, JTextArea commentTextArea) {
        Connection con = Connect.getConnection();
        try {
            if (con != null) {
                PreparedStatement st = con.prepareStatement("INSERT INTO avis_citoyen(rate, Time, Type, security, comment,id_citoyen) VALUES (?, ?, ?, ?, ?,?)");

                int rate = (int) rateComboBox.getSelectedItem();
                java.util.Date utilDate = jDateChooser1.getDate();
                java.sql.Date date = new java.sql.Date(utilDate.getTime());

                StringBuilder typeBuilder = new StringBuilder();
                if (typeCheckBox1.isSelected()) {
                    typeBuilder.append("SOLO ");
                }
                if (typeCheckBox2.isSelected()) {
                    typeBuilder.append("Couple ");
                }
                if (typeCheckBox3.isSelected()) {
                    typeBuilder.append("Family ");
                }
                if (typeCheckBox4.isSelected()) {
                    typeBuilder.append("Job ");
                }
                if (typeCheckBox5.isSelected()) {
                    typeBuilder.append("Friends");
                }
                String type = typeBuilder.toString().trim();

                int securite = (int) securiteComboBox.getSelectedItem();
                String comment = commentTextArea.getText();

             
                st.setInt(1, rate);
                st.setDate(2, date);
                st.setString(3, type);
                st.setInt(4, securite);
                st.setString(5, comment);
                st.setInt(6,currentUserId );

                System.out.println("SQL Statement: " + st.toString());

                int rowsAffected = st.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Avis ajouté avec succès à la base de données.");
                    effacerFeedback(securiteComboBox, jDateChooser1, typeCheckBox5, typeCheckBox5, typeCheckBox5, typeCheckBox5, typeCheckBox5, securiteComboBox, commentTextArea);
                } else {
                    System.out.println("Échec de l'ajout d'Avis à la base de données.");
                }
            } else {
                System.out.println("Échec a la connexion au base de données.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* clear input method on sumbit button or in clear button */
    private void effacerFeedback(JComboBox<Integer> rateComboBox, JDateChooser jDateChooser1, JCheckBox typeCheckBox1,
            JCheckBox typeCheckBox2, JCheckBox typeCheckBox3, JCheckBox typeCheckBox4,
            JCheckBox typeCheckBox5, JComboBox<Integer> securiteComboBox, JTextArea commentTextArea) {
            
        rateComboBox.setSelectedIndex(0); 
        jDateChooser1.setDate(null); 
        typeCheckBox1.setSelected(false);
        typeCheckBox2.setSelected(false);
        typeCheckBox3.setSelected(false);
        typeCheckBox4.setSelected(false);
        typeCheckBox5.setSelected(false);
        securiteComboBox.setSelectedIndex(0); 
        commentTextArea.setText(""); 

    }

  
    private void showLoginDialog() {
    LoginDialog loginDialog = new LoginDialog(this);
    loginDialog.setVisible(true);

    if (loginDialog.isConnectPressed()) {
        try {
            Connection con = Connect.getConnection();

            if (con != null) {
                String username = loginDialog.getUsername();
                String password = loginDialog.getPassword();

                String checkIfExistsQuery = "SELECT ID FROM citoyen WHERE username = ? AND password = ?";
                try (PreparedStatement checkIfExistsStatement = con.prepareStatement(checkIfExistsQuery)) {
                    checkIfExistsStatement.setString(1, username);
                    checkIfExistsStatement.setString(2, password);

                    try (ResultSet rs = checkIfExistsStatement.executeQuery()) {
                        if (rs.next()) {
                            currentUserId = rs.getInt("ID");
                            System.out.println("Connected User ID: " + currentUserId);
                        } else {
                            // User doesn't exist, insert a new record
                            try (PreparedStatement st = con.prepareStatement("INSERT INTO citoyen(username, password) VALUES (?, ?)")) {
                                st.setString(1, username);
                                st.setString(2, password);

                                int rowsAffected = st.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Citoyen ajouté avec succès à la base de données.");
                                
                                    currentUserId = getLastInsertedId(con);
                                    System.out.println("Connected User ID: " + currentUserId);
                                } else {
                                    System.out.println("Échec de l'ajout du citoyen à la base de données.");
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

    private int getLastInsertedId(Connection con) throws SQLException {
        int lastInsertedId = -1;
        String query = "SELECT LAST_INSERT_ID() as last_id";
        try (PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                lastInsertedId = resultSet.getInt("last_id");
            }
        }
        return lastInsertedId;
    }
}
