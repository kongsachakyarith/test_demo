package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/mydatabase";
        String user = "postgres";
        String password = "Rith1213";
        String settlement_date = null;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             BufferedReader br = new BufferedReader(new FileReader("230322_TXNS_9116106.dat"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String record_type = line.substring(0, 4);
                if(record_type.equals("0001")) {
                     settlement_date = line.substring(11,17);
                } else if (record_type.equals("0002")) {
                String card_number = line.substring(4,23);
                String processing_code = line.substring(23,29);
                String transaction_amount = line.substring(29,41);
                String system_trace_number = line.substring(41,47);
                String local_transaction_tme = line.substring(47,53);
                String local_transaction_date = line.substring(53,57);
                settlement_date = line.substring(57,61);
                String merchant_type = line.substring(61,65);
                String acquiring_institution_code = line.substring(65,72);
                String issuer_institution_code = line.substring(72,79);
                String beneficiary_institution_code = line.substring(79,86);
                String acq_Response_code = line.substring(86,88);
                String iss_response_code = line.substring(88,90);
                String bnb_response_code = line.substring(90,92);
                String response_code = line.substring(92,94);
                String card_acceptor_terminal = line.substring(94,102);
                String identification = line.substring(102);
                String transaction_currency_code = line.substring(102,105);
                String message_type_identifier_code = line.substring(105,109);
                String pending_expiry_date = line.substring(109,115);
                String dispute_expiry_date = line.substring(115,121);
                String transaction_status = line.substring(121,122);
                } else if (record_type.equals("0003")) {
                  String a_number_of_records_in_the_file = line.substring(4,13);
                  String checksum_value = line.substring(13,45);
                }
                String sql = "INSERT INTO mytable (record_type, settlement_date) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, record_type);
                stmt.setString(2, settlement_date);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
