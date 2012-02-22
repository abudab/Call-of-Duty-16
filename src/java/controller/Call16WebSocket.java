/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

/**
 *Połączenie przez web socket z klientem gry. Posiada pola, które pozwalają
 * na identyfikację gracza i areny na której się znajduje.
 * @author andrzej
 */
public class Call16WebSocket extends DefaultWebSocket{

    public Call16WebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        super(protocolHandler, listeners);
    }
    
    private int graid=-1;
    private long id=-1;

    /**
     * Zwraca identyfikator gracza, z którym połączenie odzwierciedla instancja.
     * @return 
     */
    public long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator gracza, z którym połączenie odzwierciedla instancja.
     * @param id unikalna liczba związana z graczem
     */
    public void setId(long id) {
        this.id = id;
    }
        
    /**
     * Zwraca identyfikator areny na której gra gracz, z którym połączenie.
     * @return 
     */
    public int getGraid(){
        return graid;
    }
    
    /**
     * Ustawia identyfikator areny na której gra gracz, z którym połączenie.
     * @param graid 
     */
    public void setGraid(int graid){
        this.graid=graid;
    }
}
