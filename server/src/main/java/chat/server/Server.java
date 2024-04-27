package chat.server;


import chat.server.controller.ServerController;
import chat.server.model.ServerModel;

/**
 * todo
 * 1 Слушать заданный порт, в ожидании запроса на подключение
 * 2 После получения запроса, продолжать слушать порт
 * 3 Идентифицировать участника чата по имени
 * 4 Отправлять всем участникам чата сообщение "Участник подключился"
 * 5 Уметь отправлять сообщения всем участникам сайта кроме отправителя
 * 6 Уметь пересылать ЛИЧНЫЕ сообщения
 */
public class Server {
    public static void main(String[] args) {
        ServerController server = new ServerController(new ServerModel());

    }
}
