package com.example.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileUploader extends Application {

    private ListView<File> fileListView;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        fileListView = new ListView<>();
        Button selectButton = new Button("Select Files");
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Files to Upload");
                List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
                if (selectedFiles != null) {
                    fileListView.getItems().addAll(selectedFiles);
                }
            }
        });

        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydatabase", "postgres", "Rith1213")) {
                    String insertSql = "INSERT INTO files (name, content) VALUES (?, ?)";
                    PreparedStatement ps = conn.prepareStatement(insertSql);
                    for (File file : fileListView.getItems()) {
                        byte[] fileContent = Files.readAllBytes(file.toPath());
                        ps.setString(1, file.getName());
                        ps.setBytes(2, fileContent);
                        ps.executeUpdate();
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button saveButton = new Button("Save File Names");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"))) {
                    int index = 1;
                    for (File file : fileListView.getItems()) {
                        String fileName = String.format("%09d%s", index++, file.getName());
                        writer.write(fileName);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox hbox  = new HBox(10);
        hbox.getChildren().addAll(new Label(), fileListView, selectButton, uploadButton, saveButton);
        hbox.setPadding(new Insets(10));
        root.setTop(hbox);
        root.setCenter(fileListView);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
