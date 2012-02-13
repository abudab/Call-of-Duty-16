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
 *To jest klasa która odpowiada za sesję - połączenie gracza z serwerem.
 * Podaje potrzebne rzeczy JSF'owi oraz dodaje gracza do gry.
 * @author andrzej
 */
@ManagedBean
@SessionScoped
public class Sesja implements HttpSessionBindingListener {
   
    private Gracz g=null;
    private long myid=-1;
    @EJB
    private Rozgrywka rozgrywka;
    private String nick=null;
    private int graid=-1;

    public int getGraid() {
        return graid;
    }

    public void setGraid(int graid) {
        this.graid = graid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    /**
     * Gracz powienien zniknąć jak sesja wygaśnie i instancja zginie.
     * To chyba nie jest dobre rozwiązanie...
     * @throws Throwable 
     */
    @Override
    protected void finalize() throws Throwable {
        if(g!=null)
            rozgrywka.remove(g,graid);
    }

    /**
     * Creates a new instance of Sesja
     */
    public Sesja(){
        
    }
    
    public String getTerrain(){
        String r="";
        int [] y=rozgrywka.getTerrain(graid);
        if(y!=null){
            int i;
            for(i=0;i<y.length-1;++i){
                r+=y[i]+" ";
            }
            r+=y[i]+"";
        }
        return r;
    }
    
    public int getXres(){
        return rozgrywka.getXresTerenu(graid);
    }
    
    public int getPos(){
        
        if(g!=null)
            return g.getPos();
        else
            return 0;
    }
    
    public int getLife(){
        
        if(g!=null)
            return  g.getLife();
        else
            return 0;
    }
    

    
    public String [] getEnemies(){
        Gracz [] gracze=rozgrywka.getGracze(graid);
        if(gracze==null)
            return null;
        String [] enemies=new String[gracze.length];
        for(int i=0;i<gracze.length;++i){
            if(gracze[i].getId()!=myid)
                enemies[i]=gracze[i].getName()+" "+
                        gracze[i].getPos()+" "+
                        gracze[i].getLife();
        }
        return enemies;
    }

    /**
     * Pytając o id gracza próbujemy go wepchnąć do gry.
     * Próba dołączenia do gry wystąpi, gdy znany jest nick i wybrana arena,
     * a nie ma jeszcze gracza w grze.
     * @return 
     */
    public long getMyid() {
        if(g==null)
            if(myid==-1 && nick!=null && graid>-1)
                myid=rozgrywka.add(nick,graid);
        if(myid>-1)
            g=rozgrywka.getGracz(myid,graid);
        return myid;
    }
    
    public int getIlegraczy(){
        Gracz [] ge=rozgrywka.getGracze(graid);
        if(ge==null)
            return 0;
        return ge.length;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        
    }

    /**
     * Handler ma wywalić gracza jeśli sesja wygaśnie.
     * Czy działa? Nie wiem, chyba nie.
     * @param event 
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        seppuku();
    }
    
    /**
     * Wywalenie gracza z gry 
     */
    public void seppuku(){
        if(g!=null){
            rozgrywka.remove(g, graid);
            myid=-1;
            g=null;
            nick=null;
        }
    }
    
}
