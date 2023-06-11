package Server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static String url;
    private static String databaseName;
    private static String collectionName;
    private static int port;

    public static void main(String[] args){
        try {
            caricaXML("config.xml");
            GestoreDb gestoreDb = new GestoreDb(url, databaseName, collectionName);

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server avviato. In attesa di connessioni...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nuova connessione da: " + clientSocket.getInetAddress());

                    // Crea un nuovo thread per gestire la connessione del client
                    GestoreClient gestoreClient = new GestoreClient(clientSocket, gestoreDb);
                    Thread thread = new Thread(gestoreClient);
                    thread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void caricaXML(String uri)throws Exception {
        // Carico il file di configurazione XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(uri);

        // Ottengo l'elemento radice
        Element root = document.getDocumentElement();

        // Ottengo l'elemento "database"
        Element databaseElement = (Element) root.getElementsByTagName("database").item(0);

        // Ottengo le informazioni di connessione al database
        url = databaseElement.getElementsByTagName("url").item(0).getTextContent();
        databaseName = databaseElement.getElementsByTagName("databaseName").item(0).getTextContent();
        collectionName = databaseElement.getElementsByTagName("collectionName").item(0).getTextContent();

        // Ottengo l'elemento "server"
        Element serverElement = (Element) root.getElementsByTagName("server").item(0);

        port = Integer.parseInt(serverElement.getElementsByTagName("port").item(0).getTextContent());
    }
}
