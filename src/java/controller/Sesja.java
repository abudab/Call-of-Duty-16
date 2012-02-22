/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import javax.servlet.http.HttpSessionBindingEvent;
//import javax.servlet.http.HttpSessionBindingListener;

/**
 *To jest klasa która odpowiada za sesję - połączenie gracza z serwerem.
 * Podaje potrzebne rzeczy JSF'owi oraz dodaje gracza do gry.
 * @author andrzej
 */
@ManagedBean
@SessionScoped
public class Sesja /*implements HttpSessionBindingListener */{
   

    private long myid=-1;
    @EJB
    private Rozgrywka rozgrywka;
    private String nick=null;
    private int graid=-1;

    /**
     * Zwraca numer areny, na której gra gracz przypisany do sesji
     * @return id gry
     */
    public int getGraid() {
        return graid;
    }

    /**
     * Ustawia graczowi z sesji arenę na której gra
     * @param graid id gry
     */
    public void setGraid(int graid) {
        this.graid = graid;
    }

    /**
     * Zwraca pseudonim gracza przypisanego do sesji
     * @return nick
     */
    public String getNick() {
        if(nick==null)
            return "";
        return nick;
    }

    /**
     * Ustawia graczowi z sesji pseudonim widoczny dla pozostałych graczy.
     * @param nick 
     */
    public void setNick(String nick) {
        this.nick = nick;
    }
    

    /**
     * Creates a new instance of Sesja
     */
    public Sesja(){
        
    }
    
    /*public String getTerrain(){
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
                        gracze[i].getLife()+" "+
                        gracze[i].getId();
        }
        return enemies;
    }*/

    /**
     * Pytając o id gracza próbujemy go wepchnąć do gry.
     * Próba dołączenia do gry wystąpi, gdy znany jest nick i wybrana arena,
     * ale identyfikator gracza jest nieustawiony - kombinacja która występuje po kliknięciu
     * na "Dołącz" na stronie startowej. Gdy próba dołączenia gracza będzie pomyślna zwrócony
     * będzie uzyskany identyfikator.
     * Gdy identyfikator gracza jest już ustawiony sprawdzana jest obecność gracza w grze.
     * @return identyfikator gracza - liczba nieujemna lub -1 gdy gracz nie gra
     */
    public long getMyid() {
        if(myid==-1)
            if(nick!=null && graid!=-1)
                myid=rozgrywka.add(nick,graid);
            else
                return -1;
        else{
            if(!rozgrywka.czyGra(myid, graid) || nick==null){
                myid=-1;
                nick=null;
            }
        }
        return myid;
       /* if(g==null)//sesja (jeszcze) nie ma przypisanego gracza
            if(myid==-1 && nick!=null && graid>-1) //id niezainicjowane, ale jest ustawiony nick i id gry
                                                // to znaczy, że gracz dopiero wcisnął "dołącz"
                myid=rozgrywka.add(nick,graid);
        if(myid>-1)
            g=rozgrywka.getGracz(myid,graid);
        return myid;*/
    }
    
    /*public int getIlegraczy(){
        Gracz [] ge=rozgrywka.getGracze(graid);
        if(ge==null)
            return 0;
        return ge.length;
    }*/

    /*@Override
    public void valueBound(HttpSessionBindingEvent event) {
        
    }*/

    /**
     * Handler ma wywalić gracza jeśli sesja wygaśnie.
     * Czy działa? Nie wiem, chyba nie.
     * @param event 
     */
    /*@Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        seppuku();
    }*/
    
    /**
     * Usunięcie gracza z gry 
     */
    public void seppuku(){
        if(myid>-1){
            rozgrywka.remove(myid,graid);
        }
        /*else if(g!=null){
            rozgrywka.remove(g.getId(),graid);
        }*/
        myid=-1;
        nick=null;
        /*try{
            FacesContext context = FacesContext.getCurrentInstance(); 
            context.getExternalContext().getSessionMap().remove("#{sesja}");
        }
        catch(Exception e){}*/
        /*if(g!=null){
            rozgrywka.remove(g, graid);
            //g=null;
        }
        myid=-1;
        nick=null;
        try{
            FacesContext context = FacesContext.getCurrentInstance(); 
            context.getExternalContext().getSessionMap().remove("#{sesja}");
        }
        catch(Exception e){}*/
    }
    
}
