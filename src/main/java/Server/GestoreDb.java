package Server;

import Personaggi.Giocatore;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.Map;

public class GestoreDb {
    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> playerscollection;

    public GestoreDb(String uri, String databaseName, String collectionName) {
        client = MongoClients.create(uri);
        database = client.getDatabase(databaseName);
        playerscollection = database.getCollection(collectionName);
        // Crea un'indicizzazione unica sul campo "username"
        IndexOptions indexOptions = new IndexOptions().unique(true);
        playerscollection.createIndex(new Document("username", 1), indexOptions);
        System.out.println("Connessione al database effettuata.");
    }

    public int registraUtente(String username, String password) {
        Document doc = new Document("username", username)
                .append("password", password);

        try {
            playerscollection.insertOne(doc);
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

    public Document login(String username, String password) throws Exception{
        Document utente = playerscollection.find(Filters.eq("username", username)).first();

        if (utente != null && utente.getString("password").equals(password)) {
            System.out.println("Accesso effettuato con successo!");
            return utente;
        } else {
            System.out.println("Username o password non validi.");
            return null;
        }
    }

    public void nuovaPartita(Document utente, Giocatore giocatore){
        //Verifico se esistono già partite create dall'utente
        int conteggio = 0;
        for (Map.Entry<String, Object> entry : utente.entrySet()) {
            String chiave = entry.getKey();
            if (chiave.contains("partita")) {
                conteggio++;
            }
        }
        //Preparo il document da inserire
        Document partita = new Document("roundCorrente", 1)
                .append("puntiVita", giocatore.getPuntiVita())
                .append("puntiAttacco", giocatore.getPuntiAttacco())
                .append("puntiDifesa", giocatore.getPuntiDifesa())
                .append("puntiAgilità", giocatore.getPuntiAgilità())
                .append("livello", giocatore.getLivello())
                .append("esperienza", giocatore.getEsperienza());

        //Aggiorno il database
        Document aggiornamento = new Document("$set", new Document("partita"+(conteggio+1), partita));
        playerscollection.updateOne(utente, aggiornamento);


    }


    public void disconnetti() {
        client.close();
        System.out.println("Connessione al database chiusa.");
    }
}

