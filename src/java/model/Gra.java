/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    List<Gracz> ubici=new ArrayList<Gracz>();
    
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
            Random r=new Random();
            g.setGid(id);
            if(gracze==null || gracze.length==0)
                g.setPos(pozycje[r.nextInt(pozycje.length)]);
            else{
                for(int i=0;i<pozycje.length;++i){
                    boolean nadasie=true;
                    for(int j=0;j<gracze.length;++j)
                        if(gracze[j].getPos()==pozycje[i]){
                            nadasie=false;
                            break;
                        }
                    if(nadasie){
                        g.setPos(pozycje[i]);
                        break;
                    }
                }
            }
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
    
    /**
     * Wywołuje odpowiednią funkcję z instancji klasy Fizyka
     * @param ix0
     * @param iv0
     * @param ialfa
     * @return 
     */
    public String trajektoria(int ix0,int iv0,int ialfa){
        return fizyka.trajektoria(ix0, iv0, ialfa);
    }
    
    /**
     * Wywołuje odpowiednią funkcję z instancji klasy Fizyka
     * @param x
     * @return 
     */
    public int igrek(int x){
        return fizyka.igrek(x);
    }
    
    /**
     * Oblicza obrażenia wywołane przez strzał i uaktualnia dane graczy.
     * @param x pozycja x eksplozji pocisku
     * @param y pozycja y eksplozji pocisku
     * @return prawda gdy wykryto obrażenia (czyli gdy należy przekazać klientom nowe dane)
     */
    public boolean czyTrafilo(int x,int y){
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
                    ubici.add(gracze[i]);
                }
            }
        }
        if(ubici!=null)
            for(Gracz g: ubici){
                remove(g.getId());
            }
        return trafilo;
    }
    
    /**
     * Na podstawie listy z poprzednio wykonanego strzału zwraca pozycje 
     * graczy których życie dobiło zera.
     * @return lista pozycji graczy
     */
    public int [] getUbitychPozycje(){
        if(ubici==null || ubici.size()<1)
            return null;
        int [] up=new int[ubici.size()];
        for(int i=0;i<ubici.size();++i){
            up[i]=ubici.get(i).getPos();
        }
        ubici=new ArrayList<Gracz>();
        return up;
    }
    
    /**
     * Porusza gracza w wybranym kierunku gdy jest to możliwe.
     * @param ktorego id gracza którego należy przesunąć
     * @param jak dodatnie do przodu inaczej do tyłu
     * @return wartość o jaką zmieniono pozycję gracza. Zero gdy operacja nie
     * miała miejsca.
     */
    public int ruszGracza(long ktorego,int jak){
        int dx=0;
        if(gracze==null)
            return 0;
        for(int i=0;i<gracze.length;++i){
            if(gracze[i].getId()==ktorego){
                if(jak>0 && gracze[i].getPos()<teren.length*x_res-50){
                    dx=20;
                    gracze[i].setPos(gracze[i].getPos()+dx);
                }
                else if(gracze[i].getPos()>50){
                    dx=-20;
                    gracze[i].setPos(gracze[i].getPos()+dx);
                }
                break;
            }
        }
        return dx;
    }
}
