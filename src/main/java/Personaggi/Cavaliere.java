package Personaggi;

import Oggetti.Arma;
import Server.Gioco;

import java.io.IOException;
import java.util.Random;

public class Cavaliere extends Umano{
    public Cavaliere(Giocatore giocatore) {
        setTipo("Cavaliere");
        setNome();
        //stats
        setPuntiVita(generaPuntiVita(giocatore.getLivello(), 10, 18));
        setPuntiAttacco(generaPuntiAttacco(giocatore.getPuntiAttacco()));
        setPuntiDifesa(generaPuntiDifesa(giocatore.getPuntiDifesa()));
        setPuntiAttacco(generaPuntiAgilità(giocatore.getPuntiAgilità()));
        //drop esperienza
        setDropEsperienza(generaDropEsperienza());
        //drop oggetti
        setDropOggetto(generaDrop());
        setQuantitaDrop(random.nextInt(2)+1);

    }

    public void interagisci(Gioco gioco) throws IOException {
        while (true) {
            gioco.getGestoreClient().manda("Ti ritrovi davanti a un avamposto. Un cavaliere si pone davanti a te.\n" +
                    "Cavaliere: Benvenuto, viandante! Dove ti stai dirigendo?\n");
            gioco.getGestoreClient().manda("Cosa rispondi?");
            gioco.getGestoreClient().manda("1. Non ho una meta precisa");
            gioco.getGestoreClient().manda("2. Sto cercando da mangiare");
            gioco.getGestoreClient().manda("3. Non sono affari tuoi\nPASS");

            String scelta = gioco.getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                gioco.getGestoreClient().manda("Cavaliere: Non avere una meta precisa può essere avventuroso.\n" +
                        "Ma fa attenzione, il pericolo è sempre dietro l'angolo! Sai come difenderti?\n" +
                        "Tieni, potrebbe tornarti utile");
                gioco.sleep();
                gioco.getGestoreClient().manda("Il cavaliere ti dona un coltello!");
                Arma coltello = creaArma();
                gioco.getGiocatore().nuovaArma(coltello);
                break;
            } else if (scelta.equals("2")) {
                gioco.getGestoreClient().manda("Cavaliere: Beh, sei stato molto fortunato ad avermi incontrato!\n" +
                        "Ecco a te...");
                gioco.sleep();
                gioco.getGestoreClient().manda("Il cavaliere ti dona un pezzo di carne!");
                gioco.getGestoreDb().incrementaQuantita(gioco, "manzo", 1);
                break;
            } else if (scelta.equals("3")) {
                gioco.getGestoreClient().manda("Cavaliere: Che modo è questo di rispondermi, ti pentirai di avermi incontrato!");
                //inizia il combattimento
                gioco.Combattimento(this);
                break;
            } else {
                gioco.getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }

    private Arma creaArma(){
        String descrizioneArma = "Un coltello con uno strano simbolo impresso sul manico";
        Random random = new Random();
        int danniBase = 4 + random.nextInt(11);
        return new Arma("Coltello del cavaliere", descrizioneArma , danniBase, "Nessuna");
    }
    }

