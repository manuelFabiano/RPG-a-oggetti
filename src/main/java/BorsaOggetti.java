import java.util.ArrayList;
import java.util.List;

public class BorsaOggetti {
    private List<Oggetto> oggetti;

    public BorsaOggetti() {
        oggetti = new ArrayList<>();
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public void rimuoviOggetto(Oggetto oggetto) {
        oggetti.remove(oggetto);
    }

    public void stampaContenuto() {
        for (Oggetto oggetto : oggetti) {
            System.out.println("Oggetto: " + oggetto.getNome() + ", Peso: " + oggetto.getQuantita());
        }
    }
}

