package Oggetti;

public class Arma extends Oggetto{
    private int danniBase;
    private String abilita;

    public Arma(String nome, String descrizione, int danniBase, String abilita) {
        super(nome, descrizione);
        this.danniBase = danniBase;
        this.abilita = abilita;
    }

    public int getDanniBase() {
        return danniBase;
    }

    public void setDanniBase(int danniBase) {
        this.danniBase = danniBase;
    }

    public String getAbilita() {
        return abilita;
    }

    public void setAbilita(String abilita) {
        this.abilita = abilita;
    }

    //Stampa la descrizione completa dell'arma
    public String getArma(){
        return " - Nome: " + getNome() + "\n - Descrizione: " + getDescrizione() + "\n - Danni di base: " + getDanniBase() + "\n - Abilità: " + getAbilita();
    }
}
