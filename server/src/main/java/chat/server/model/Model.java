package chat.server.model;

import java.util.List;

public interface Model {
    public static final Client SERVER_CLIENT = new Client("server");
    void addClient(Client client);

    List<Client> getClients();

    boolean checkLogin(String login, String password);

    void removeClient(Client recipient);


}
