package chat.client.controller;

import chat.server.model.Message;
import chat.client.model.Model;
import chat.client.view.ClientView;
import chat.client.view.View;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientController implements Controller {
    private Model clientModel;
    private View clientView;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ClientController(Model clientModel) {
        this.clientModel = clientModel;
        SwingUtilities.invokeLater(() -> clientView = new ClientView(this));
    }

    @Override
    public void login(String login, String password, String ip, String port) {
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            listenForMessages();
            sendMessage(new Message(login, password, "LOGIN"));

        } catch (IOException e) {
            handleException(e);
        }
    }

    private void handleException(IOException e) {
        e.printStackTrace();
        closeEverything();
    }

    private void closeEverything() {
        try {
            if (socket != null) socket.close();
            if (writer != null) writer.close();
            if (reader != null) reader.close();
        } catch (IOException e) {
            handleException(e);
        }
        clientView.changeToLoginPane();
    }

    private void sendLoginMessage(String login, String password) throws IOException {
        sendMessage(new Message(login, password, new String[]{"LOGIN"}));
    }

    @Override
    public void sendMessage(Message message) {
        if (socket != null && socket.isConnected()) {
            try {
                writer.write(message.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                handleException(e);
            }
        }
    }


    private void listenForMessages() {
        new Thread(() -> {
            try {
                String line;
                while (socket.isConnected() && (line = reader.readLine()) != null) {
                    handleMessage(parseMessage(line));
                }
            } catch (IOException e) {
                handleException(e);
            }
        }).start();
    }

    private void handleMessage(Message message) {
        switch (message.getMeta()[0]) {
            case "BROADCAST", "PRIVATE_MESSAGE" -> {
                clientView.displayMessage(message);
            }
            case "LOGIN" -> {
                if (message.getMeta().length > 1 && message.getMeta()[1].equals("LOGIN_SUCCESS")) {
                    message.setLine("Вы вошли в чат");
                    clientView.changeToChatPane();
                    clientView.displayMessage(message);
                }
            }
            case "UPDATE_CONTACT" -> {
                clientModel.updateContacts(message.getLine().split(";"));

            }
            default -> {
                System.out.println("Unsupported message: " + message);
            }
        }
    }

    private Message parseMessage(String string) { //"<%s{%s}[%s]>", contact, line, meta[]
        return new Message(
                string.substring(1, string.indexOf("{")),
                string.substring(string.indexOf("{") + 1, string.indexOf("}")),
                string.substring(string.indexOf("[") + 1, string.indexOf("]")).split(";")
        );
    }
}