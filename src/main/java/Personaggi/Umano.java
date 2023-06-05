package Personaggi;

import Server.InterfacciaGestoreClient;

public abstract class Umano extends Nemico{

    public Umano(InterfacciaGestoreClient gestoreClient, String tipo) {
        super(gestoreClient, tipo);
    }
}
