/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Rozgrywka;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Gracz;

/**
 * Klasa odpowiadająca na pytania index.xhtml, nie pamięta sesji.
 * @author andrzej
 */
@ManagedBean
@RequestScoped
public class Powitalna {
    @EJB
    private Rozgrywka rozgrywka;
    /**
     * Creates a new instance of Powitalna
     */
    public Powitalna() {
        
    }
    
    public String [] getNazwy(){
        return rozgrywka.getNazwyTerenow();
    }
    
    public String getTeren(int i){
        int [] l=rozgrywka.getTerrain(i);
        String r="";
        for(int j=0;j<l.length;++j){
            r+=l[j]+" ";
        }
        return r;
    }
    
    public int lgraczy(int i){
        Gracz [] ge=rozgrywka.getGracze(i);
        if(ge==null)
            return 0;
        return ge.length;
    }
    
    public int mgraczy(int i){
        return rozgrywka.getMGracze(i);
    }
    
    public String [] graczy(int i){
        Gracz [] ge=rozgrywka.getGracze(i);
        String [] lg=new String[ge.length];
        for(int j=0;j<ge.length;++j){
            lg[j]=ge[j].getName()+" "+ge[j].getLife();
        }
        return lg;
    }
}
