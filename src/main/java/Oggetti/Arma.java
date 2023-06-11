package Oggetti;

public class Arma extends Oggetto{
    private final int danniBase;
    private final String abilita;

    public Arma(String nome, String descrizione, int danniBase, String abilita) {
        super(nome, descrizione);
        this.danniBase = danniBase;
        this.abilita = abilita;
    }
    public int getDanniBase() {
        return danniBase;
    }
    public String getAbilita() {
        return abilita;
    }

    /**
     * Restituisce una stringa con la descrizione completa dell'arma
     */
    public String getArma(){
        return " - Nome: " + getNome() + "\n - Descrizione: " + getDescrizione() + "\n - Danni di base: " + getDanniBase() + "\n - Abilità: " + getAbilita();
    }
}
