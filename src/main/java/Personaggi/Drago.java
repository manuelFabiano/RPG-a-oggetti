package Personaggi;

public class Drago extends Inumano{
    /* I nemici drago sono potenti e temibili, con un'abilità di attacco e difesa eccezionale,
   ma la loro agilità è più limitata rispetto ad altri mostri */

    public Drago(int livelloGiocatore) {
        // Il costruttore della superclasse setta tutti i drop randomici, il tipo e il nome
        super("Drago", livelloGiocatore);
        // HP
        setPuntiVita(generaPuntiVita(livelloGiocatore, 20, 25));
    }

    @Override
    protected int generaPuntiAttacco(int livelloGiocatore) {
        int puntiBase = 8 + (random.nextInt(4) + 1); // da 8 a 12 atk
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiDifesa(int livelloGiocatore) {
        int puntiBase = 6 + (random.nextInt(4) + 1); // da 6 a 10 dfs
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiAgilità(int livelloGiocatore) {
        int puntiBase = (random.nextInt(4) - 2); // da -2 a 2 agl
        return puntiBase + livelloGiocatore;
    }

    @Override
    public String getFrase() {
        return getNome() + ": Trema mortale, davanti al mio potere divino!";
    }

}
