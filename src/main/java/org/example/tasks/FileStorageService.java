package org.example.tasks;

import com.azure.storage.file.share.*;
import com.azure.storage.file.share.models.*;
import org.example.config.ConfigReader;

import java.io.File;

public class FileStorageService {

	private static ShareClient shareClient;

	// Ініціалізація підключення до File Storage
	public static void initializeClient() {
		// Отримуємо конфігурацію з ConfigReader
		ConfigReader.Config config = ConfigReader.getConfig();

		String connectionString = "DefaultEndpointsProtocol=https;" +
				"AccountName=" + config.azure_storage_account_name + ";" +
				"AccountKey=" + config.azure_storage_account_key + ";";

		String shareName = config.azure_file_share_name;  // Отримуємо ім'я частки з конфігурації

		// Створюємо клієнт для доступу до File Share
		shareClient = new ShareClientBuilder()
				.connectionString(connectionString)
				.shareName(shareName)  // Використовуємо ім'я частки з конфігурації
				.buildClient();
	}

	// Завантаження файлу в Azure File Storage
	public void uploadFile(String filePath) {
		// Отримуємо файл
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("Файл не знайдений: " + filePath);
			return;
		}

		// Отримуємо директорію частки
		ShareDirectoryClient directoryClient = shareClient.getDirectoryClient("");

		// Завантажуємо файл у частку
		try {
			ShareFileClient fileClient = directoryClient.getFileClient(file.getName());
			fileClient.uploadFromFile(file.getPath());  // Замінити файл, якщо він існує
			System.out.println("Файл успішно завантажено в частку");
		} catch (Exception e) {
			System.out.println("Помилка під час завантаження файлу: " + e.getMessage());
		}
	}

	// Завантаження файлу з Azure File Storage
	public static void downloadFile(String fileName, String destinationPath) {
		try {
			ShareFileClient fileClient = shareClient.getDirectoryClient("")
					.getFileClient(fileName);

			fileClient.downloadToFile(destinationPath);
			System.out.println("Файл успішно завантажено з Azure File Storage: " + fileName);
		} catch (Exception e) {
			System.out.println("Помилка при завантаженні файлу: " + e.getMessage());
		}
	}

	// Перегляд файлів у Share
	public static void listFiles() {
		try {
			ShareDirectoryClient directoryClient = shareClient.getDirectoryClient("");
			for (ShareFileItem fileItem : directoryClient.listFilesAndDirectories()) {
				System.out.println(fileItem.getName());
			}
		} catch (Exception e) {
			System.out.println("Помилка при перегляді файлів: " + e.getMessage());
		}
	}

	// Видалення файлу з Azure File Storage
	public void deleteFile(String fileName) {
		try {
			// Отримуємо директорію частки
			ShareDirectoryClient directoryClient = shareClient.getDirectoryClient("");

			// Отримуємо клієнт для файлу
			ShareFileClient fileClient = directoryClient.getFileClient(fileName);

			// Видаляємо файл
			fileClient.delete();
			System.out.println("Файл " + fileName + " успішно видалено з частки");
		} catch (Exception e) {
			System.out.println("Помилка під час видалення файлу: " + e.getMessage());
		}
	}
}
