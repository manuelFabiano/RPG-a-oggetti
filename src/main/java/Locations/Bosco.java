package Locations;

public class Bosco extends Locations {

    public Bosco(String nome, String descrizione) {
        super(nome, descrizione);
    }

    @Override
    public String getNome() {
        return "Bosco";
    }
    @Override
    public String getDescrizione() {
        return "Ti trovi in un bosco tetro.";
    }
}