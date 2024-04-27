package chat.client.view;

import chat.client.controller.Controller;
import chat.server.model.Message;

import javax.swing.*;
import java.awt.*;

public class ClientView extends JFrame implements View {
    private Controller clientController;

    private JPanel loginPane ;
    private ChatPane chatPane;

    public ClientView(Controller clientController) {
        this.clientController = clientController;
        loginPane = new LoginPane(clientController);
        chatPane = new ChatPane(clientController);

        setTitle("Chat Client");

        setContentPane(loginPane);

        setMinimumSize(new Dimension(300, 260));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setVisible(true);
    }
    public void changeToChatPane() {
        loginPane.setVisible(false);
        setContentPane(chatPane);
        chatPane.setVisible(true);
        pack();
        repaint();
    }

    @Override
    public void displayMessage(Message message) {
        chatPane.displayMessage(message);
    }

    @Override
    public void changeToLoginPane() {

        chatPane.setVisible(false);
        setContentPane(loginPane);
        loginPane.setVisible(true);
    }

    @Override
    public void updateContacts() {

    }
}
