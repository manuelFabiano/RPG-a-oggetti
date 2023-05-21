public abstract class Nemico {
    private String nome;
    private int puntiVita;

    public Nemico(int puntiVita) {
        this.nome = NomeNemico.getNomeRandom();
        this.puntiVita = puntiVita;
    }
}
