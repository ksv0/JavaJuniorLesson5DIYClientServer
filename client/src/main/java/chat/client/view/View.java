package chat.client.view;

import chat.server.model.Message;


public interface View {
    void changeToChatPane();

    void displayMessage(Message message);

    void changeToLoginPane();

    void updateContacts();
}
