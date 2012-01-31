/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author andrzej
 */
public class Fizyka {
    public static int [] teren;
    public static int x_res;
    public static String trajektoria(int ix0,int iv0,int ialfa){
        ialfa=-ialfa;
        int iy0=igrek(ix0)+20;
        String traj=ix0+" "+iy0+" ";
        int wezel=ix0/x_res;
        int i;
        int di=ialfa<90?1:-1;
        
        Fizyka.x0=ix0;
        Fizyka.y0=iy0;
        Fizyka.v0=iv0;
        Fizyka.alfa=Math.PI*(ialfa/180.);
        
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
        double t=0.5;
        //boolean stanx=x>x0;
        //boolean stany=y>y0;
        while(true){
            double xodt=x0+Math.cos(alfa)*v0*t;
            double yodt=y0+Math.sin(alfa)*v0*t-5.*t*t;
            if(igrek((int)xodt)>yodt)
                break;                      
            traj+=(int)xodt+" "+(int)yodt+" ";
            t+=0.5;
            /*if((Math.abs(xodt-x)<4 && Math.abs(yodt-y)<4) ||
                    ((xodt<x)!=stanx && (yodt<y)!=stany))
                break;*/
            if(t>30)
                break;
        }
        //traj+=(int)x+" "+(int)y;
        return traj;
    }
    public static int igrek(int x){
        int wn=x/x_res;
        int wn1=wn==teren.length-1?wn:wn+1;
        return teren[wn]+
                (int)((teren[wn1]-teren[wn])*((double)(x%x_res)/(double)x_res));
    }
    
    static double x0;
    static double y0;
    static double v0;
    static double alfa;
    
    static double parabola(int x){
        return y0+Math.tan(alfa)*(x-x0)-5*Math.pow((x-x0)/(Math.cos(alfa)*v0),2);
    }
}
