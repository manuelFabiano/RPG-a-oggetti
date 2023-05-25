package Personaggi;

public abstract class Nemico {
    private String nome;
    private int puntiVita;

    public Nemico(int puntiVita) {
        this.nome = Nomi.getNomeRandom();
        this.puntiVita = puntiVita;
    }
}
