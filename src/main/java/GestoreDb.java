import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

public class GestoreDb {
    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public GestoreDb(String uri, String databaseName, String collectionName) {
        client = MongoClients.create(uri);
        database = client.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
        // Crea un'indicizzazione unica sul campo "username"
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(new Document("username", 1), indexOptions);
        System.out.println("Connessione al database effettuata.");
    }

    public int registraUtente(String username, String password) {
        Document doc = new Document("username", username)
                .append("password", password);

        try {
            collection.insertOne(doc);
            System.out.println("Utente inserito nel database.");
            return 1;
        } catch (MongoWriteException e) {
            System.err.println("Errore durante l'inserimento dell'utente: il documento esiste già nel database.");
            e.printStackTrace();
            return 2;
        } catch (MongoException e) {
            System.err.println("Errore generico di MongoDB durante l'inserimento dell'utente.");
            e.printStackTrace();
            return -1;
        }
    }

    public boolean login(String username, String password) {
        try{
            Document utente = collection.find(Filters.eq("username", username)).first();

            if (utente != null && utente.getString("password").equals(password)) {
                System.out.println("Accesso effettuato con successo!");
                return true;
            } else {
                System.out.println("Username o password non validi.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Si è verificato un errore durante il login:");
            e.printStackTrace();
            return false;
        }
    }


    public void disconnetti() {
        client.close();
        System.out.println("Connessione al database chiusa.");
    }
}

