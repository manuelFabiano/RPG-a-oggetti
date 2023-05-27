package Locations;

import Server.InterfacciaGestoreClient;

public class Bosco extends Locations {

    public Bosco(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Ti ritrovi di fronte a un bosco tetro e spaventoso.\n" +
                "L'aria è densa di un odore terroso e umido, mescolato con un vago sentore di decomposizione.\n" +
                "Ogni tanto, un ramo si spezza improvvisamente, rompendo il silenzio e facendoti sobbalzare.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
    }

    @Override
    public void esplora() {

    }
}