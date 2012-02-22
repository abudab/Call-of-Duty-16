/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Rozgrywka;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Gracz;

/**
 * Klasa odpowiadająca na pytania index.xhtml i cod17.xtml, nie pamięta sesji.
 * Aby dostarczać odpowiedzi komunikuje się z singletonem Rozgrywka
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
    
    /**
     * Zwraca nazwy dostępnych do gry aren.
     * @return tablica stringów z nazwami aren.
     */
    public String [] getNazwy(){
        return rozgrywka.getNazwyTerenow();
    }
    
    /**
     * Zwraca ciąg węzłów składających się na kształ terenu areny - wynik nadaje
     * się do bezpośredniego wklejenia na stronę - klient odczyta taką formę.
     * @param i numer areny (id gry) o której teren chodzi
     * @return string z ciągiem węzłów terenu
     */
    public String getTeren(int i){
        int [] l=rozgrywka.getTerrain(i);
        String r="";
        int j;
        for(j=0;j<l.length-1;++j){
            r+=l[j]+" ";
        }
        r+=l[j]+"";//dla pewności - nie kończymy spacją
        return r;
    }
    
    /**
     * Zwraca liczbę graczy aktualnie grających na arenie i.
     * @param i numer areny (id gry)
     * @return liczba graczy
     */
    public int lgraczy(int i){
        Gracz [] ge=rozgrywka.getGracze(i);
        if(ge==null)
            return 0;
        return ge.length;
    }
    
    /**
     * Zwraca maksymalną dopuszczalną liczbę graczy na arenie i.
     * @param i numer areny (id gry)
     * @return maks. liczba graczy
     */
    public int mgraczy(int i){
        return rozgrywka.getMGracze(i);
    }
    
    /**
     * Zwraca tablicę stringów z danymi graczy dla strony początkowej - aktualni gracze
     * na danej arenie. W przypadku braku graczy 
     * zwraca tablicę o długości zero. Elementy tablicy nadają się do umieszczenia
     * na stronie - klient został napisany aby odczytać taką formę. 
     * @param i numer areny (id gry)
     * @return tablica z danymi graczy "nick punkty_życia"
     */
    public String [] graczy(int i){
        Gracz [] ge=rozgrywka.getGracze(i);
        if(ge==null || ge.length==0)
            return new String[0];
        int l=0;
        for(int j=0;j<ge.length;++j)
            if(ge[j]!=null && ge[j].getName()!=null)
                l++;
        if(l<1)
            return new String[0];
        String [] lg=new String[ge.length];
        for(int j=0;j<ge.length;++j){
            if(ge[j]!=null && ge[j].getName()!=null)
                lg[j]=ge[j].getName()+" "+ge[j].getLife();
        }
        return lg;
    }
    
    /**
     * Zwraca rozdzielczość poziomą terenu - odległość x między węzłami.
     * @param graid numer areny (id gry)
     * @return odległość między węzłami
     */
    public int xres(int graid){
        return rozgrywka.getXresTerenu(graid);
    }
    
    /**
     * Zwraca tablicę stringów z danymi graczy dla strony z grą (cod17.xhtml).
     * W przypadku braku graczy zwraca tablicę długości zero. Format stringów
     * jest zgodny z tym co oczekuje klient. 
     * @param graid numer areny (id gry)
     * @return tablica danych graczy "nick pozycja punkty_życia id".
     */
    public String [] enemies(int graid){
        Gracz [] gracze=rozgrywka.getGracze(graid);
        if(gracze==null)
            return new String[0];
        int l=0;
        for(int j=0;j<gracze.length;++j)
            if(gracze[j]!=null && gracze[j].getName()!=null)
                l++;
        if(l<1)
            return new String[0];
        String [] enemies=new String[l];
        for(int i=0;i<gracze.length;++i){
            if(gracze[i]!=null && gracze[i].getName()!=null)
                enemies[i]=gracze[i].getName()+" "+
                        gracze[i].getPos()+" "+
                        gracze[i].getLife()+" "+
                        gracze[i].getId();
        }
        return enemies;
    }
    
    
}
