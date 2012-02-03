/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import model.Fizyka;
import model.Gracz;

/**
 *
 * @author andrzej
 */
public class Call16App extends WebSocketApplication{

    private Gracz [] gracze=null;
    int [] pozycje={150,400,900,1500,1900};
    long lastid=0;
    /*@Override
    public WebSocket createWebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        return new Call16WebSocket(protocolHandler,listeners);
    }*/

    @Override
    public void onMessage(WebSocket socket, String text) {
        if(text.startsWith("chat")){
            broadcastShout(text.split(" ")[1],text.substring(text.indexOf(" ", 5)));
        }
        else {/*if(text.startsWith("strzal")){*/
            //strzel(text.substring(6));
            strzel(text);
        }
    }
    

    @Override
    public boolean isApplicationRequest(Request rqst) {
        return true;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    void sendUpdate() {
        //this.gracze=gracze;
        String msg="Gracze ";
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
            i.send(msg);
        }
    }

    private void broadcastShout(String who, String said) {
        for(WebSocket ws: getWebSockets()){
            ws.send(who+" powiedział: "+said);
        }
    }

    private void strzel(String smsg) {
        int x0=Integer.parseInt(smsg.split(" ")[0]);
        int alfa=Integer.parseInt(smsg.split(" ")[1]);
        int v0=Integer.parseInt(smsg.split(" ")[2]);
        String msg="Trajektoria ";
        msg+=Fizyka.trajektoria(x0, v0, alfa);
        for(WebSocket ws:getWebSockets()){
            ws.send(msg);
        }
        if(gracze!=null){
            String [] ss=msg.split(" ");
            int x=Integer.parseInt(ss[ss.length-2]);
            int y=Integer.parseInt(ss[ss.length-1]);
            boolean trafionecos=false;
            for(int i=0;i<gracze.length;++i){
                int gx=gracze[i].getPos();
                int gy=Fizyka.igrek(gx);
                if((x-gx)*(x-gx)+(y-gy)*(y-gy)<3600){
                    gracze[i].setLife(gracze[i].getLife()-10>2?
                            gracze[i].getLife()-10:0);
                    trafionecos=true;
                }
                if((x-gx)*(x-gx)+(y-gy)*(y-gy)<2400)
                    gracze[i].setLife(gracze[i].getLife()-20>2?
                            gracze[i].getLife()-20:0);
                if((x-gx)*(x-gx)+(y-gy)*(y-gy)<100)
                    gracze[i].setLife(gracze[i].getLife()-20>2?
                            gracze[i].getLife()-20:0);
                
            }
            if(trafionecos){
                
                sendUpdate();
            }
            
        }
    }
    
    
    
    
    public long addGracz(String name){
        if(gracze== null || gracze.length<6){
            if(gracze==null)
                gracze=new Gracz[1];
            else{
                Gracz [] ngracze=new Gracz[gracze.length+1];
                for(int i=0;i<gracze.length;++i)
                    ngracze[i]=gracze[i];
                gracze=ngracze;
            }
            Gracz g=new Gracz();
            g.setLife(100);
            g.setPos(pozycje[gracze.length-1]);
            g.setId(lastid);
            g.setName(name);
            ++lastid;
            gracze[gracze.length-1]=g;
            sendUpdate();
            return g.getId();
        }
        else
            return (long)-1;
    }
    
    public void removeGracz(Gracz g){
        if(gracze==null)
            return;
        if(gracze.length<2){
            gracze=null;
            return;
        }
        Gracz [] ngracze=new Gracz[gracze.length-1];
        int j=0;
        for(int i=0;i<gracze.length;++i){
            if(g.getId()!=gracze[i].getId()){
                ngracze[j]=gracze[i];
                j++;
            }
        }
        sendUpdate();
        gracze=ngracze;
    }
    
    public Gracz getGracz(long id){
        for(int i=0;i<gracze.length;++i)
            if(id==gracze[i].getId())
                return gracze[i];
        return null;
    }
    
    public Gracz [] getGracze(){
        return gracze;
    }
    
}
