/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;
import model.Gra;
import model.Gracz;

/**
 *Aplikacja websocketowa.
 * Wysyła, odbiera i odmawia posłuszeństwa
 * @author andrzej
 */
public class Call16App extends WebSocketApplication{

    //private Gracz [] gracze=null;
    //int [] pozycje={150,400,900,1500,1900};
    //long lastid=0;
    //List tereny;
    Gra [] gry;
    
    /**
     * Przesłaniam metodę bo chcę skorzystać z własnej klasy websocket: Call16WebSocket
     * @param protocolHandler
     * @param listeners
     * @return 
     */
    @Override
    public WebSocket createWebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        return new Call16WebSocket(protocolHandler,listeners);
    }

    public void setGry(Gra[] gry) {
        this.gry = gry;
    }

    /**
     * Reaguje na wiadomość.
     * Odrzuca wiadomości, które przychodzą od websocketów, które się
     * nie przedstawiają, lub nie przedstawiły się wcześniej.
     * Wywołuje odpowiednią metodę w zależności od rodzaju wiadaomości.
     * @param socket
     * @param text 
     */
    @Override
    public void onMessage(WebSocket socket, String text) {
        if(text.startsWith("Uklony")){//klient pod tym web socketem chce sie przedstawić?
            try{
                int graid=Integer.parseInt(text.split(" ")[1]);//przypisujemy mu numer gry
                ((Call16WebSocket)socket).setGraid(graid);
                long id=Long.parseLong(text.split(" ")[2]);
                ((Call16WebSocket)socket).setId(id);
                ((Call16WebSocket)socket).setValid(true);
            }
            catch(Exception e){}
            return;
        }
        if( !((Call16WebSocket)socket).isValid() || ((Call16WebSocket)socket).getGraid()>=gry.length)
            return; //odrzucanie wiadomości od klientów którzy się nie ukłonili i nie mają przypisanych graid
        if(text.startsWith("chat")){//broadcast - nie używane
            broadcastShout(text.split(" ")[1],text.substring(text.indexOf(" ", 5)));
        }
        else {//strzał
            
            strzel(text,((Call16WebSocket)socket).getGraid());
        }
    }
    

    @Override
    public boolean isApplicationRequest(Request rqst) {
        return true;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Wysyła do wszystkich klientów w danej grze informację o graczach tj.
     * ich pozycje, nicki i punkty zdrowia.
     * Powinna być wywoływana jak w danej grze, zmieni się liczba graczy lub zmienią
     * się ich parametry(zdrowie).
     * @param gdzie 
     */
    void sendUpdate(int gdzie) {
        //this.gracze=gracze;
        String msg="Gracze ";
        Gracz[] gracze=gry[gdzie].getGracze();
        if(gracze==null)
            return;
        for(int i=0;i<gracze.length;++i){
            if(i<gracze.length-1)
            msg+=gracze[i].getName()+" "+
                    gracze[i].getPos()+" "+
                    gracze[i].getLife()+" "+
                    gracze[i].getId()+" ";
            else
                msg+=gracze[i].getName()+" "+
                    gracze[i].getPos()+" "+
                    gracze[i].getLife()+" "+
                    gracze[i].getId();
        }
        for(WebSocket i:getWebSockets()){
            if(((Call16WebSocket)i).getGraid()==gdzie)
                i.send(msg);
        }
    }

    private void broadcastShout(String who, String said) {
        for(WebSocket ws: getWebSockets()){
            ws.send(who+" powiedział: "+said);
        }
    }

    private void strzel(String smsg,int graid) {
        int x0=Integer.parseInt(smsg.split(" ")[0]);
        int alfa=Integer.parseInt(smsg.split(" ")[1]);
        int v0=Integer.parseInt(smsg.split(" ")[2]);
        if(graid<0 || graid>=gry.length)
            return;
        String msg="Trajektoria ";
        msg+=gry[graid].trajektoria(x0, v0, alfa);
        for(WebSocket ws:getWebSockets()){
            if(((Call16WebSocket)ws).getGraid()==graid)
                ws.send(msg);
        }
        String [] ss=msg.split(" ");
        if(gry[graid].czyTrafilo(Integer.parseInt(ss[ss.length-2]), Integer.parseInt(ss[ss.length-1])))
            sendUpdate(graid);
        
    }
    
    
    
    

    
    
       
}
