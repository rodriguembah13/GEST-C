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
}
