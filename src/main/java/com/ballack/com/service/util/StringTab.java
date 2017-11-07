package com.ballack.com.service.util;

/**
 * Created by ballack on 31/10/2017.
 */
public class StringTab {

    public char [] getTabString(String exemp){

       char rep[]=exemp.toCharArray();
        return rep;
    }
    public int getIntTab(char val){
        int resp = 0;
        if (val=='a'|| val=='f'|| val=='m'){
            resp=1;
        }else if (val=='b'|| val=='u'){
            resp=0;
        }else if (val=='d'|| val=='g'|| val=='i'){
            resp=2;
        }else if (val=='e'|| val=='h'|| val=='j'){
            resp=3;
        }else if (val=='l'|| val=='y'|| val=='o'){
            resp=4;
        }else if (val=='q'|| val=='r'|| val=='p'){
            resp=5;
        }else if (val=='v'|| val=='s'){
            resp=6;
        }else if (val=='w'|| val=='k'){
            resp=7;
        }else if (val=='t'|| val=='c'|| val=='n'){
            resp=8;
        }else if (val=='z'|| val=='x'){
            resp=9;
        }
        return resp;
    }
    public String getString(int[] entre){
         String retour="";
        for (int anEntre : entre) {
            retour += anEntre;
            //System.out.println(retour);
        }
        return retour;
    }
    public String getString(String s,int t){
        int[] tab = new int [t];
        char [] tabchar=getTabString(s.toLowerCase().trim());
        int taille=tabchar.length;
        int[] tabtemp = new int [taille];
        if(taille<t){
            int temp=t-taille;
            int[] tabtemp2 = new int [temp];
            for (int i = 0; i < taille; i++) {
                tabtemp[i] = getIntTab(tabchar[i]);
                //System.out.println(tab[i]);
            }
            for (int i = 0; i < temp; i++) {
                tabtemp2[i] = 0;

            }
            //copie du premier tableau
            System.arraycopy(tabtemp, 0, tab, 0, tabtemp.length);
            //copie du deuxième tableau tableau
            System.arraycopy(tabtemp2, 0, tab, tabtemp.length, tabtemp2.length);
            return getString(tab);
        }else {
            for (int i = 0; i < t; i++) {
                tab[i] = getIntTab(tabchar[i]);
                //System.out.println(tab[i]);
            }
            return getString(tab);
        }


    }
}
