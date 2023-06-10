package Personaggi;

public class Strega extends Inumano{
    /* I nemici stregone sono abili nell'utilizzo della magia e possiedono una grande forza mentale,
   ma sono fisicamente più deboli rispetto ad altri mostri */

    public Strega(int livelloGiocatore) {
        // Il costruttore della superclasse setta tutti i drop randomici, il tipo e il nome
        super("Strega", livelloGiocatore);
        setQuantitaDrop(1);
        // HP
        setPuntiVita(generaPuntiVita(livelloGiocatore, 10, 20));
    }

    @Override
    protected int generaPuntiAttacco(int livelloGiocatore) {
        int puntiBase = 4 + (random.nextInt(4) + 1); // da 5 a 8 atk
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiDifesa(int livelloGiocatore) {
        int puntiBase = random.nextInt(4) + 1; // da 0 a 4 dfs
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiAgilità(int livelloGiocatore) {
        int puntiBase = random.nextInt(4)+2; // da 2 a 5 agl
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected String generaDrop() {
        int numeroRandom = random.nextInt(5);
        switch (numeroRandom) {
            case 0:
                return "pozione curativa";
            case 1:
                return "superpozione curativa";
            case 2:
                return "pozione di attacco";
            case 3:
                return "pozione di difesa";
            case 4:
                return "pozione di fuoco";
        }
        return null;
    }

    @Override
    public String getFrase() {
        return getNome() + ": I miei incantesimi ti distruggeranno senza pietà!";
    }

}
