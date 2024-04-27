package chat.client.view;

import chat.client.controller.Controller;
import chat.server.model.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ChatPane extends JPanel {
    private final Controller controller;

    private JScrollPane chatLogPane;
    private JTextField chatInput;
    private JButton sendButtonChat;
    private JPanel contentPane;
    private JTabbedPane tabs;

    private JTextArea chatLog;

    private JTextField directInputField;
    private JTextField contactInputField;
    private JButton sendButtonDirect;


    public ChatPane(Controller controller) {
        this.controller = controller;
        setUp();
    }

    private void setUp() {

        sendButtonDirect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMessage(new Message("You to " + contactInputField.getText(), directInputField.getText(), "PRIVATE_MESSAGE"));
                controller.sendMessage(new Message(contactInputField.getText(), directInputField.getText(), "PRIVATE_MESSAGE"));
                directInputField.setText("");

            }
        });


        sendButtonChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMessage(new Message("You", chatInput.getText(), "BROADCAST"));
                controller.sendMessage(new Message("ALL", chatInput.getText(), "BROADCAST"));
                chatInput.setText("");
            }
        });
        chatLogPane.setViewportView(chatLog);

        add(contentPane);
        setVisible(true);
    }

    public void displayMessage(Message message) {
        if (message != null) {
            if (message.getMeta().length > 1) {
                switch (message.getMeta()[1]) {
                    case "JOIN" -> {
                        chatLog.append(String.format("%s joined\n", message.getLine()));
                    }
                    case "LOGIN_SUCCESS" -> {
                        chatLog.append("You are logged in\n");
                    }
                }
            } else {
                switch (message.getMeta()[0]) {
                    case "PRIVATE_MESSAGE" -> {
                        chatLog.append(String.format("%s to you: %s\n", message.getContact(), message.getLine()));
                    }
                    default -> {
                        chatLog.append(String.format(" %s: %s: \n", message.getContact(), message.getLine()));
                    }
                }
            }
        }
    }
}