import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

public class GestoreDb {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public void connetti(String uri, String databaseName, String collectionName) {
        client = MongoClients.create(uri);
        database = client.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
        // Crea un'indicizzazione unica sul campo "username"
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(new Document("username", 1), indexOptions);
        System.out.println("Connessione al database effettuata.");
    }

    public void inserisciUtente(String username, String password) {
        Document doc = new Document("username", username)
                .append("password", password);

        try {
            collection.insertOne(doc);
            System.out.println("Utente inserito nel database.");
        } catch (MongoWriteException e) {
            System.err.println("Errore durante l'inserimento dell'utente: il documento esiste già nel database.");
            e.printStackTrace();
        } catch (MongoException e) {
            System.err.println("Errore generico di MongoDB durante l'inserimento dell'utente.");
            e.printStackTrace();
        }
    }

    public void disconnetti() {
        client.close();
        System.out.println("Connessione al database chiusa.");
    }
}

