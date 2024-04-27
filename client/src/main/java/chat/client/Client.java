package chat.client;


import chat.client.controller.ClientController;
import chat.client.model.ClientModel;

/**
 * todo
 * 1 Подключение к серверу
 * 2 Параллельная обработка сообщений (туда сюда одновременно)
 * 3 Обработка множественных сообщений (пока туда сюда сколько угодно)
 * 4 Отправка личных сообщений
 * 5 Корректное закрытие ресурсов
 * 6 Юзабилити
 */
public class Client {


    public static void main(String[] args) {
        new ClientController(new ClientModel());
    }
}
