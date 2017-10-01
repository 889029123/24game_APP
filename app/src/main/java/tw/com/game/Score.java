package tw.com.game;


/**
 * Created by ASUS on 2016/9/26.
 */
public class Score {
    int lastSeconds, finishSeconds, totalScore=0, combo=0, limitCombo=0;
    boolean win;
    Score(){
        resetScore();
    }
    void resetScore(){
        //finishSeconds=0;
        //lastSeconds=0;
        win=false;
        totalScore=0;
        combo=0;
        limitCombo=0;
    }
    void setLastSeconds(int n){
        lastSeconds=n;
    }
    void setFinishSeconds(int n){finishSeconds=n;}
    int totalScore(){
        int tmp=Math.abs(lastSeconds-finishSeconds);
        /*Log.d("MainActivity", "========"+lastSeconds);
        Log.d("MainActivity", "========"+finishSeconds);
        Log.d("MainActivity", "========"+tmp);*/
        if(tmp<=12 && win) {
            if(combo==0) {
                totalScore++;
                combo++;
                limitCombo=1;
            }else{
                limitCombo = ++combo;
                totalScore+=limitCombo;
            }
        }
        else if(win) {
            totalScore+=1;
            combo=0;
            limitCombo=0;
        }
        else{
            totalScore+=0;
            combo=0;
            limitCombo=0;
        }
        win=false;
        return totalScore;
    }
    void win(){
        win=true;
    }
}
