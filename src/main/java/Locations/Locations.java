package Locations;

import Oggetti.Arma;
import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;

public abstract class Locations {
    private InterfacciaGestoreClient gestoreClient;
    private String descrizione;



    public String getDescrizione() { return descrizione;}

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }

    public void setGestoreClient(InterfacciaGestoreClient gestoreClient) {
        this.gestoreClient = gestoreClient;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Ogni sottoclasse di Locations deve implemetare esplora, che gestisce l'interazione dell'utente con la location
     */
    public abstract void esplora(Gioco gioco) throws IOException;

    /**
     * creArma restituisce un oggetto Arma (sempre diversa) trovabile all'interno della location che implementa il metodo
     */
    protected abstract Arma creaArma();
}


