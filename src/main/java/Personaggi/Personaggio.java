package Personaggi;

public abstract class Personaggio {
    private String nome;

    public Personaggio() {
        setNome();
    }

    public void setNome() {
        this.nome = Nomi.getNomeRandom();
    }

    public String getNome() {
        return nome;
    }

}
