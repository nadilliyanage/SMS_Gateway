package com.example.smsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class SmsGatewayApplication {
	public static void main(String[] args) throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(8082)) {
			System.out.println("Gateway Server running on port 8082...");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String message;
				while ((message = reader.readLine()) != null) {
					System.out.println("Received: " + message);
				}
			}
		}
	}
}
