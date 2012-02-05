/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *Klasa reprezentuje grę na jednej arenie.
 * Zawiera listę graczy, identyfikator i informacje o arenie.
 * @author andrzej
 */
public class Gra {
    Gracz [] gracze=null;
    int id;
    int max;
    int x_res;
    int [] teren;
    int [] pozycje;
    Fizyka fizyka;
    
    /**
     * Inicjując należy podać unikalny numer jaki gra będzie mieć w systemie
     * @param id numer gry
     */
    public Gra(int id){
        this.id=id;
    }
    /**
     * Zwraca id gry
     * @return id gry
     */
    public int getId(){
        return id;
    }
    /**
     * Ustawia id gry
     * @param id 
     */
    public void setId(int id){
        this.id=id;
    }
    /**
     * Dodaje gracza do listy - gracz musi być wcześniej stworzony
     * metoda ustawi mu jedynie właściwy identyfikator gry i pozycję.
     * @param g gracz
     * @return ile graczy obecnie liczy gra lub -1 gdy dodanie nie powiodło się
     */
    public int add(Gracz g){
            g.setGid(id);
            g.setPos(pozycje[gracze==null?0:gracze.length]);
            if(gracze==null)
                gracze=new Gracz[1];
            else if(gracze.length>max){
                return -1;
            }
            else{
                Gracz [] ngracze=new Gracz[gracze.length+1];
                for(int i=0;i<gracze.length;++i)
                    ngracze[i]=gracze[i];
                gracze=ngracze;
            }
            gracze[gracze.length-1]=g;
            return gracze.length;
    }
    
    public void remove(long id){
        if(gracze==null)
            return;
        
        boolean aOnTamWOgoleJest=false;
        for(int i=0;i<gracze.length;++i){
            if(gracze[i].getId()==id){
                aOnTamWOgoleJest=true;
                break;
            }
        }
        if(!aOnTamWOgoleJest)
            return;
        if(gracze.length<2){
            gracze=null;
            return;
        }
        Gracz [] ngracze=new Gracz[gracze.length-1];
        int j=0;
        for(int i=0;i<gracze.length;++i){
            if(id!=gracze[i].getId()){
                ngracze[j]=gracze[i];
                j++;
            }
        }
        gracze=ngracze;
    }

    
    public int getMax() {
        return max;
    }

    /**
     * Ustala maksymalną liczbę graczy, którzy mogą brać udział w grze
     * @param max 
     */
    public void setMax(int max) {
        this.max = max;
    }

    public int[] getTeren() {
        return teren;
    }

    public void setTeren(int[] teren) {
        this.teren = teren;
        if(fizyka==null)
            fizyka=new Fizyka();
        fizyka.teren=teren;
    }

    public int getX_res() {
        return x_res;
    }

    public void setX_res(int x_res) {
        this.x_res = x_res;
        if(fizyka==null)
            fizyka=new Fizyka();
        fizyka.x_res=x_res;
    }
    
    public Gracz [] getGracze(){
        return gracze;
    }

    public int[] getPozycje() {
        return pozycje;
    }

    public void setPozycje(int[] pozycje) {
        this.pozycje = pozycje;
        max=pozycje.length;
    }
    
    public boolean canIJoin(){
        if(gracze==null || gracze.length<max)
            return true;
        else
            return false;
    }
    
    public Gracz getGracz(long id){
        if(gracze==null)
            return null;
        for(int i=0;i<gracze.length;++i){
            if(gracze[i].getId()==id)
                return gracze[i];
        }
        return null;
    }
    
    public String trajektoria(int ix0,int iv0,int ialfa){
        return fizyka.trajektoria(ix0, iv0, ialfa);
    }
    
    public int igrek(int x){
        return fizyka.igrek(x);
    }
    
    public boolean czyTrafilo(int x,int y){
        List ubici=null;
        if(gracze==null)
            return false;
        boolean trafilo=false;
        for(int i=0;i<gracze.length;++i){
            int gx=gracze[i].getPos();
            int gy=igrek(gx);
            double r=Math.sqrt((x-gx)*(x-gx)+(y-gy)*(y-gy));
            if(r<36.){
                trafilo=true;
                gracze[i].setLife( gracze[i].getLife()-
                        (int)(40*Math.exp(-r*r/600.)) );
                if(gracze[i].getLife()<1){
                    gracze[i].setLife(0);
                    if(ubici==null)
                        ubici=new ArrayList();
                    ubici.add(gracze[i].getId());
                }
            }
        }
        if(ubici!=null)
            for(Object id: ubici)
                remove((Long)id);
        return trafilo;
    }
}
