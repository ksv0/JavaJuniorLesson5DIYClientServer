package chat.client.view;

import chat.client.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class LoginPane extends JPanel {

    private final Controller controller;


    JLabel ipLabel ;
    JLabel portLabel ;
    JLabel loginLabel ;
    JLabel passwordLabel ;


    private JTextField ipField;
    private JTextField portField;

    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel contentPane;



    public LoginPane(Controller controller) {
        this.controller = controller;
        setUp();
    }

    private void setUp() {
        Dimension fieldSize = new Dimension(150,30);
        ipField.setMinimumSize(fieldSize);
        portField.setMinimumSize(fieldSize);
        loginField.setMinimumSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);

        loginButton.addActionListener(
                e -> controller.login(
                        loginField.getText(),
                        new String(passwordField.getPassword()),
                        ipField.getText(),
                        portField.getText()));

        add(contentPane);
        setVisible(true);

    }


}
