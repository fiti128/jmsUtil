package ru.sberbank.jms.util.services;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 24.11.13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public interface MessageStorageService {
    /**
     * Добавляет сообщение в склад
     * @param textMessage
     */
    void addMessageToStorage(String textMessage);

    /**
     * Очищает склад от сообщений
     */
    boolean clearStorage();

    /**
     * Выдает все сообщения со склада одной строкой
     * @return
     */
    String getMessagesFromStorage();
}
