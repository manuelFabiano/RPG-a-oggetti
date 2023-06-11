import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class TerminalClientGUI extends Application {

    private PrintWriter output;
    private TextArea outputArea;
    private Label hpLabel;
    private Label levelLabel;
    private Label roundLabel;

    private final Semaphore inputSemaphore = new Semaphore(0);

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
        outputArea.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-text-fill: #333333; ");

        hpLabel = new Label("HP: -");
        levelLabel = new Label("Livello: -");
        roundLabel = new Label("Round: -");

        HBox labelsBox = new HBox(hpLabel, levelLabel, roundLabel);
        labelsBox.setAlignment(Pos.CENTER);
        labelsBox.setSpacing(10);

        VBox vbox = new VBox(labelsBox, outputArea,  inputField);
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 800, 400);

        // Applica stili CSS alle label
        hpLabel.getStyleClass().add("status-label");
        levelLabel.getStyleClass().add("status-label");
        roundLabel.getStyleClass().add("status-label");

        // Applica stili CSS alla scena
        scene.getStylesheets().add("styles.css");

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> closeConnection());
        primaryStage.show();
        inputField.requestFocus();

        // Avvia la connessione al server
        connectToServer();
    }

    private static Element caricaXML()throws Exception {
        // Carico il file di configurazione XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse("clientConfig.xml");

        // Ottengo l'elemento radice
        Element root = document.getDocumentElement();

        // Ottengo l'elemento "server"
        return  (Element) root.getElementsByTagName("server").item(0);
    }

    private void connectToServer() {
        try {
            Element serverElement = caricaXML();

            String serverAddress = serverElement.getElementsByTagName("address").item(0).getTextContent(); // Indirizzo IP del server
            int portNumber = Integer.parseInt(serverElement.getElementsByTagName("port").item(0).getTextContent()); // Porta del server

            Socket socket = new Socket(serverAddress, portNumber);
            output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        if (serverMessage.equals("PASS")) {
                            inputSemaphore.release();
                        } else if (serverMessage.equals("CLOSE")) {
                            closeConnection();
                            break; // Esci dal ciclo
                        } else {
                            updateOutputArea(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    closeConnection();
                }
            }).start();

            System.out.println("Connessione al server riuscita...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer(String message) {
        try {
            inputSemaphore.acquire(); // Attendere il rilascio del semaforo prima di inviare l'input
            output.println(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateOutputArea(String text) {
        if (!text.startsWith("HP:") && !text.startsWith("Livello:") && !text.startsWith("Round:")) {
            if (!text.equals("PASS\n")) {
                Platform.runLater(() -> outputArea.appendText(text + "\n"));
            }
        } else {
        // Aggiorna i valori delle label HP, Livello e Round
        Platform.runLater(() -> updateLabels(text));
    }
    }

    private void updateLabels(String text) {
        if (text.startsWith("HP:")) {
            String[] hpParts = text.split("/");
            if (hpParts.length == 2) {
                String hpValue = hpParts[0].substring(3);
                String maxHpValue = hpParts[1];
                Platform.runLater(() -> hpLabel.setText("HP: " + hpValue + "/" + maxHpValue));
            }
        } else if (text.startsWith("Livello:")) {
            String levelValue = text.substring(8);
            Platform.runLater(() -> levelLabel.setText("Livello: " + levelValue));
        } else if (text.startsWith("Round:")) {
            String roundValue = text.substring(7);
            Platform.runLater(() -> roundLabel.setText("Round: " + roundValue));
        }
    }

    private void closeConnection() {
        output.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

