package Server;

import java.io.IOException;

//In questa interfaccia vengono definiti solo i metodi manda e ricevi,
// che sono gli unici che le altre classi devono poter utilizzare
public interface InterfacciaGestoreClient {

    void manda(String string);
    String ricevi()throws IOException;
}
