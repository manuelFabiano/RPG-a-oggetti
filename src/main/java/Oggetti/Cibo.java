package Oggetti;

public class Cibo extends Oggetto{
    private final int puntiVita;

    public Cibo(String nome, String descrizione, int puntiVita) {
        super(nome, descrizione);
        this.puntiVita = puntiVita;
    }
    public int getPuntiVita() {
        return puntiVita;
    }
}
