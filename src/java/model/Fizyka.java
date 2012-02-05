/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *Klasa enkapsuluje funkcje związane z fizyką gry (przede wszystkim trajektorie).
 * @author andrzej
 */
public class Fizyka {
    public int [] teren;//tu trzeba podpiąć wskaźnik do tablicy wysokości węzłów terenu
    public int x_res;//tu trzeba przypisać odległość x pomiędzy węzłami terenu
    /**
     * Zwraca ciąg punktów należących do trajektorii pocisku w postaci ciągu znaków
     * nadającego się bezpośrednio do przekazania klientowi javascript. Ciąg: "x0 y0
     * x1 y1 ... xn yn" gdzie x0 i y0 to pozycja czołgu który strzela, a xn yn to 
     * punkt w którym eksploduje pocisk (okolica zderzenia trajektorii z terenem -
     * TODO: polepszyć detekcję zderzeń żeby trafienia były dokładniejsze).
     * Próbkowanie jest ze stałym czasem dt.
     * @param ix0 pozycja x czołgu 
     * @param iv0 "siła strzału" := prędkość początkowa pocisku
     * @param ialfa kąt strzału w stopniach zgodnie z ruchem wskazówek zegara (czyli będą ujemne)
     * @return string w sam raz do wysłania klientowi (no, bez nagłówka)
     */
    public String trajektoria(int ix0,int iv0,int ialfa){
       // return "0 0 100 100 200 200 300 300 400 200 500 300 600 200";
        ialfa=-ialfa;
        int iy0=igrek(ix0)+20;//pozycja początkowa y czołgu
        String traj=ix0+" "+iy0+" ";//pierwszy punkt trajektorii
        int wezel=ix0/x_res;
        int i;
        int di=ialfa<90?1:-1;
        /*przepisuję na double dla dokładniejszych obliczeń*/
        x0=ix0;
        y0=iy0;
        v0=iv0;
        alfa=Math.PI*(ialfa/180.);
        /*Tu miała być mądrzejsza detekcja kolizji ale nie wyszła*/
        /*for(i=wezel;(alfa<90 && i<teren.length)||(alfa>=90 && i>-1);i+=di){
            if(teren[i]>parabola(i*x_res))
                break;
        }
        i=i>teren.length-1?teren.length-1:i<0?0:i;
        int x=i*x_res;
        int y=igrek(x);
        if(di>0 && i>0){
            int dx=x_res/2;
            x=igrek(x-dx)>parabola(x-dx)?x-dx:x;
            dx=dx/2;
            x=igrek(x-dx)>parabola(x-dx)?x-dx:x;
            dx=dx/2;
            x=igrek(x-dx)>parabola(x-dx)?x-dx:x;
            dx=dx/2;
            x=igrek(x-dx)>parabola(x-dx)?x-dx:x;
            dx=dx/2;
            x=igrek(x-dx)>parabola(x-dx)?x-dx:x;
            y=(int) parabola(x);
        }
        else if(di<0 && i<teren.length-1){
            int dx=x_res/2;
            x=igrek(x+dx)>parabola(x+dx)?x+dx:x;
            dx=dx/2;
            x=igrek(x+dx)>parabola(x+dx)?x+dx:x;
            dx=dx/2;
            x=igrek(x+dx)>parabola(x+dx)?x+dx:x;
            dx=dx/2;
            x=igrek(x+dx)>parabola(x+dx)?x+dx:x;
            dx=dx/2;
            x=igrek(x+dx)>parabola(x+dx)?x+dx:x;
            y=(int) parabola(x);
        }*/
        double dt=0.5;
        double t=dt;
        //boolean stanx=x>x0;
        //boolean stany=y>y0;
        while(true){
            double xodt=x0+Math.cos(alfa)*v0*t;//rzut ukośny duuuh..
            double yodt=y0+Math.sin(alfa)*v0*t-5.*t*t;
            if(igrek((int)xodt)>yodt){//z krokiem dt czekamy aż pocisk wejdzie w ziemię
                double niedalej=t-dt;
                for(t=t-.1*dt;t>niedalej;t-=.1*dt){
                    xodt=x0+Math.cos(alfa)*v0*t;
                    yodt=y0+Math.sin(alfa)*v0*t-5.*t*t;
                    if(igrek((int)xodt)<yodt)//z mniejszym krokiem wyciągamy pocisk z ziemi
                        break;
                }
                traj+=(int)xodt+" "+(int)yodt+" ";//w tym miejscu pocisk ekploduje
                break;                      
            }
            traj+=(int)xodt+" "+(int)yodt+" ";//kolejny punkt trajektorii pocisku
            t+=dt;
            /*if((Math.abs(xodt-x)<4 && Math.abs(yodt-y)<4) ||
                    ((xodt<x)!=stanx && (yodt<y)!=stany))
                break;*/
            if(t>30)//zabezpieczenie aby pocisk nie wyleciał na orbitę
                break;
        }
        //traj+=(int)x+" "+(int)y;
        return traj;
    }
    /**
     * Metoda zwraca współrzędną y terenu na podstawie położenia x
     * @param x współrzędna x
     * @return współrzędna y terenu (wyskokość w miejscu x)
     */
    public int igrek(int x){
        int wn=x/x_res;
        wn=wn<0?0:wn>teren.length-1?teren.length-1:wn;
        int wn1=wn==teren.length-1?wn:wn+1;
        return teren[wn]+
                (int)((teren[wn1]-teren[wn])*((double)(x%x_res)/(double)x_res));
    }
    
    double x0;
    double y0;
    double v0;
    double alfa;
    
    /**
     * Nieużywana w tej chwili metoda do obliczania pozycji y pocisku w zależności
     * od pozycji x. 
     * @param x
     * @return 
     */
    double parabola(int x){
        return y0+Math.tan(alfa)*(x-x0)-5*Math.pow((x-x0)/(Math.cos(alfa)*v0),2);
    }
}
