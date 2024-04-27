package chat.client.controller;

import chat.server.model.Message;

public interface Controller {
    void login(String login, String password, String ip, String port);


    void sendMessage(Message message);


}
