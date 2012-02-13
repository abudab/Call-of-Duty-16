/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

/**
 *
 * @author andrzej
 */
public class Call16WebSocket extends DefaultWebSocket{

    public Call16WebSocket(ProtocolHandler protocolHandler, WebSocketListener... listeners) {
        super(protocolHandler, listeners);
    }
    
    private int graid=-1;
    private long id=-1;
    private boolean valid=false;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
            
    public int getGraid(){
        return graid;
    }
    
    public void setGraid(int graid){
        this.graid=graid;
    }
}
