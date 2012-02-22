/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.websockets.WebSocketEngine;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.*;

/**
 *Klasa rozstawiająca wszystko po kątach.
 * Instancja powstaje w raz z uruchomieniem aplikacji i inicjuje wtedy znalezione w
 * bazie danych areny oraz odpala serwer websocketów.
 * @author andrzej
 */
@Singleton

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class Rozgrywka {
    Gracz [] gracze=null;
    Gra [] gry=null;
    List tereny;

    @PersistenceContext(unitName = "Call16PU")
    private EntityManager em;
    long lastid=0;
    private final Call16App app=new Call16App();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    /**
     * To się dzieje po uruchomieniu aplikacji - inicjacja aren i odpalanie websocketów
     */
    @PostConstruct
    public void init(){
        tereny=(em.createNamedQuery("Teren.findAll")).getResultList();
        if(tereny.size()>0){
            gry=new Gra[tereny.size()];
            for(int i=0;i<tereny.size();++i){
                gry[i]=new Gra(i);
                Teren t=(Teren)(tereny.get(i));
                gry[i].setX_res(t.getXres());
                List ly=t.getIgrekList();
                int [] y=new int[ly.size()];
                for(int j=0;j<ly.size();++j)
                    y[j]=((Igrek)ly.get(j)).getY();
                gry[i].setTeren(y);
                ly=t.getPozycjaList();
                y=new int[ly.size()];
                for(int j=0;j<ly.size();++j)
                    y[j]=((Pozycja)ly.get(j)).getX();
                gry[i].setPozycje(y);
            }
        }
        //Fizyka.teren=terrain;
        //Fizyka.x_res=66;
        WebSocketEngine.getEngine().register(app);
        app.setGry(gry);
    }
    
    
    /**
     * Usiłuje dodać gracza do gry na arenie graid. Gdy się powiedzie
     * wywołuje sendUpdate serwera web socketów
     * @param name pseudonim gracza
     * @param graid numer areny
     * @return nieujemny identyfikator gracza na serwerze lub -1 gdy
     * dodanie nie powiodło się.
     */
    public long add(String name,int graid){
        if(graid<0 || graid>gry.length-1 || !gry[graid].canIJoin()){
            return -1;
        }
        ++lastid;
        Gracz g=new Gracz();
        g.setLife(100);
        g.setId(lastid);
        g.setName(name);    
        int result=gry[graid].add(g);
        if(result==-1){
            lastid--;
            return -1;
        }
        app.sendUpdate(graid);
        return lastid;
    }
    

    /**
     * Usuwa gracza z gry i wysyła update klientom websocket.
     * @param id id gracza
     * @param graid  id gry
     */
    public void remove(long id,int graid){
        if(id<0 || gry==null || graid<0 || graid>gry.length-1)
            return;
        gry[graid].remove(id);
        app.sendUpdate(graid);
    }
    
    /**
     * Zwraca tablicę węzłów terenu
     * @param graid numer areny
     * @return 
     */
    public int [] getTerrain(int graid){
        if(graid>gry.length-1 || graid<0)
            return null;
        return gry[graid].getTeren();
    }
    
    /**
     * Zwraca gracza o danym id.
     * @param id id gracza
     * @param graid id gry w której gra gracz
     * @return gracz lub null gdy nie ma takiego
     */
    public Gracz getGracz(long id,int graid){
        if(graid>-1 && graid<gry.length && id>-1)
            return gry[graid].getGracz(id);
        return null;
    }
    

    public void persist(Object object) {
        em.persist(object);
    }
    
    /**
     * Zwraca listę nazw aren
     * @return 
     */
    public String [] getNazwyTerenow(){
        String [] r=new String[tereny.size()];
        int i=0;
        for(Object t:tereny){
            r[i]=((Teren)t).getNazwa();
            i++;
        }
        return r;
    }
    
    /**
     * Zwraca listę odległości pomiędzy węzłami aren.
     * @return 
     */
    public int [] getXresTerenow(){
        int [] r=new int[tereny.size()];
        int i=0;
        for(Object t:tereny){
            r[i]=((Teren)t).getXres();
            i++;
        }
        return r;
    }
    
    /*public List getTeren(int i){
        return ((Teren)tereny.get(i)).getIgrekList();
    }*/
    
    /**
     * Zwraca tablicę graczy z areny i
     * @param i numer areny
     * @return aktualnie grający gracze na arenie
     */
    public Gracz [] getGracze(int i){
        if(i<gry.length && i>-1){
            return gry[i].getGracze();
        }
        else
            return null;
    }
    
    /**
     * Zwraca maksymalną ilość graczy na arenie i
     * @param i numer areny
     * @return 
     */
    public int getMGracze(int i){
        if(i<gry.length && i>-1){
            return gry[i].getMax();
        }
        else
            return 0;
    }
    
    /**
     * Zwraca odległość x między węzłami terenu areny i
     * @param i numer areny
     * @return 
     */
    public int getXresTerenu(int i){
        if(i<gry.length && i>-1)
            return gry[i].getX_res();
        return 500;
    }
    
    /**
     * Sprawdza czy gracz gra na arenie.
     * @param id id gracza
     * @param graid numer areny
     * @return prawda gry gracz gra na arenie, fałsz gdy nie
     */
    public boolean czyGra(long id,int graid){
        if(id<0 || graid<0 || gry==null || graid>gry.length-1)
            return false;
        return gry[graid].czyGra(id);
    }

}
