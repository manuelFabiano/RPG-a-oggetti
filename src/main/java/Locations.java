import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Locations {
    private String nome;
    private String descrizione;

    // Costruttore
    public Locations(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    // Metodi getter
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

}
