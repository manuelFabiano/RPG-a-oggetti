package Personaggi;


public class Goblin extends Inumano{

    /*I nemici goblin saranno più deboli in attacco e in difesa rispetto agli altri, ma
      avranno una statistica in Agilità più alta, quindi spesso attaccheranno prima */
    public Goblin(Giocatore giocatore) {
        setTipo("Goblin");
        setNome();
        //stats
        setPuntiVita(generaPuntiVita(giocatore.getLivello(), 6, 18));
        setPuntiAttacco(generaPuntiAttacco(giocatore.getPuntiAttacco()));
        setPuntiDifesa(generaPuntiDifesa(giocatore.getPuntiDifesa()));
        setPuntiAttacco(generaPuntiAgilità(giocatore.getPuntiAgilità()));
        //drop esperienza
        setDropEsperienza(generaDropEsperienza());
        //drop oggetti
        setDropOggetto(generaDrop());
        setQuantitaDrop(random.nextInt(2)+1);
    }

    @Override
    protected int generaPuntiAttacco(int puntiAttaccoGiocatore) {
        puntiAttaccoGiocatore -= random.nextInt(4); //Diminuisce ulteriormente l'attacco dei goblin, perchè sono più scarsi in attacco
        return super.generaPuntiAttacco(puntiAttaccoGiocatore);
    }

    @Override
    protected int generaPuntiDifesa(int puntiDifesaGiocatore) {
        puntiDifesaGiocatore -= random.nextInt(7); //Diminuisce ulteriormente l'attacco dei goblin, perchè sono più scarsi in difesa
        return super.generaPuntiDifesa(puntiDifesaGiocatore);
    }

    @Override
    protected int generaPuntiAgilità(int puntiAgilitàGiocatore) {
        puntiAgilitàGiocatore += random.nextInt(5); //I goblin sono più agili
        return super.generaPuntiAgilità(puntiAgilitàGiocatore);
    }


}
