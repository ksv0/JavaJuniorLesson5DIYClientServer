package chat.server.controller;

import chat.server.model.Client;
import chat.server.model.Message;
import chat.server.model.Model;

import java.io.*;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;

public class ServerController implements Controller {
    ServerSocket serverSocket;
    private final Model model;

    public ServerController(Model model) {
        this.model = model;
        try {
            serverSocket = new ServerSocket(1719);
            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        while (!serverSocket.isClosed()) {
            try {
                Client client = new Client(serverSocket.accept());
                model.addClient(client);
                listenForMessages(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void listenForMessages(Client client) {
        new Thread(() -> {
            try (BufferedReader reader = client.getReader()) {
                String line;
                while (client.getSocket().isConnected() && (line = reader.readLine()) != null) {
                    handleMessage(client, parseMessage(line));
                }
            } catch (IOException e) {
                System.out.println("Connection closed: " + client.getSocket().isConnected());
                e.printStackTrace();
            }
        }).start();
    }

    private Message parseMessage(String line) {

        return new Message(
                line.substring(1, line.indexOf('{')),
                line.substring(line.indexOf('{') + 1, line.indexOf('}')),
                line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(";")
        );
    }

    private void handleMessage(Client client, Message message) {
        if (client.isAuthorized()) {
            switch (message.getMeta()[0]) {
                case "PRIVATE_MESSAGE" -> {
                    handlePrivateMessage(client, message);
                }
                case "BROADCAST" -> {
                    handleBroadcast(client, message);
                }
                default -> {
                    System.out.println("Unknown message: " + message + " from " + client);
                }

            }
        } else if (message.getMeta()[0].equals("LOGIN")) {
            handleLogin(client, message);
        } else {
            System.out.println("Unknown message: " + message + " from " + client);
        }

    }

    /**
     * @param client  - отправитель сообщения
     * @param message - сообщение "<%s{%s}[%s]>", кому, сообщение, meta[]
     */
    private void handleBroadcast(Client client, Message message) {
        model.getClients()
                .stream()
                .filter(c -> c.getSocket() != null)
                .filter(Client::isAuthorized)
                .filter(c -> !c.getLogin().equals(client.getLogin()))
                .forEach(c -> {
                    try {
                        if (!(message.getMeta().length > 1 && message.getMeta()[1].equals("JOIN"))) {
                            sendMessage(c, new Message(client.getLogin(), message.getLine(), message.getMeta()));
                        }
                    } catch (IOException e) {
                        handleException(e, client);
                    }
                });
    }


    /**
     * @param client
     * @param message contact=логин, line=пароль
     */
    private void handleLogin(Client client, Message message) {
        try {
            if (model.checkLogin(message.getContact(), message.getLine())) {
                client.setAuthorized(true);
                client.setLogin(message.getContact());
                sendMessage(client, new Message("server", "", new String[]{"LOGIN", "LOGIN_SUCCESS"}));
                handleBroadcast(
                        model.SERVER_CLIENT,
                        new Message(
                                model.SERVER_CLIENT.getLogin(),
                                client.getLogin(),
                                new String[]{"BROADCAST", "JOIN"}));
                // либо я дурак и где-то накосячил, либо иде опять забаговалась,
                // перестало работать 6 часов назад и до сих пор не заработало,
                // программа переобросла лишним кодом, но проблему не исправил,
                // уже неделю работаю сутки через сутки и голова уже не варит
            }
        } catch (IOException e) {
            handleException(e, client);
        }
    }


    private void handleException(Exception e, Client client) {
        System.out.println(e.getMessage());
        System.out.println(client.toString());
    }

    private void handlePrivateMessage(Client client, Message message) {
        model.getClients()
                .stream()
                .filter(c -> c.getLogin().equals(client.getLogin()))
                .filter(Client::isAuthorized)
                .forEach(c -> {
                    try {
                        sendMessage(c, new Message(client.getLogin(), message.getLine(), message.getMeta()));
                    } catch (IOException e) {
                        handleException(e, client);
                    }
                });
    }

    private void sendMessage(Client c, Message message) throws IOException {
        System.out.println(message);
        c.getWriter().write(message.toString() + "\n");
        c.getWriter().flush();

    }

    private void updateContactsOnClient(Client c) throws IOException {
        sendMessage(c, new Message(
                "server",
                model.getClients().stream().map(Client::getLogin).collect(Collectors.joining(";")),
                new String[]{"UPDATE_CONTACTS"}));
    }
}