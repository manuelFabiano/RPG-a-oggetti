package Personaggi;


public class Goblin extends Inumano{

    /*I nemici goblin saranno più deboli in attacco e in difesa rispetto agli altri,
     ma avranno una statistica in Agilità più alta, quindi spesso
     attaccheranno prima */
    public Goblin(int livelloGiocatore) {
        //Il costruttore della superclasse setta tutti i drop randomici,
        // il tipo e il nome
        super("Goblin", livelloGiocatore);
        //HP
        setPuntiVita(generaPuntiVita(livelloGiocatore, 6, 18));
    }

    @Override
    protected int generaPuntiAttacco(int livelloGiocatore) {
        int puntiBase = 1 + (random.nextInt(4)+1); //da 2 a 5 atk
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiDifesa(int livelloGiocatore) {
        int puntiBase = 1 + (random.nextInt(4)+1); //da 2 a 5 dfs
        return puntiBase + livelloGiocatore;
    }

    @Override
    protected int generaPuntiAgilità(int livelloGiocatore) {
        int puntiBase = 10 + (random.nextInt(4)-2); //da 8 a 12 agl
        return puntiBase + livelloGiocatore;
    }

    @Override
    public String getFrase() {
        return getNome() + ": Ihihihih, non vedo l'ora di mangiare la tua carne Ihihihihih!";
    }
}
