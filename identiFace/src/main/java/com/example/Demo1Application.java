package com.example;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.storage.StorageProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
		new File(ReturnTrainController.uploadingDir).mkdirs();
	}
}
