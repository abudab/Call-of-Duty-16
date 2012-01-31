/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import model.Gracz;

/**
 *
 * @author andrzej
 */
@ManagedBean
@SessionScoped
public class Sesja implements HttpSessionBindingListener {
   
    private Gracz [] gracze=null;
    private Gracz g=null;
    private long myid=-1;
    @EJB
    private Rozgrywka rozgrywka;
    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        if(g!=null)
            rozgrywka.remove(g);
    }

    /**
     * Creates a new instance of Sesja
     */
    public Sesja(){
        
    }
    
    public int [] getTerrain(){
        return rozgrywka.getTerrain();
    }
    
    public int getPos(){
        return g.getPos();
    }
    
    public int getLife(){
        return  g.getLife();
    }
    
    public void lockGraczy(){
        gracze=rozgrywka.getGracze();
    }
    
    public String [] getEnemies(){
        if(gracze==null)
        lockGraczy();
        String [] enemies=new String[gracze.length];
        for(int i=0;i<gracze.length;++i){
            if(gracze[i].getId()!=myid)
                enemies[i]=gracze[i].getName()+" "+
                        gracze[i].getPos()+" "+
                        gracze[i].getLife();
        }
        return enemies;
    }

    public long getMyid() {
        if(g==null)
            myid=rozgrywka.add(nick);
        if(myid>-1)
            g=rozgrywka.getGracz(myid);
        return myid;
    }
    
    public int getIlegraczy(){
        lockGraczy();
        if(gracze==null)
            return 0;
        return gracze.length;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        if(g!=null)
            rozgrywka.remove(g);
    }
    
    
}
