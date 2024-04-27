package chat.client.model;

public interface Model {
    boolean removeContact(String part);

    String getProperty(String key);

    void updateContacts(String[] contacts);
}
