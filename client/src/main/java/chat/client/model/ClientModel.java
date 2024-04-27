package chat.client.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ClientModel implements Model {
    private static final String CONFIG_FILE = "config.properties";

    private static List<String> activeContacts;
    private static HashMap<String, List<String>> history;

    public ClientModel() {
//        readHistory();
        activeContacts = new ArrayList<>();
    }

    private void saveHistory() {
        File file = new File(getProperty("HISTORY_FILE"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                handleException(e);
            }
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));) {
            out.writeObject(history);
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
    }

    private void readHistory() { // TODO
        File file = new File(getProperty("HISTORY_FILE"));
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));) {
                history = (HashMap<String, List<String>>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                handleException(e);
            }
        }
        if (history == null) {
            history = new HashMap<>();
        }
    }

    @Override
    public boolean removeContact(String contact) {
        if (activeContacts.contains(contact)) {
            activeContacts.remove(contact);
            return true;
        }
        return false;
    }

    @Override
    public String getProperty(String key) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException e) {
            handleException(e);
        }
        return properties.getProperty(key);
    }

    @Override
    public void updateContacts(String[] contacts) {
        saveHistory();
        activeContacts.clear();
        activeContacts.addAll(List.of(contacts));
    }
}
