package Personaggi;

public class Orco extends Inumano {

    /* Gli orchi saranno più forti in attacco e difesa rispetto agli altri, ma
       avranno una statistica in agilità più bassa*/
    public Orco(int livelloGiocatore) {
        super("Orco", livelloGiocatore);
        setPuntiVita(generaPuntiVita(livelloGiocatore, 8, 20));
    }

    @Override
    protected int generaPuntiAttacco(int livelloGiocatore) {
        int puntiBase = 4 + (random.nextInt(4)+1); //da 5 a 8 atk
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiDifesa(int livelloGiocatore) {
        int puntiBase = 2 + (random.nextInt(4)+1); //da 3 a 6 dfs
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiAgilità(int livelloGiocatore) {
        int puntiBase = 2 + (random.nextInt(4)-2); //da 0 a 4 agl
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaQuantitaDrop() {
        return random.nextInt(5)+1;
    }

    @Override
    public String getFrase() {
        return getNome() + ": Muahahahaha! Ti ridurrò in poltiglia!";
    }
}

