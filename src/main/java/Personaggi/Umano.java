package Personaggi;

import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;

public abstract class Umano extends Nemico{

    private InterfacciaGestoreClient gestoreClient;

    public Umano(InterfacciaGestoreClient gestoreClient, String tipo, int livelloGiocatore) {
        super(tipo, livelloGiocatore);
        this.gestoreClient = gestoreClient;
    }

    public abstract void interagisci(Gioco gioco) throws IOException;

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }
}
