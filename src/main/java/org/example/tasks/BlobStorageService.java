package org.example.tasks;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.core.util.IterableStream;
import org.example.config.ConfigReader;  // Додаємо import для ConfigReader

import java.nio.file.Path;
import java.nio.file.Paths;

public class BlobStorageService {

	private String connectionString;
	private String containerName;

	// Ініціалізація клієнта для доступу до контейнера
	private BlobContainerClient getBlobContainerClient() {
		// Отримуємо конфігурацію з ConfigReader
		ConfigReader.Config config = ConfigReader.getConfig(); // Читання конфігурації з JSON файлу

		if (config == null) {
			System.out.println("Помилка зчитування конфігурації!");
			return null;
		}

		// Отримуємо connectionString та containerName з конфігураційного файлу
		connectionString = "DefaultEndpointsProtocol=https;" +
				"AccountName=" + config.azure_storage_account_name + ";" +
				"AccountKey=" + config.azure_storage_account_key + ";";

		containerName = config.azure_blob_container_name;

		// Створюємо і повертаємо BlobContainerClient
		return new BlobContainerClientBuilder()
				.connectionString(connectionString)
				.containerName(containerName)
				.buildClient();
	}

	// Метод для завантаження файлів у Blob Storage
	public void uploadFile(String filePath) {
		// Створюємо Blob клієнт
		BlobContainerClient containerClient = getBlobContainerClient();
		if (containerClient == null) return;

		// Визначаємо ім'я файлу
		Path path = Paths.get(filePath);
		String fileName = path.getFileName().toString();

		// Створюємо Blob клієнт для файлу
		BlobClient blobClient = containerClient.getBlobClient(fileName);

		// Завантажуємо файл
		try {
			blobClient.uploadFromFile(filePath, true); // true для перезапису, якщо файл вже існує
			System.out.println("Файл успішно завантажено в Blob Storage: " + fileName);
		} catch (com.azure.storage.blob.models.BlobStorageException e) {  // Специфічний виняток для Azure Blob Storage
			System.out.println("Azure помилка при завантаженні файлу: " + e.getMessage());
		} catch (Exception e) {  // Загальна обробка інших винятків
			System.out.println("Помилка при завантаженні файлу: " + e.getMessage());
		}
	}

	// Метод для вивантаження файлів з Blob Storage
	public void downloadFile(String fileName, String downloadPath) {
		// Створюємо Blob клієнт
		BlobContainerClient containerClient = getBlobContainerClient();
		if (containerClient == null) return;

		BlobClient blobClient = containerClient.getBlobClient(fileName);

		// Завантажуємо файл
		try {
			blobClient.downloadToFile(downloadPath);
			System.out.println("Файл успішно вивантажено до: " + downloadPath);
		} catch (com.azure.storage.blob.models.BlobStorageException e) {  // Специфічний виняток для Azure Blob Storage
			System.out.println("Azure помилка при вивантаженні файлу: " + e.getMessage());
		} catch (Exception e) {  // Загальна обробка інших винятків
			System.out.println("Помилка при вивантаженні файлу: " + e.getMessage());
		}
	}
	public void removeFile(String fileName) {
		// Створюємо Blob клієнт
		BlobContainerClient containerClient = getBlobContainerClient();
		if (containerClient == null) return;

		BlobClient blobClient = containerClient.getBlobClient(fileName);
		if (blobClient.exists()) {
			try {
				blobClient.delete();
				System.out.println("Файл успішно видалено з Blob Storage: " + fileName);
			} catch (com.azure.storage.blob.models.BlobStorageException e) {  // Специфічний виняток для Azure Blob Storage
				System.out.println("Azure помилка при видаленні файлу: " + e.getMessage());
			} catch (Exception e) {  // Загальна обробка інших винятків
				System.out.println("Помилка при видаленні файлу: " + e.getMessage());
			}
		} else {
			System.out.println("Файл не знайдено.");
		}
		// Видаляємо файл

	}
	// Метод для переліку всіх файлів у контейнері
	public void listFiles() {
		BlobContainerClient containerClient = getBlobContainerClient();
		if (containerClient == null) return;

		IterableStream<BlobItem> blobItems = containerClient.listBlobs();

		System.out.println("Список файлів у контейнері:");
		for (BlobItem blobItem : blobItems) {
			System.out.println(blobItem.getName());
		}
	}
}
