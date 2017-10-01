package tw.com.game;

import android.widget.Button;

import java.util.StringTokenizer;

/**
 * Created by ASUS on 2016/9/24.
 */
public class BtnData {//控制按鈕介面是否按過與按過哪個
    Button btn;
    boolean boolBtn;
    int btnPositionID, colorBtnLive, colorBtnUsed;//Live= 0xffd6d7d7白色,Used= 0xff505050灰色
    String btnValue, btnFieldID;
    BtnData(Button b, int ID){
        btn=b;
        btnPositionID=ID;
        switch (ID){
            case 0:
                colorBtnLive=0xFFFFD633;
                colorBtnUsed=0xFFFFFF71;
                break;
            case 1:
                colorBtnLive=0xFFFFAA80;
                colorBtnUsed=0xFFFFC9B0;
                break;
            case 2:
                colorBtnLive=0xFF5DD55D;
                colorBtnUsed=0xFFC6FF9F;
                break;
            case 3:
                colorBtnLive=0xFF33ADFF;
                colorBtnUsed=0xFF99F2FF;
                break;
        }

    }
    int clicked(){//被按過之後設定為false，並且回傳按鈕已經被按過了的顏色
        boolBtn=false;
        return colorBtnUsed;
    }
    int canPress(){//設定為可按按鈕的狀態，並且回傳按鈕還沒被按過的顏色
        boolBtn=true;
        return colorBtnLive;
    }
    boolean canItPress(){
        return boolBtn;
    }
    int btnPositionID(){return btnPositionID;}
    void setFieldInBtnData(String ID, int value){//設定按鈕一開始的值與在資料庫中的欄位
        btnFieldID = ID;
        btnValue = value + "/1";
    }
    void setBtnValue(String s){//記住運算過後的按鈕值
        btnValue=s;
    }
    String setBtnValue(){//用來設定按鈕上的值
        StringTokenizer fenxi = new StringTokenizer(String.valueOf(btnValue), "/");
        int data1 = Integer.parseInt(fenxi.nextToken());
        int data2 = Integer.parseInt(fenxi.nextToken());
        if(data2==1) return ""+data1;
        else return data1+"/"+data2;
    }
    void release(){
        btn=null;
        btnValue=null;
        btnPositionID=0;
    }

}
