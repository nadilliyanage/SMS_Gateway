package com.example.smsreceiving.service;

import com.example.smsreceiving.model.EmailRecord;
import com.example.smsreceiving.repository.EmailRecordRepository;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@Service
public class SmsReceivingService {

    private final EmailRecordRepository repository;

    public SmsReceivingService(EmailRecordRepository repository) {
        this.repository = repository;
    }

    // Fetch records from the database and process PENDING and ERROR records
    public void processRecords() throws Exception {
        List<EmailRecord> pendingRecords = repository.findByStatus("PENDING");
        if (!pendingRecords.isEmpty()) {
            sendToGateway(pendingRecords);
        }

        List<EmailRecord> errorRecords = repository.findByStatus("ERROR");
        if (!errorRecords.isEmpty()) {
            sendToGateway(errorRecords);
        }
    }

    // Send records to the Gateway Server via socket
    private void sendToGateway(List<EmailRecord> records) throws Exception {
        try (Socket socket = new Socket("localhost", 8082)) {
            OutputStream os = socket.getOutputStream();
            for (EmailRecord record : records) {
                String data = record.getId() + "," + record.getMobileNumber() + ","+ record.getEmail() + "," + record.getMessage() + "\n";
                os.write(data.getBytes());
                record.setStatus("PROCESSING");
                repository.save(record);
            }
            os.flush();
        }
    }

    // Update record status based on Gateway response
    public void updateRecordStatus(Long id, String status) {
        EmailRecord record = repository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        record.setStatus(status);
        repository.save(record);
    }
}
