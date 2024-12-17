package org.example.tasks;

public class User {
	private String partitionKey;
	private String rowKey;
	private String name;
	private String email;
	private String phoneNumber;

	// Конструктор для створення нового користувача
	public User(String partitionKey, String rowKey, String name, String email, String phoneNumber) {
		this.partitionKey = partitionKey;
		this.rowKey = rowKey;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	// Геттери та сеттери
	public String getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
