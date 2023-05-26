package Personaggi;

public class Orco extends Inumano {

    /* Gli orchi saranno più forti in attacco e difesa rispetto agli altri, ma
       avranno una statistica in agilità più bassa*/
    public Orco(Giocatore giocatore) {
        setTipo("Orco");
        setNome();
        setPuntiVita(generaPuntiVita(giocatore.getLivello(), 8, 20));
        setPuntiAttacco(generaPuntiAttacco(giocatore.getPuntiAttacco()));
        setPuntiDifesa(generaPuntiDifesa(giocatore.getPuntiDifesa()));
        setPuntiAgilità(generaPuntiAgilità(giocatore.getPuntiAgilità()));
        setDropEsperienza(generaDropEsperienza());
    }

    @Override
    protected int generaPuntiAttacco(int puntiAttaccoGiocatore) {
        puntiAttaccoGiocatore += random.nextInt(3); // Aumenta l'attacco degli orchi, perché sono più forti in attacco
        return super.generaPuntiAttacco(puntiAttaccoGiocatore);
    }

    @Override
    protected int generaPuntiDifesa(int puntiDifesaGiocatore) {
        puntiDifesaGiocatore += random.nextInt(2); // Aumenta la difesa degli orchi, perché sono più forti in difesa
        return super.generaPuntiDifesa(puntiDifesaGiocatore);
    }

    @Override
    protected int generaPuntiAgilità(int puntiAgilitàGiocatore) {
        puntiAgilitàGiocatore -= random.nextInt(7); // Gli orchi sono meno agili
        return super.generaPuntiAgilità(puntiAgilitàGiocatore);
    }
}

