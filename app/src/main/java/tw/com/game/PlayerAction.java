package tw.com.game;

import java.util.StringTokenizer;

/**
 * Created by ASUS on 2016/9/24.
 */
public class PlayerAction {
    int totalMoves, limitMoves;
    int []iWasPressedBtn=new int [2];//記住玩家所按過的Btn代號
    String []playerValue=new String [2];//記住玩家所按過的值
    String iWasPressedOperation;//記住玩家按過的運算元
    String [] undoValue=new String[2];//記住玩家按過的值
    int []undoID=new int [2];
    String []btnFID=new String[2];
    Calculator cal=new Calculator();
    PlayerAction(){
        totalMoves=0;
        iWasPressedBtn[0]=iWasPressedBtn[1]=-1;
        playerValue[0]=playerValue[1]="";
    }
    void setiWasPressedStep1(int n){//設定第一步所按的按鈕ID
        iWasPressedBtn[0]=n;
        if(totalMoves==1) undoID[0]=n;
    }
    void setiWasPressedStep2(int n){//設定第二步所按的按鈕ID
        totalMoves++;
        iWasPressedBtn[1]=n;
        if(totalMoves==2) undoID[1]=n;
    }
    void setPressBtnValueStep1(String m){//用來undo
        playerValue[0]=m;
        if(totalMoves==1) undoValue[0]=m;
    }
    void setPressBtnValueStep2(String m){//用來undo
        playerValue[1]=m;
        if(totalMoves==2) undoValue[1]=m;
    }
    void setFidStep1(String s){
        btnFID[0]=s;
    }
    void setFidStep2(String s){
        btnFID[1]=s;
    }
    String cacu(){
        return cal.compute(""+playerValue[0],iWasPressedOperation,""+playerValue[1]);
    }
    void setiWasPressedOperation(String s){
        if(iWasPressedBtn[0]!=-1)
            iWasPressedOperation=s;
    }
    int playerTotalMoves(){
        return totalMoves;
    }
    int playerLimitMoves(){return limitMoves;}
    int pressBtnIDStep1(){
        return iWasPressedBtn[0];
    }
    int pressBtnIDStep2(){
        return iWasPressedBtn[1];
    }
    boolean pressedOperation(){//是否按過運算元按鈕，按過迴傳true
        if("".equals(iWasPressedOperation)){
            return false;
        }else{
            return true;
        }
    }
    void resetValue(){
        playerValue[0]=playerValue[1]=null;
        iWasPressedBtn[0]=iWasPressedBtn[1]=-1;
        iWasPressedOperation="";
        btnFID[0]=btnFID[1]=null;
    }
    void allReset(){
        resetValue();
        totalMoves=0;
    }
    void setLimitMoves(int n){
        limitMoves=4-n-1;
    }

    String playerValueData(int n){
        StringTokenizer fenxi = new StringTokenizer(String.valueOf(playerValue[n]), "/");
        int data1 = Integer.parseInt(fenxi.nextToken());
        int data2 = Integer.parseInt(fenxi.nextToken());
        if(data2==1) return ""+data1;
        else return data1+"/"+data2;
    }

    void release(){
        playerValue[0]=playerValue[1]=null;
        iWasPressedOperation=null;
        undoValue[0]=undoValue[1]=null;
        btnFID[0]=btnFID[1]=null;
    }
}
