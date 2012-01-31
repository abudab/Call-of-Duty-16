/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.grizzly.websockets.WebSocketEngine;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import model.Fizyka;
import model.Gracz;

/**
 *
 * @author andrzej
 */
@Singleton

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class Rozgrywka {
    Gracz [] gracze=null;
    //int [] pozycje={150,400,900,1500,1900};
    int [] terrain={130, 260, 400, 450, 430, 410, 370, 270, 190, 130, 100,
                    90, 120, 180, 260, 430, 530, 480, 390, 400, 500,
                    530, 400, 290, 200, 130, 40, 50, 160, 280, 270};
    //long lastid=0;
    private final Call16App app=new Call16App();
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void init(){
        Fizyka.teren=terrain;
        Fizyka.x_res=66;
        WebSocketEngine.getEngine().register(app);
    }
    
    public boolean canIJoin(){
        if(gracze==null || gracze.length<6)
            return true;
        else
            return false;
    }
    
    public long add(String name){
        return app.addGracz(name);
    }
    
    public void remove(Gracz g){
        app.removeGracz(g);
    }
    
    
    public int [] getTerrain(){
        return terrain;
    }
    
    public Gracz getGracz(long id){
        return app.getGracz(id);
    }
    
    public Gracz [] getGracze(){
        return app.getGracze();
    }
    

}
