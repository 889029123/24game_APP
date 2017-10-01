package tw.com.game;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
/**
 * Created by ASUS on 2016/10/6.
 */
public class PlayerData {
    String []userLog=new String[3];
    String eid, imei, t1, t2;
    //int lastSeconds, finishSeconds;
    boolean bF;
    //Date d = new Date();
    String []actionTime=new String[3];
    PlayerData(){
        reset();
    }
    void setIMEI(String s){imei=s;}
    void setEID(String n){
        eid=n;
    }
    void setUserLog(int totalMoves, String s){
        userLog[totalMoves-1] =s;
    }
    /*void setLastSeconds(int n){
        lastSeconds=n;
        CharSequence s  = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.getTime());
        t1= (String) s;
    }*/
    void setLastSeconds(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-dd+kk:mm:ss", d.getTime());
        t1= (String) s;
        d=null;
    }
    /*void setFinishSeconds(int n, int m){
        finishSeconds=n;
        m--;
        actionTime[m]= String.valueOf(Math.abs(lastSeconds-finishSeconds));
        CharSequence s1  = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.getTime());
        t2= (String) s1;
        actionTime[m]= String.valueOf(t2);
    }*/
    void setFinishSeconds(){
        Date d = new Date();
        CharSequence s1  = DateFormat.format("yyyy-MM-dd+kk:mm:ss", d.getTime());
        t2= (String) s1;
        d=null;
    }
    void isRight(boolean b){bF=b;}
    String tOrF(){
        if(bF) return "1";
        else return "0";
    }
    String actionTime(int n){
        n-=1;
        return actionTime[n];
    }
    String startTime(){
        return t1;
    }
    String endTime(){
        return t2;
    }
    void reset(){
        for(int i=0; i<3; i++) userLog[i]="";
    }
}
