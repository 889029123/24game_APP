package tw.com.game;

/**
 * Created by ASUS on 2016/9/26.
 */
public class Life {
    int totalLife;
    Life(){
        reset();
    }
    boolean isLive(){
        if(totalLife==0) return false;
        else return true;
    }
    void reset(){
        totalLife=10;
    }
    void used(){
        totalLife--;
    }
}
