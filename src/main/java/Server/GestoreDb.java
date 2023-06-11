package Server;

import Oggetti.Cibo;
import Oggetti.Pozione;
import Personaggi.Giocatore;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class GestoreDb {
    private MongoCollection<Document> playerscollection;

    public GestoreDb(String uri, String databaseName, String collectionName) {
        try {
            MongoClient client = MongoClients.create(uri);
            MongoDatabase database = client.getDatabase(databaseName);
            playerscollection = database.getCollection(collectionName);

            // Crea un'indicizzazione unica sul campo "username"
            IndexOptions indexOptions = new IndexOptions().unique(true);
            playerscollection.createIndex(new Document("username", 1), indexOptions);

            System.out.println("Connessione al database effettuata.");
        } catch (MongoException e) {
            System.err.println("Errore durante la connessione al database: " + e.getMessage());
        }
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

    public Document login(String username, String password){
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
                .append("soldi", giocatore.getSoldi())
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
                .append("soldi", giocatore.getSoldi())
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
    }


    public String stampaPartite(Document utente){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : utente.entrySet()) {
            String chiave = entry.getKey();
            if (chiave.contains("partita")) {
                Document partita = utente.get(chiave, Document.class);
                if(partita.getInteger("puntiVita")>0)
                    result.append(chiave).append(": round ").append(partita.getInteger("roundCorrente")).append(" - livello ").append(partita.getInteger("livello")).append("\n");
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
        Document partita = utente.get(chiave, Document.class);
        if (partita != null) {
            return partita.get("inventario", Document.class);
        }

        return null; // La partita specificata non esiste nel documento Utente
    }

    public String getInventarioStampabile(Document inventario){
        StringBuilder sb = new StringBuilder();
        for (String oggetto : inventario.keySet()) {
            Document documentOggetto = (Document) inventario.get(oggetto);
            int quantita = documentOggetto.getInteger("quantita", 0);
            if (quantita > 0) {
                sb.append(documentOggetto.getString("nome")).append(": ").append(quantita).append("\n");
            }
        }
        return sb.toString();
    }


    private Document creaInventarioVuoto(){
        return new Document("mela", creaCibo("Mela", "Una mela rossa" , 2))
                .append("pane", creaCibo("Pane", "Una fetta di pane croccante", 3))
                .append("cioccolato", creaCibo("Cioccolato", "Una barretta di cioccolato", 4))
                .append("banana", creaCibo("Banana", "Una banana gialla", 3))
                .append("pollo", creaCibo("Pollo", "Un pezzo di pollo arrosto", 5))
                .append("manzo", creaCibo("Manzo", "Un gustoso pezzo di manzo", 6))
                .append("pozione curativa", creaPozione("Pozione curativa", "Una pozione di colore rosso", "Cura", 10))
                .append("superpozione curativa", creaPozione("Superpozione curativa", "Una grande pozione di colore rosso", "Cura", 20))
                .append("pozione di attacco", creaPozione("Pozione di attacco", "Una pozione di colore verde", "Attacco", 10))
                .append("pozione di difesa", creaPozione("Pozione di difesa", "Una pozione di colore blu", "Difesa", 10))
                .append("pozione di fuoco", creaPozione("Pozione di fuoco", "Una pozione infuocata", "Fuoco", 0));
    }

    public void incrementaQuantita(Gioco gioco, String chiaveOggetto, int incremento) {
        Document inventario = getInventario(gioco.getUtente(), gioco.getChiavePartita());
        if (inventario != null) {
            Document oggetto = inventario.get(chiaveOggetto, Document.class);
            if (oggetto != null) {
                int quantita = oggetto.getInteger("quantita", 0);
                int nuovaQuantita = quantita + incremento;
                oggetto.put("quantita", nuovaQuantita);
            }
        }
    }

    public boolean decrementaQuantita(Gioco gioco, String chiaveOggetto) {
        Document inventario = getInventario(gioco.getUtente(), gioco.getChiavePartita());
        if (inventario != null) {
            Document oggetto = inventario.get(chiaveOggetto, Document.class);
            if (oggetto != null) {
                int quantita = oggetto.getInteger("quantita", 0);
                if (quantita > 0) {
                    int nuovaQuantita = quantita - 1;
                    oggetto.put("quantita", nuovaQuantita);
                    return true;
                }
            }
        }
        return false;
    }

    public Document aggiornaUtente(Document utente){
        return playerscollection.find(Filters.eq("_id", utente.getObjectId("_id"))).first();
    }


    private Document creaCibo(String nome, String descrizione, int puntiVita ){
        return new Document("nome", nome)
                .append("descrizione", descrizione)
                .append("puntiVita", puntiVita)
                .append("quantita", 0);
    }
    private Document creaPozione(String nome, String descrizione, String tipo, int punti ){
        return new Document("nome", nome)
                .append("descrizione", descrizione)
                .append("tipo", tipo)
                .append("punti", punti)
                .append("quantita", 0);
    }

    public Cibo getCibo(Document inventario, String chiaveCibo){
        Document documentCibo = (Document) inventario.get(chiaveCibo);
        return new Cibo(documentCibo.getString("nome"), documentCibo.getString("descrizione"), documentCibo.getInteger("puntiVita"));
    }
    public Pozione getPozione(Document inventario, String chiavePozione){
        Document documentPozione = (Document) inventario.get(chiavePozione);
        return new Pozione(documentPozione.getString("nome"), documentPozione.getString("descrizione"), documentPozione.getString("tipo"), documentPozione.getInteger("punti"));
    }

    //METODI PER LA CLASSIFICA:
    public List<Document> generaClassifica() {
        List<Document> classifica = new ArrayList<>();

        // Ottieni tutti gli utenti dalla collezione
        for (Document document : playerscollection.find()) {
            String username = document.getString("username");

            // Scansiona tutte le partite dell'utente
            for (String key : document.keySet()) {
                if (key.startsWith("partita")) {
                    Document partitaObject = document.get(key, Document.class);
                    int round = partitaObject.getInteger("roundCorrente");
                    int livello = partitaObject.getInteger("livello");

                    // Aggiungi la partita alla classifica
                    Document partita = new Document();
                    partita.append("username", username)
                            .append("round", round)
                            .append("livello", livello);
                    classifica.add(partita);
                }
            }
        }

        // Ordina la classifica in base al round (in ordine decrescente)
        classifica.sort(Comparator.comparingInt((Document p) -> p.getInteger("round")).reversed());

        return classifica;
    }

    public String stampaClassifica(List<Document> classifica){
        StringBuilder classificaStampabile = new StringBuilder();
        for (int i = 0; i < classifica.size(); i++) {
            Document partita = classifica.get(i);
            String username = partita.getString("username");
            int round = partita.getInteger("round");
            int livello = partita.getInteger("livello");

            String posizioneString = "Posizione " + (i + 1) + ": ";
            String partitaString = username + " - Round: " + round + ", Livello: " + livello;

            classificaStampabile.append(posizioneString).append(partitaString).append("\n");
        }

        return classificaStampabile.toString();
    }

}

