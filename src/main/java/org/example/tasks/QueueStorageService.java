package org.example.tasks;

import com.azure.storage.queue.*;
import com.azure.storage.queue.implementation.models.QueueMessage;
import com.azure.storage.queue.models.*;
import org.example.config.ConfigReader;  // Додаємо import для ConfigReader

public class QueueStorageService {

	private static QueueClient queueClient;

	// Ініціалізація підключення до черги
	public static void initializeClient() {
		// Отримуємо конфігурацію з ConfigReader
		ConfigReader.Config config = ConfigReader.getConfig(); // Читання конфігурації з JSON файлу

		if (config == null) {
			System.out.println("Помилка зчитування конфігурації!");
			return;
		}

		String connectionString = "DefaultEndpointsProtocol=https;" +
				"AccountName=" + config.azure_storage_account_name + ";" +
				"AccountKey=" + config.azure_storage_account_key + ";";
		String queueName = config.azure_queue_name;

		// Створюємо клієнт для роботи з чергою
		queueClient = new QueueClientBuilder()
				.connectionString(connectionString)
				.queueName(queueName)
				.buildClient();
	}

	// Створення нової черги
	public static void createQueue() {
		queueClient.create();
		System.out.println("Черга успішно створена.");
	}

	// Додавання повідомлення в чергу
	public static void addMessage(String message) {
		queueClient.sendMessage(message);
		System.out.println("Повідомлення успішно додано до черги: " + message);
	}

	// Отримання повідомлення з черги
	public static void receiveMessage() {
		QueueMessageItem message = queueClient.receiveMessage();
		if (message != null) {
			System.out.println("Отримано повідомлення з черги: " + message.getMessageText());
			// Після обробки, можна видалити повідомлення
			queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
		} else {
			System.out.println("Черга порожня.");
		}
	}

	// Видалення повідомлення з черги
	public static void deleteMessage(String messageId, String popReceipt) {
		queueClient.deleteMessage(messageId, popReceipt);
		System.out.println("Повідомлення успішно видалено.");
	}
}
