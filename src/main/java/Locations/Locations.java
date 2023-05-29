package Locations;

import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;

public abstract class Locations {
    private InterfacciaGestoreClient gestoreClient;
    private String descrizione;
    public String getDescrizione() {
        return descrizione;
    }

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }

    public void setGestoreClient(InterfacciaGestoreClient gestoreClient) {
        this.gestoreClient = gestoreClient;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public abstract void esplora(Gioco gioco) throws IOException;
}


