package tw.com.game;

import java.util.StringTokenizer;

/**
 * Created by ASUS on 2016/9/25.
 */
public class FieldProcessing {//從資料庫拿到資料後所做的事
    String fieldID,valueInString;
    int fieldValue,position;
    void initialFieldProcessing(String name, int value, int p){//資料庫中的欄位名稱、資料庫中欄位名稱的值、以及在按鈕上的位置
        fieldID=name;
        fieldValue=value;
        position=p;
        valueInString=""+fieldValue;
    }
    int position(){
        if(position==0) return 0;
        else if(position==1) return 1;
        else if(position==2) return 2;
        else return 3;
    }
    String btnValue(){
        return valueInString;
    }
    void release(){
        fieldID=null;
        valueInString=null;
        position=0;
        fieldValue=0;
    }
}
