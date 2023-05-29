import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TerminalClientGUI extends Application {

    private PrintWriter output;
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RPG a oggetti");

        TextField inputField = new TextField();


        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                outputArea.clear();
                String clientResponse = inputField.getText();
                sendMessageToServer(clientResponse);
                inputField.clear();
                event.consume();
            }
        });

        outputArea = new TextArea();
        outputArea.setEditable(false);

        VBox vbox = new VBox(outputArea, inputField);
        vbox.setVgrow(outputArea, Priority.ALWAYS);

        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> closeConnection());
        primaryStage.show();

        // Avvia la connessione al server
        connectToServer();
    }

    private void connectToServer() {
        String serverAddress = "localhost"; // Indirizzo IP del server
        int portNumber = 1234; // Porta del server

        try {
            Socket socket = new Socket(serverAddress, portNumber);
            output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        updateOutputArea(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Connessione al server riuscita...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer(String message) {
        output.println(message);
    }

    private void updateOutputArea(String text) {
        Platform.runLater(() -> outputArea.appendText(text + "\n"));
    }

    private void closeConnection() {
        output.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
