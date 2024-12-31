package com.example.smsreceiving;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@SpringBootApplication
public class SmsReceivingApplication {

	public static void main(String[] args) throws Exception {

		try (Socket socket = new Socket("localhost", 8082)) {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			String message = "Test Message from Receiving Server by Nadil Liyanage BOC";
			writer.println(message);
			//inserting to the log file
			LogFile.info("Err |            |                 |" + message);

		}
	}
}

