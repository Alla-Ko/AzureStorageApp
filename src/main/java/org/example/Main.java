package org.example;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import org.example.tasks.*;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
	public static void main(String[] args) {
		// Демонстрація роботи з Blob Storage
		//BlobStorageService blobService = new BlobStorageService();
		//uploadBlobs(blobService);
		//downloadBlobs(blobService);
		//removeBlobs(blobService);

		// Демонстрація роботи з Table Storage
		//TableStorageService.initializeClient();
		//User user = new User("partitionKey1", "rowKey1", "John Doe", "john.doe@example.com", "123-456-7890");
		//addAndGetUser(user);
		//updateUser(user);// Оновлюємо дані користувача
    	//removeUser(user);// Видаляємо користувача з таблиці


		// Демонстрація роботи з Queue Storage
		//QueueStorageService.initializeClient(); // Ініціалізація підключення до Queue Storage
	//	addQueues(); //додавання повідомлень в чергу
	//	receiveQueues(); // Отримання і видалення повідомлень з черги


		// Демонстрація роботи з Azure File Storage
		FileStorageService.initializeClient(); // Ініціалізація клієнта для роботи з Azure File Storage
		FileStorageService fileStorageService = new FileStorageService(); // Створення екземпляра сервісу

		uploadFilesToStorage(fileStorageService);
//		downloadFilesFromStorage(fileStorageService);
//		deleteFilesFromStorage(fileStorageService); 		// Видалення файлів з Azure File Storage
	}

	private static void removeBlobs(BlobStorageService blobService) {
		blobService.removeFile("1.txt");
		blobService.removeFile("1.jpg");
	}

	private static void deleteFilesFromStorage(FileStorageService fileStorageService) {
		fileStorageService.deleteFile("1.txt");
		fileStorageService.deleteFile("1.jpg");

		System.out.println("--------------------------------------------------------\n");
	}

	private static void downloadFilesFromStorage(FileStorageService fileStorageService) {
		// Завантаження файлів з Azure File Storage на локальний диск
		fileStorageService.downloadFile("1.txt", "C:/Users/allak/Desktop/bags/downloaded_1.txt");
		fileStorageService.downloadFile("1.jpg", "C:/Users/allak/Desktop/bags/downloaded_1.jpg");
	}

	private static void uploadFilesToStorage(FileStorageService fileStorageService) {
		System.out.println("------------------FileStorageService------------------");
		// Завантаження файлів в Azure File Storage
		fileStorageService.uploadFile("C:/Users/allak/Desktop/bags/1.txt");
		fileStorageService.uploadFile("C:/Users/allak/Desktop/bags/1.jpg");

		// Перегляд файлів в Azure File Storage
		System.out.println("Перелік файлів у частці:");
		fileStorageService.listFiles();
	}

	private static void receiveQueues() {
		QueueStorageService.receiveMessage();
		QueueStorageService.receiveMessage();
		System.out.println("--------------------------------------------------------\n");
	}

	private static void addQueues() {
		System.out.println("------------------QueueStorageService------------------");


		// Створення нової черги
		QueueStorageService.createQueue();

		// Додавання повідомлень в чергу
		QueueStorageService.addMessage("Повідомлення 1");
		QueueStorageService.addMessage("Повідомлення 2");
	}

	private static void removeUser(User user) {
		TableStorageService.deleteUser(user.getPartitionKey(), user.getRowKey());

		// Переглядаємо всі записи після видалення
		TableStorageService.listAllUsers();
		System.out.println("--------------------------------------------------------\n");
	}

	private static void updateUser(User user) {
		user.setName("John Doe Updated");
		user.setEmail("john.updated@example.com");
		user.setPhoneNumber("987-654-3210");
		TableStorageService.updateUser(user);

		// Переглядаємо всі записи в таблиці
		TableStorageService.listAllUsers();
		User retrievedUser = TableStorageService.getUser(user.getPartitionKey(), user.getRowKey());
		if (retrievedUser != null) {
			System.out.println("Отриманий користувач: " + retrievedUser.getName());
			System.out.println("Email: " + retrievedUser.getEmail());
			System.out.println("Телефон: " + retrievedUser.getPhoneNumber());
		}
	}

	private static void addAndGetUser(User user) {
		System.out.println("------------------TableStorageService------------------");
		// Ініціалізуємо підключення до таблиці


		// Додаємо користувача до таблиці
		TableStorageService.addUser(user);
		// Отримуємо користувача за PartitionKey та RowKey
		User retrievedUser = TableStorageService.getUser("partitionKey1", "rowKey1");
		if (retrievedUser != null) {
			System.out.println("Отриманий користувач: " + retrievedUser.getName());
			System.out.println("Email: " + retrievedUser.getEmail());
			System.out.println("Телефон: " + retrievedUser.getPhoneNumber());
		}
	}

	private static void downloadBlobs(BlobStorageService blobService) {
		blobService.downloadFile("1.txt", "C:/Users/allak/Desktop/bags/1new.txt");
		blobService.downloadFile("1.jpg", "C:/Users/allak/Desktop/bags/1new.jpg");
		blobService.listFiles();
		System.out.println("--------------------------------------------------------\n");
	}

	private static void uploadBlobs(BlobStorageService blobService) {
		System.out.println("------------------BlobStorageService------------------");
		blobService.uploadFile("C:/Users/allak/Desktop/bags/1.txt");
		blobService.uploadFile("C:/Users/allak/Desktop/bags/1.jpg");
		blobService.listFiles();
	}


}