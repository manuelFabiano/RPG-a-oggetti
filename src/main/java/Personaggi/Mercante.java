package Personaggi;

import Oggetti.Oggetto;
import Server.Gioco;

import java.io.IOException;
import java.util.*;


public class Mercante extends Personaggio {
    private Map<Oggetto, Integer> inventario;
    private Gioco gioco;
    Random random = new Random();

    public Mercante(Gioco gioco) {
        this.gioco = gioco;
        setNome();
        inventario = generaInventarioCasuale();
        setGestoreClient(gioco.getGestoreClient());
    }

    public void interagisci() throws IOException {
        while(true) {
            getGestoreClient().manda("Mercante: Benvenuto nel mio negozio! Ecco cosa ho in vendita:");
            for (Map.Entry<Oggetto, Integer> entry : inventario.entrySet()) {
                Oggetto oggetto = entry.getKey();
                String nome = oggetto.getNome();
                int quantita = entry.getValue();
                int prezzo = oggetto.getPrezzo();

                getGestoreClient().manda(nome + " - Quantità disponibile: " + quantita + " - Prezzo: " + prezzo + " monete");
            }

            getGestoreClient().manda("Cosa desideri acquistare? Inserisci il nome dell'oggetto o premi invio per uscire.\nPASS");

            // Leggi l'input dell'utente
            String scelta = getGestoreClient().ricevi();

            if (scelta.isEmpty()) {
                getGestoreClient().manda("Mercante: Arrivederci!");
                break;
            }

            Oggetto oggetto = verificaDisponibilita(scelta);
            if(oggetto != null){
                vendiOggetto(gioco.getGiocatore(), oggetto);
            }
        }
    }

    private Map<Oggetto, Integer> generaInventarioCasuale() {
        Map<Oggetto, Integer> inventario = new HashMap<>();
        String[] nomiOggetti = {"mela", "banana", "pane", "pollo", "cioccolato", "manzo"};


        // Genera un numero casuale di oggetti presenti nell'inventario
        int numOggetti = random.nextInt(nomiOggetti.length) + 1;

        // Seleziona casualmente un sottoinsieme di oggetti dall'array nomiOggetti
        List<String> oggettiDisponibili = Arrays.asList(nomiOggetti);
        Collections.shuffle(oggettiDisponibili);
        oggettiDisponibili = oggettiDisponibili.subList(0, numOggetti);

        for (String nomeOggetto : oggettiDisponibili) {
            int quantita = random.nextInt(4) + 1; // Quantità casuale da 1 a 4
            int prezzo = generaPrezzo(nomeOggetto);
            Oggetto oggetto = new Oggetto(nomeOggetto, prezzo);
            inventario.put(oggetto, quantita);
        }

        return inventario;
    }

    private void vendiOggetto(Giocatore giocatore, Oggetto oggetto) {
        int quantita = inventario.get(oggetto);
        int prezzo = oggetto.getPrezzo();
        if (giocatore.getSoldi() >= prezzo) {
            //Aggiungo l'oggetto all'inventario del giocatore
            gioco.getGestoreDb().incrementaQuantita(gioco,oggetto.getNome().toLowerCase(),1);
            //tolgo i soldi al giocatore
            giocatore.decrementaSoldi(prezzo);
            //tolgo l'oggetto al mercante
            inventario.put(oggetto, quantita - 1);
            getGestoreClient().manda("Hai acquistato un " + oggetto.getNome());
        } else {
            getGestoreClient().manda("Non hai abbastanza soldi!");
        }
    }

    private Oggetto verificaDisponibilita(String nomeOggetto) {
        for (Map.Entry<Oggetto, Integer> entry : inventario.entrySet()) {
            Oggetto oggetto = entry.getKey();
            int quantita = entry.getValue();

            if (oggetto.getNome().equalsIgnoreCase(nomeOggetto) && quantita > 0) {
                return oggetto; // Oggetto trovato nell'inventario con quantità > 0
            }
        }
        return null; // Oggetto non presente nell'inventario o con quantità <= 0

    }


    private int generaPrezzo(String nomeOggetto){
        switch (nomeOggetto){
            case "mela":
                return random.nextInt(2)+1;
            case "banana":
                return random.nextInt(3)+1;
            case "pane":
                return random.nextInt(4)+1;
            case "pollo":
                return 4 + random.nextInt(6)+1;
            case "manzo":
                return 6 + random.nextInt(6)+1;
            case "cioccolato":
                return 2 + random.nextInt(4)+1;
            default:
                return 0;
        }
    }

}
