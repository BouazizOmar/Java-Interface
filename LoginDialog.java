import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean connectPressed = false;


    public LoginDialog(JFrame parent) {
        super(parent, "Connectez-vous !", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(240, 240, 240)); 
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setFont(labelFont);


        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(labelFont);

        addComponent(contentPanel, usernameLabel, 0, 0);
        addComponent(contentPanel, usernameField, 0, 1);
        addComponent(contentPanel, passwordLabel, 1, 0);
        addComponent(contentPanel, passwordField, 1, 1);

        JButton connectButton = new JButton("Connecter");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(50, 150, 50)); // Green background
        connectButton.setForeground(Color.WHITE); // White text
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectPressed = true;
                dispose();
            }
        });

        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(200, 50, 50)); // Red background
        cancelButton.setForeground(Color.WHITE); // White text
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addComponent(contentPanel, connectButton, 2, 0);
        addComponent(contentPanel, cancelButton, 2, 1);

        add(contentPanel);
    }

    private void addComponent(Container container, Component component, int gridy, int gridx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add(component, gbc);
    }





    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return String.valueOf(passwordField.getPassword());
    }

    public boolean isConnectPressed() {
        return connectPressed;
    }
}
