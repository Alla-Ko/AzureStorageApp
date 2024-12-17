package org.example.tasks;

import org.example.config.ConfigReader;
import com.azure.data.tables.*;
import com.azure.data.tables.models.*;
import com.azure.core.exception.HttpResponseException;

import java.util.List;

public class TableStorageService {

	private static TableClient tableClient;

	// Ініціалізація підключення до таблиці
	public static void initializeClient() {
		// Використовуємо метод getConfig() з ConfigReader для отримання конфігурації
		ConfigReader.Config config = ConfigReader.getConfig(); // Читання конфігурації з JSON файлу

		if (config == null) {
			System.out.println("Помилка зчитування конфігурації!");
			return;
		}

		// Отримуємо connectionString та tableName з об'єкта config
		String connectionString = "DefaultEndpointsProtocol=https;" +
				"AccountName=" + config.azure_storage_account_name + ";" +
				"AccountKey=" + config.azure_storage_account_key + ";";
		String tableName = config.azure_table_name;

		// Створюємо клієнт для таблиці
		tableClient = new TableClientBuilder()
				.connectionString(connectionString)
				.tableName(tableName)
				.buildClient();
	}

	// Додавання нового запису в таблицю
	public static void addUser(User user) {
		try {
			// Створюємо новий об'єкт таблиці
			TableEntity entity = new TableEntity(user.getPartitionKey(), user.getRowKey());
			entity.addProperty("name", user.getName());
			entity.addProperty("email", user.getEmail());
			entity.addProperty("phoneNumber", user.getPhoneNumber());

			// Додаємо запис до таблиці
			tableClient.createEntity(entity);
			System.out.println("Запис успішно додано до таблиці "+ entity);
		} catch (HttpResponseException e) {
			System.out.println("Помилка при додаванні запису: " + e.getMessage());
		}
	}

	// Отримання запису з таблиці
	public static User getUser(String partitionKey, String rowKey) {
		try {
			// Отримуємо запис з таблиці за PartitionKey і RowKey
			TableEntity entity = tableClient.getEntity(partitionKey, rowKey);
			User user = new User(entity.getPartitionKey(), entity.getRowKey(),
					(String) entity.getProperty("name"),
					(String) entity.getProperty("email"),
					(String) entity.getProperty("phoneNumber"));

			System.out.println("Запис отримано: " + entity);
			return user;

		} catch (HttpResponseException e) {
			System.out.println("Помилка при отриманні запису: " + e.getMessage());
			return null;
		}
	}

	// Отримання всіх записів з таблиці
	public static void listAllUsers() {
		try {
			// Отримуємо всі записи з таблиці
			List<TableEntity> entities = tableClient.listEntities().stream().toList();
			if(entities.size()>0){
				System.out.println("Записи в таблиці:");
				entities.forEach(entity -> System.out.println(entity));
			}
			else System.out.println("Записів не знайдено");
		} catch (HttpResponseException e) {
			System.out.println("Помилка при отриманні записів: " + e.getMessage());
		}
	}

	// Видалення запису з таблиці
	public static void deleteUser(String partitionKey, String rowKey) {
		try {
			// Видаляємо запис за PartitionKey і RowKey
			tableClient.deleteEntity(partitionKey, rowKey);
			System.out.println("Запис успішно видалено.");
		} catch (HttpResponseException e) {
			System.out.println("Помилка при видаленні запису: " + e.getMessage());
		}
	}

	// Редагування запису в таблиці
	public static void updateUser(User user) {
		try {
			// Отримуємо запис з таблиці
			TableEntity entity = tableClient.getEntity(user.getPartitionKey(), user.getRowKey());

			if (entity != null) {
				// Оновлюємо значення
				entity.addProperty("name", user.getName());
				entity.addProperty("email", user.getEmail());
				entity.addProperty("phoneNumber", user.getPhoneNumber());

				// Перезаписуємо запис
				tableClient.updateEntity(entity);
				System.out.println("Запис успішно оновлено.");
			} else {
				System.out.println("Запис не знайдений для оновлення.");
			}
		} catch (HttpResponseException e) {
			System.out.println("Помилка при оновленні запису: " + e.getMessage());
		}
	}
}
