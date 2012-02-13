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
    int [] terrain={130, 260, 400, 450, 430, 410, 370, 270, 190, 130, 100,
                    90, 120, 180, 260, 430, 530, 480, 390, 400, 500,
                    530, 400, 290, 200, 130, 40, 50, 160, 280, 270};
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
    
    
    
    public long add(String name,int graid){
        if(graid<0 || graid>gry.length-1 || !gry[graid].canIJoin()){
            return -1;
        }
        ++lastid;
        Gracz g=new Gracz();
        g.setLife(100);
        g.setId(lastid);
        g.setName(name);    
        gry[graid].add(g);
        app.sendUpdate(graid);
        return lastid;
    }
    
    public void remove(Gracz g,int graid){
        gry[graid].remove(g.getGid());
        app.sendUpdate(graid);
    }
    
    
    public int [] getTerrain(int graid){
        if(graid>gry.length)
            return null;
        return gry[graid].getTeren();
    }
    
    public Gracz getGracz(long id,int graid){
        if(graid>-1 && graid<gry.length && id>-1)
            return gry[graid].getGracz(id);
        return null;
    }
    

    public void persist(Object object) {
        em.persist(object);
    }
    
    public String [] getNazwyTerenow(){
        String [] r=new String[tereny.size()];
        int i=0;
        for(Object t:tereny){
            r[i]=((Teren)t).getNazwa();
            i++;
        }
        return r;
    }
    
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
    
    public Gracz [] getGracze(int i){
        if(i<gry.length && i>-1){
            return gry[i].getGracze();
        }
        else
            return null;
    }
    
    public int getMGracze(int i){
        if(i<gry.length && i>-1){
            return gry[i].getMax();
        }
        else
            return 0;
    }
    
    public int getXresTerenu(int i){
        if(i<gry.length && i>-1)
            return gry[i].getX_res();
        return 500;
    }

}
