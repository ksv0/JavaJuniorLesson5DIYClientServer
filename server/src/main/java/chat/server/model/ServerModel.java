package chat.server.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ServerModel implements Model {

    private static List<Client> clients;
    private static HashMap<String, String> users = new HashMap<>();

    public ServerModel() {
        readUsers();
        clients = new ArrayList<>();
    }


    private void readUsers() {

        File file = new File("server/src/main/resources/users.csv");
        users = new HashMap<>();
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addClient(Client client) {
        try {
            clients.add(client);
        } catch (RuntimeException e) {
            return;
        }
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }

    @Override
    public boolean checkLogin(String login, String password) {
        System.out.println("Registered users: " + users);
        if (!users.containsKey(login)) {
            users.put(login, password);
            saveUsers();
            return true;
        } else if (users.containsKey(login) && users.get(login).equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public void removeClient(Client recipient) {
        clients.remove(recipient);
    }

    private void saveUsers() {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("server/src/main/resources/users.csv"))))) {
            for (String login : users.keySet()) {
                System.out.println(login + " " + users.get(login));
                writer.write(login + "," + users.get(login) + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}