package chat.server.model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private String login;
    private boolean isAuthorized = false;

    public Client(String login) {
        this.login = login;
        isAuthorized = true;
        socket = null;
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Client(Socket socket) {
        this.socket = socket;
        try {

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything();
            throw new RuntimeException(e);
        }

    }

    public void closeEverything() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket +
                ", writer=" + writer +
                ", reader=" + reader +
                ", login='" + login + '\'' +
                ", isAuthorized=" + isAuthorized +
                '}';
    }
}
