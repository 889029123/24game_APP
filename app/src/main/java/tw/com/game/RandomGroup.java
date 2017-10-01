package tw.com.game;

/**
 * Created by ASUS on 2016/9/24.
 */
public class RandomGroup {
    int times,min,max;
    int []q;
    RandomGroup(int m, int dataLength){
        times=-1;
        q=new int [dataLength];
        min=m;
        max=dataLength;
        rand();
    }
    void rand(){
        int num=max-min, ra;
        int []a=new int [num];
        for(int i=0; i<num; i++) a[i]=i+min;
        for(int i=0; i<max; i++){
            if(q[i]!=0){
                for(int j=q[i]-(i+1); j<num-1; j++) a[j]=a[j+1];
                num--;
            }else{
                ra=(int) (Math.random()*num);
                q[i]=a[ra];
                for(int j=ra; j<num-1; j++) a[j]=a[j+1];
                num--;
            }
        }
    }
    int getNumber(){
        //if(q[times++]==max) times++;
        times++;//取一次
        return q[times];
    }
    void reset(){
        times=-1;
    }
    void allReset(){
        reset();
        for(int i=min; i<max; i++) q[i]=0;
    }
}