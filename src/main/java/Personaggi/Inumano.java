package Personaggi;

import Server.InterfacciaGestoreClient;

public abstract class Inumano extends Nemico{
    public Inumano(InterfacciaGestoreClient gestoreClient, String tipo) {
        super(gestoreClient, tipo);
    }
}
