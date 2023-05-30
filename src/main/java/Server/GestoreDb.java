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

    public String nuovaPartita(Document utente, Giocatore giocatore, Gioco gioco){
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
                .append("maxPuntiVita", giocatore.getMaxPuntiVita())
                .append("puntiAttacco", giocatore.getPuntiAttacco())
                .append("puntiDifesa", giocatore.getPuntiDifesa())
                .append("puntiAgilità", giocatore.getPuntiAgilità())
                .append("livello", giocatore.getLivello())
                .append("esperienza", giocatore.getEsperienza())
                .append("arma", new Document("nome",giocatore.getArma().getNome())
                        .append("descrizione", giocatore.getArma().getDescrizione())
                        .append("danniBase", giocatore.getArma().getDanniBase())
                        .append("abilita", giocatore.getArma().getAbilita()))
                .append("inventario", creaInventarioVuoto());
        //Aggiorno il database
        Document filtro = new Document("_id", utente.getObjectId("_id"));
        Document aggiornamento = new Document("$set", new Document("partita"+(conteggio+1), partita));
        playerscollection.updateOne(filtro, aggiornamento);
        //Aggiorno utente
        gioco.setUtente(aggiornaUtente(utente));
        return "partita"+(conteggio+1);
    }

    public void salvaPartita(Document utente, Giocatore giocatore, Gioco gioco) {
        // Preparo il documento da inserire
        Document partita = new Document("roundCorrente", gioco.getRoundCorrente())
                .append("puntiVita", giocatore.getPuntiVita())
                .append("maxPuntiVita", giocatore.getMaxPuntiVita())
                .append("puntiAttacco", giocatore.getPuntiAttacco())
                .append("puntiDifesa", giocatore.getPuntiDifesa())
                .append("puntiAgilità", giocatore.getPuntiAgilità())
                .append("livello", giocatore.getLivello())
                .append("esperienza", giocatore.getEsperienza())
                .append("arma", new Document("nome", giocatore.getArma().getNome())
                        .append("descrizione", giocatore.getArma().getDescrizione())
                        .append("danniBase", giocatore.getArma().getDanniBase())
                        .append("abilita", giocatore.getArma().getAbilita()))
                .append("inventario", getInventario(utente, gioco.getChiavePartita()));

        Document filtro = new Document("_id", utente.getObjectId("_id"));
        Document aggiornamento = new Document("$set", new Document(gioco.getChiavePartita(), partita));
        playerscollection.updateOne(filtro, aggiornamento);
        //Aggiorno utente
        gioco.setUtente(aggiornaUtente(utente));
        String documentoJSON = gioco.getUtente().toJson();
        String documentoFormattato = documentoJSON.toString();
        System.out.println(documentoFormattato);
    }


    public String stampaPartite(Document utente){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : utente.entrySet()) {
            String chiave = entry.getKey();
            if (chiave.contains("partita")) {
                Document partita = utente.get(chiave, Document.class);
                result.append(chiave + ": round " + partita.getInteger("roundCorrente") + " - livello " + partita.getInteger("livello") + "\n" );
            }
        }
        return result.toString();
    }

    public Document trovaPartita(Document utente, String chiave) {
        if (utente.containsKey(chiave)) {
            return utente.get(chiave, Document.class);
        }
        return null;
    }

    public Document getInventario(Document utente, String chiave) {
        System.out.println(chiave);

        String documentoJSON = utente.toJson();
        String documentoFormattato = documentoJSON.toString();
        System.out.println(documentoFormattato);

        Document partita = utente.get(chiave, Document.class);
        if (partita != null) {
            System.out.println("test");
            return partita.get("inventario", Document.class);
        }

        return null; // La partita specificata non esiste nel documento Utente
    }

    public void disconnetti() {
        client.close();
        System.out.println("Connessione al database chiusa.");
    }

    private Document creaInventarioVuoto(){
        Document inventario = new Document("mela", creaCibo("Mela", "Una mela rossa" , 2,0))
                .append("pane", creaCibo("Pane", "Una fetta di pane croccante", 3, 0))
                .append("cioccolato", creaCibo("Cioccolato", "Una barretta di cioccolato", 4, 0))
                .append("banana", creaCibo("Banana", "Una banana gialla", 3, 0))
                .append("pollo", creaCibo("Pollo", "Un pezzo di pollo arrosto", 5, 0))
                .append("manzo", creaCibo("Manzo", "Un gustoso pezzo di manzo", 6, 0));
        return inventario;
    }

    public void incrementaQuantita(Document utente, String chiavePartita, String chiaveOggetto, int incremento) {
        Document inventario = getInventario(utente,chiavePartita);
        if (inventario != null) {
            Document oggetto = inventario.get(chiaveOggetto, Document.class);
            if (oggetto != null) {
                int quantita = oggetto.getInteger("quantita", 0);
                int nuovaQuantita = quantita + incremento;
                oggetto.put("quantita", nuovaQuantita);
            }
        }
    }

    public Document aggiornaUtente(Document utente){
        Document aggiornamento = playerscollection.find(Filters.eq("_id", utente.getObjectId("_id"))).first();
        return aggiornamento;
    }


    private Document creaCibo(String nome, String descrizione, int puntiVita, int quantita ){
        Document oggetto = new Document("nome", nome)
                .append("descrizione", descrizione)
                .append("puntiVita", puntiVita)
                .append("quantita", quantita);
        return oggetto;
    }

}

