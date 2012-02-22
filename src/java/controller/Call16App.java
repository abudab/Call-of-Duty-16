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
import java.util.ArrayList;
import java.util.List;
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
     * Przesłaniam metodę bo chcę skorzystać z własnej klasy websocket: Call16WebSocket.
     * @param protocolHandler
     * @param listeners
     * @return 
     */
    @Override
    public WebSocket createWebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        return new Call16WebSocket(protocolHandler,listeners);
    }

    /**
     * Ustawia tablice z arenami na których może toczyś się gra. Kolejność aren musi odpowiadać
     * identyfikatorom gier używanym w aplikacji i w komunikacji z klientami.
     * Ustawienie tego pola jest konieczne do działania serwera web socketów.
     * @param gry tablica aren
     */
    public void setGry(Gra[] gry) {
        this.gry = gry;
    }
    
    /**
     * Obsługuje wiadomość powitalną przesyłaną przez klientów zaraz po połączeniu.
     * Ustawia pola web socketa, z którego nadeszła wiadomość jeżeli format wiadomości jest poprawny
     * co umożliwia dalszą obsługę wiadomości przesyłanych przez niego (nie podpisane web sockety
     * są ignorowane). Wyszukuje i usuwa duplikaty.
     * @param ws web socket z którego nadeszło powitanie
     * @param text treść wiadomości - powinno być "Uklony idgry idgracza"
     */
    void chlebemISola(Call16WebSocket ws,String text){
        int graid=Integer.parseInt(text.split(" ")[1]);
        long id=Long.parseLong(text.split(" ")[2]);
        if(graid<0 || gry==null || graid>gry.length-1 || id<0){
            ws.close();
            remove(ws);
            return;
        }
        if(!gry[graid].czyGra(id)){
            ws.close();
            remove(ws);
            return;
        }
        List<WebSocket> nadmiarowe=new ArrayList<WebSocket>();
        for(WebSocket i: getWebSockets()){
            try{
                if(((Call16WebSocket)i).getId()==id && i.hashCode()!=ws.hashCode())
                   nadmiarowe.add(i);
            }
            catch(Exception e){
                nadmiarowe.add(i);
            }
        }
        for(WebSocket i: nadmiarowe){
            i.close();
            remove(i);
        }
        ws.setGraid(graid);
        ws.setId(id);
    }
    
    /**
     * Obsługuje prośbę przesunięcia gracza. Sprawdza obecność gracza w grze i
     * zgodność podpisu z prośbą. Wywołuje odpowiednią metodę klasy Gra i 
     * gdy wystąpiło niezerowe przesunięcie wysyła do graczy z areny wiadomość 
     * ruch: "Ruch idgracza przesuniecie_wzgledem_poprzedniej_pozycji"
     * @param ws web socket z którego prośba
     * @param text treść wiadomości - powinno być "Ruch idgracza kierunek"
     */
    void rusz(Call16WebSocket ws,String text){
        if(ws.getId()<0 || !gry[ws.getGraid()].czyGra(ws.getId())){
            ws.close();
            remove(ws);
            return;
        }
        long kto=Long.parseLong(text.split(" ")[1]);
        if(kto!=ws.getId()){//oszust!
            ws.close();
            remove(ws);
            return;
        }
        int dx=gry[ws.getGraid()].ruszGracza(kto,
                    Integer.parseInt(text.split(" ")[2]));
        if(dx!=0){
            for(WebSocket i:getWebSockets()){
                try{
                    if(((Call16WebSocket)i).getGraid()==ws.getGraid() /*&& i.isConnected()*/)
                        i.send("Ruch "+kto+" "+dx);
                }
                catch(Exception e){
                    try{
                        i.close();
                    }
                    catch(Exception ee){}
                }
            }
        }
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
                chlebemISola((Call16WebSocket)socket,text);
            }
            catch(Exception e){}
            return;
        }
        if(((Call16WebSocket)socket).getGraid()<0 || ((Call16WebSocket)socket).getGraid()>=gry.length)
            return; //odrzucanie wiadomości od klientów którzy się nie ukłonili i nie mają przypisanych graid
        if(text.startsWith("chat")){//broadcast - nie używane
            broadcastShout(text.split(" ")[1],text.substring(text.indexOf(" ", 5)));
        }
        else if(text.startsWith("Ruch")){//przesunięcie
            rusz((Call16WebSocket)socket,text);
            
        }
        else if(text.startsWith("Imout")){//usunięcie gracza na własną prośbę
            try{
                long id=((Call16WebSocket)socket).getId();
                int gid=((Call16WebSocket)socket).getGraid();
                if(gid>-1 && gid<gry.length){
                    gry[gid].remove(id);
                    sendUpdate(gid);
                }
            }catch(Exception e){}
        }
        else{//strzał
            if(((Call16WebSocket)socket).getId()<0 || 
                    !gry[((Call16WebSocket)socket).getGraid()].czyGra(((Call16WebSocket)socket).getId())){
                socket.close();
                remove(socket);
                return;
            }
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
     * @param gdzie numer areny
     */
    void sendUpdate(int gdzie) {
        //this.gracze=gracze;
        String msg="Gracze ";
        if(gdzie<0 || gdzie > gry.length-1)
            return;
        Gracz[] gracze=gry[gdzie].getGracze();
        if(gracze==null || gracze.length==0)
            return;
        for(int i=0;i<gracze.length;++i){
            if(gracze[i]!=null && gracze[i].getName()!=null){
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
        }
        for(WebSocket i:getWebSockets()){
            try{
                if(((Call16WebSocket)i).getGraid()==gdzie /*&& i.isConnected()*/)
                    i.send(msg);
            }
            catch(Exception e){
                //oj! socket się zamknął
                try{
                    i.close();
                }
                catch(Exception ee){}
            }
        }
    }

    /**
     * Nieużywana metoda
     * @param who
     * @param said 
     */
    private void broadcastShout(String who, String said) {
        for(WebSocket ws: getWebSockets()){
            ws.send(who+" powiedział: "+said);
        }
    }

    /**
     * Reaguję na prośbę wykonania strzału. W przypadku poprawnej formy wiadomości,
     * wywołuje odpowiednią metodę klasy gra i rozsyła do graczy z areny trajektorię
     * pocisku: "Trajektoria x0 y0 x1 y1 .... xeksplozji yeksplozji". W przypadku
     * wykrycia obrażeń wywołujue sendUpdate. W przypadku zniszczenia czołgu/ów
     * rozsyła wiadomości "Padł pozycja"
     * @param smsg treść wiadomości ze strzałem - powinno być "x0 alfa silastrzalu"
     * @param graid numer areny
     * @return prawda jeżeli strzał wykonany
     */
    private boolean strzel(String smsg,int graid) {
        int x0;
        int alfa;
        int v0;
        try{
            x0=Integer.parseInt(smsg.split(" ")[0]);
            alfa=Integer.parseInt(smsg.split(" ")[1]);
            v0=Integer.parseInt(smsg.split(" ")[2]);
        }
        catch(Exception e){
            return false;
        }
        if(graid<0 || graid>=gry.length)
            return false;
        String msg="Trajektoria ";
        msg+=gry[graid].trajektoria(x0, v0, alfa);
        for(WebSocket ws:getWebSockets()){
            try{
                if(((Call16WebSocket)ws).getGraid()==graid /*&& ws.isConnected()*/)
                    ws.send(msg);
            }
            catch(Exception e){
                try{
                    ws.close();
                }
                catch(Exception ee){}
            }
        }
        String [] ss=msg.split(" ");
        if(gry[graid].czyTrafilo(Integer.parseInt(ss[ss.length-2]), Integer.parseInt(ss[ss.length-1]))){
            sendUpdate(graid);
            int [] ubici=gry[graid].getUbitychPozycje();
            if(ubici!=null && ubici.length>0)
                for(int i=0;i<ubici.length;++i){
                    for(WebSocket ws: getWebSockets()){
                        try{
                        if(((Call16WebSocket)ws).getGraid()==graid /*&& ws.isConnected()*/)
                            ws.send("Padl "+ubici[i]);
                        }
                        catch(Exception e){
                            try{
                                ws.close();
                            }
                            catch(Exception ee){}
                        }
                    }
                    
                }
        }
        return true;
    }
    
    
    
    

    
    
       
}
