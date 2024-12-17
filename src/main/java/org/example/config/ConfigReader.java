package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigReader {

	public static class Config {
		public String azure_storage_account_name;
		public String azure_storage_account_key;
		public String azure_blob_container_name;
		public String azure_table_name;
		public String azure_queue_name;
		public String azure_file_share_name;
	}

	public static Config getConfig() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(new File("src/main/resources/azure_config.json"), Config.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
