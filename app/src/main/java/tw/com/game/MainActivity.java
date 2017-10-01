package tw.com.game;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity {
    //-----------------------------物件宣告--------------------------------------
    Button A, B, C, D, plus, minus, time, division, skip, hint;
    TextView score, stime;
    ImageView comImg;
    CountDownTimer countDownTimer;
    String eid, imei, playerName="";
    int totalTime=180,recordTimeSeconds, version, getHintTimes=0, problem=0;
    private GoogleApiClient client;
    int []btnValue=new int [4];
    boolean yOrN=true, firstRound=true, connect=true;
    String []lM = new String [2];//取代
    ArrayList<String> array = new ArrayList<String>();

    //-------------------------------------------------------------------------------
    //-----------------------------類別宣告--------------------------------------
    MediaPlayer music;
    Topic topic = new Topic();
    BtnData btnData0=new BtnData(A, 0);
    BtnData btnData1=new BtnData(B, 1);
    BtnData btnData2=new BtnData(C, 2);
    BtnData btnData3=new BtnData(D, 3);

    RandomGroup randomPosition=new RandomGroup(0,4);//0~3 共4格
    Score myScore=new Score();
    Life life=new Life();
    PlayerAction playerAction=new PlayerAction();
    PlayerData playerData=new PlayerData();
    FieldProcessing fieldA = new FieldProcessing();
    FieldProcessing fieldB = new FieldProcessing();
    FieldProcessing fieldC = new FieldProcessing();
    FieldProcessing fieldD = new FieldProcessing();
    //-------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findViews();
        comImg.setImageResource(R.drawable.combo);
        version = getIntent().getIntExtra("VERSION", 1);//1為預設值
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        TelephonyManager tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        //imei=tm.getDeviceId();
        //imei="000111222333444";//測試用
        try {
            imei=getIMEI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, String.valueOf(imei.toString()), Toast.LENGTH_SHORT).show();
        playerData.setIMEI(imei);
        //取得IMEI
        allReset();
        skip.setText(""+life.totalLife);
        try
        {
            music = MediaPlayer.create(this, R.raw.music);
            music.setAudioStreamType(AudioManager.STREAM_MUSIC);
            music.setLooping(true);
        }catch (IllegalStateException e)
        {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        rankingData();
    }

            @Override
            protected void onResume()
            {
        // TODO Auto-generated method stub
                super.onResume();
                music.start();
            }

            @Override
            protected void onPause()
            {
        // TODO Auto-generated method stub
                super.onPause();
                music.pause();
            }

            @Override
            protected void onDestroy()
            {
        // TODO Auto-generated method stub
                super.onDestroy();
                music.release();
            }
    private String getIMEI() throws Exception {
        TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method method = mTelephonyMgr.getClass().getMethod("getImei");
            String imei = (String) method.invoke(mTelephonyMgr);
            if (imei == null) {
                throw new Exception();
            } else {
                return imei;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    //=====================================按鈕設定====================================

    private void findViews() {
        A = (Button) findViewById(R.id.A);
        B = (Button) findViewById(R.id.B);
        C = (Button) findViewById(R.id.C);
        D = (Button) findViewById(R.id.D);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        time = (Button) findViewById(R.id.time);
        division = (Button) findViewById(R.id.division);
        score = (TextView) findViewById(R.id.score);
        stime = (TextView) findViewById(R.id.stime);
        skip = (Button) findViewById(R.id.skip);
        hint=(Button) findViewById(R.id.hint);
        comImg=(ImageView) findViewById(R.id.imageView);
    }
    void allBtnCanPress(){//設定所有按鈕皆可按
        A.setBackgroundColor(btnData0.canPress());
        B.setBackgroundColor(btnData1.canPress());
        C.setBackgroundColor(btnData2.canPress());
        D.setBackgroundColor(btnData3.canPress());
    }

    public void A(View v) {
        if(A.getText().equals("0") && playerAction.iWasPressedOperation.equals("/")) {
            Toast.makeText(getApplicationContext(), "邏輯錯誤", Toast.LENGTH_LONG).show();
        }
        else {
            if (btnData0.canItPress() && A.getText() != "") {//判斷是否可以按
                if (playerAction.pressedOperation() == false) {//判斷是否為第一步(還未按過運算元的按鈕)
                    step1(A, btnData0);
                    stp1_OtherCanPress(0);
                } else if (playerAction.pressedOperation() && btnData0.canItPress()) {
                    step2(A, btnData0);
                }
            } else if (btnData0.canItPress() == false && playerAction.pressBtnIDStep1() == btnData0.btnPositionID()) {//不能按的話判斷是否要取消按下此按鈕
                A.setBackgroundColor(btnData0.canPress());
                playerAction.setiWasPressedStep1(-1);
                playerAction.iWasPressedOperation = "";
            }
        }
    }
    public void B(View v) {
        if(B.getText().equals("0") && playerAction.iWasPressedOperation.equals("/")) {
            Toast.makeText(getApplicationContext(), "邏輯錯誤", Toast.LENGTH_LONG).show();
        }
        else {
            if (btnData1.canItPress() && B.getText() != "") {
                if (playerAction.pressedOperation() == false) {//判斷是否為第一步(還未按過運算元的按鈕)
                    step1(B, btnData1);
                    stp1_OtherCanPress(1);
                } else if (playerAction.pressedOperation() && btnData1.canItPress()) {
                    step2(B, btnData1);
                }
            } else if (btnData1.canItPress() == false && playerAction.pressBtnIDStep1() == btnData1.btnPositionID()) {//不能按的話判斷是否要取消按下此按鈕
                B.setBackgroundColor(btnData1.canPress());
                playerAction.setiWasPressedStep1(-1);
                playerAction.iWasPressedOperation = "";
            }
        }
    }
    public void C(View v) {
        if(C.getText().equals("0") && playerAction.iWasPressedOperation.equals("/")) {
            Toast.makeText(getApplicationContext(), "邏輯錯誤", Toast.LENGTH_LONG).show();
        }
        else {
            if (btnData2.canItPress() && C.getText() != "") {
                if (playerAction.pressedOperation() == false) {//判斷是否為第一步(還未按過運算元的按鈕)
                    step1(C, btnData2);
                    stp1_OtherCanPress(2);
                } else if (playerAction.pressedOperation() && btnData2.canItPress()) {
                    step2(C, btnData2);
                }
            } else if (btnData2.canItPress() == false && playerAction.pressBtnIDStep1() == btnData2.btnPositionID()) {//不能按的話判斷是否要取消按下此按鈕
                C.setBackgroundColor(btnData2.canPress());
                playerAction.setiWasPressedStep1(-1);
                playerAction.iWasPressedOperation = "";
            }
        }
    }
    public void D(View v) {
        if(D.getText().equals("0") && playerAction.iWasPressedOperation.equals("/")) {
            Toast.makeText(getApplicationContext(), "邏輯錯誤", Toast.LENGTH_LONG).show();
        }
        else {
            if (btnData3.canItPress() && D.getText() != "") {
                if (playerAction.pressedOperation() == false) {//判斷是否為第一步(還未按過運算元的按鈕)
                    step1(D, btnData3);
                    stp1_OtherCanPress(3);
                } else if (playerAction.pressedOperation() && btnData3.canItPress()) {
                    step2(D, btnData3);
                }
            } else if (btnData3.canItPress() == false && playerAction.pressBtnIDStep1() == btnData3.btnPositionID()) {//不能按的話判斷是否要取消按下此按鈕
                D.setBackgroundColor(btnData3.canPress());
                playerAction.setiWasPressedStep1(-1);
                playerAction.iWasPressedOperation = "";
            }
        }
    }

    public void plus(View v) {
        if (playerAction.pressBtnIDStep1() != -1) {
            playerAction.setiWasPressedOperation("+");
            Toast.makeText(this, String.valueOf(plus.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    public void minus(View v) {
        if (playerAction.pressBtnIDStep1() != -1) {
            playerAction.setiWasPressedOperation("-");
            Toast.makeText(this, String.valueOf(minus.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    public void time(View v) {//顯示時間
        if (playerAction.pressBtnIDStep1() != -1) {
            playerAction.setiWasPressedOperation("*");
            Toast.makeText(this, String.valueOf(time.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    public void division(View v) {
        if (playerAction.pressBtnIDStep1() != -1) {
            playerAction.setiWasPressedOperation("/");
            Toast.makeText(this, String.valueOf(division.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    public void undo(View v){
        //Log.d("MainActivity","======1========"+ playerData.userLog[playerAction.playerTotalMoves()-1]);
        if(playerAction.playerTotalMoves()>0) {
            if (playerAction.pressBtnIDStep1() != -1) {
                if (playerAction.pressBtnIDStep1() == btnData0.btnPositionID())
                    A.setBackgroundColor(btnData0.canPress());
                else if (playerAction.pressBtnIDStep1() == btnData1.btnPositionID())
                    B.setBackgroundColor(btnData1.canPress());
                else if (playerAction.pressBtnIDStep1() == btnData2.btnPositionID())
                    C.setBackgroundColor(btnData2.canPress());
                else if (playerAction.pressBtnIDStep1() == btnData3.btnPositionID())
                    D.setBackgroundColor(btnData3.canPress());
            }
            if (playerAction.playerTotalMoves() == 1) {
                reset();
                //userlog[playerAction.playerTotalMoves()]="";
                playerAction.totalMoves--;
                playerAction.resetValue();
            } else if (playerAction.playerTotalMoves() == 2) {
                for (int i = 0; i < 2; i++) {
                    if (playerAction.undoID[i] == 0) {
                        btnData0.setBtnValue(playerAction.undoValue[i]);
                        A.setBackgroundColor(btnData0.canPress());
                        A.setText(btnData0.setBtnValue());
                    } else if (playerAction.undoID[i] == 1) {
                        btnData1.setBtnValue(playerAction.undoValue[i]);
                        B.setBackgroundColor(btnData1.canPress());
                        B.setText(btnData1.setBtnValue());
                    } else if (playerAction.undoID[i] == 2) {
                        btnData2.setBtnValue(playerAction.undoValue[i]);
                        C.setBackgroundColor(btnData2.canPress());
                        C.setText(btnData2.setBtnValue());
                    } else if (playerAction.undoID[i] == 3) {
                        btnData3.setBtnValue(playerAction.undoValue[i]);
                        D.setBackgroundColor(btnData3.canPress());
                        D.setText(btnData3.setBtnValue());
                    }
                }
                recordLM1=recordLM2;
                //userlog[playerAction.playerTotalMoves()]="";
                playerAction.totalMoves--;
                playerAction.resetValue();
            }
        }
    }
    boolean fromSkip = false;
    public void skipbtn(View v){
        if(life.isLive()){
            playerAction.totalMoves=playerAction.playerLimitMoves();
            fromSkip=true;
            myScore.win=false;
            goNextQuestion(btnData0);
        }
        if(life.totalLife==0) {
            countDownTimer.cancel();
            skip.setText("遊戲結束");
            stime.setText("Game Over");
            goRankView();
        }
    }

    public void resetbtn(View v){
        firstRound=true;
        totalTime=180;
        countDownTimer.cancel();
        life.reset();
        randomPosition.allReset();
        randomPosition.rand();
        allReset();
        myScore.resetScore();
        playerScore();
        skip.setText(""+life.totalLife);
    }

    public void  hint(View v){//public synchronized   throws InterruptedException
        String toDB="";
        if(version==1 && getHintTimes>0) return;
        else
        if (life.isLive() && version!=3) {//3代表沒有提示 2代表提示全部 1代表提示一點點
            switch (version){
                case 1:
                    if(getHintTimes==0) {
                        getHintTimes++;
                        life.used();
                        if (playerAction.playerTotalMoves() == 0) {
                            toDB = "http://203.68.252.72:80/24game/ans.php?INDEX=9&EID=" + eid;
                        } else if (playerAction.playerTotalMoves() == 1) {
                            toDB = "http://203.68.252.72:80/24game/ans.php?INDEX=1&EID=" + eid + "&L=" + lM[0];
                        } else if (playerAction.playerTotalMoves() == 2) {
                            toDB = "http://203.68.252.72:80/24game/ans.php?INDEX=2&EID=" + eid + "&L=" + lM[0] + "&M=" + lM[1];
                        }

                        toHintV1(toDB);
                    }
                    break;
                case 2:
                    if(getHintTimes==0) {
                        getHintTimes++;
                        life.used();
                        toDB = "http://203.68.252.72:80/24game/subans.php?EID=" + eid;
                        Log.d("MMMMMMM", toDB);
                        toHintV2(toDB);
                    }
                    break;
            }
            toDB=lM[0]=lM[1]=null;
            //life.used();
            if(life.isLive()) skip.setText(""+life.totalLife);
            else {
                countDownTimer.cancel();
                skip.setText("遊戲結束");
                stime.setText("Game Over");
                goRankView();
            }
        }
    }

    //======================================副程式============================================

    void stp1_OtherCanPress(int n){
        switch (n){
            case 0:
                if(B.getText().equals("")==false) B.setBackgroundColor(btnData1.canPress());
                if(C.getText().equals("")==false) C.setBackgroundColor(btnData2.canPress());
                if(D.getText().equals("")==false) D.setBackgroundColor(btnData3.canPress());
                break;
            case 1:
                if(A.getText().equals("")==false) A.setBackgroundColor(btnData0.canPress());
                if(C.getText().equals("")==false) C.setBackgroundColor(btnData2.canPress());
                if(D.getText().equals("")==false) D.setBackgroundColor(btnData3.canPress());
                break;
            case 2:
                if(A.getText().equals("")==false) A.setBackgroundColor(btnData0.canPress());
                if(B.getText().equals("")==false) B.setBackgroundColor(btnData1.canPress());
                if(D.getText().equals("")==false) D.setBackgroundColor(btnData3.canPress());
                break;
            default:
                if(A.getText().equals("")==false) A.setBackgroundColor(btnData0.canPress());
                if(B.getText().equals("")==false) B.setBackgroundColor(btnData1.canPress());
                if(C.getText().equals("")==false) C.setBackgroundColor(btnData2.canPress());
        }
    }

    void step1(Button btn, BtnData btnData){
        playerAction.setiWasPressedStep1(btnData.btnPositionID());//記住玩家第一次按的按鈕位置為何
        playerAction.setPressBtnValueStep1(btnData.btnValue);//記住玩家按下的這個按鈕的值
        playerAction.setFidStep1(btnData.btnFieldID);//記住資料庫中的欄位名稱
        btn.setBackgroundColor(btnData.clicked());
    }
    int recordLM1=-2, recordLM2;
    String reans;
    String userlogFinal;
    //String []userlog = new String[2];

    void step2(Button btn, BtnData btnData){
        playerAction.setiWasPressedStep2(btnData.btnPositionID());
        playerAction.setPressBtnValueStep2(btnData.btnValue);//記住玩家按下的這個按鈕的值
        playerAction.setFidStep2(btnData.btnFieldID);//記住資料庫中的欄位名稱
        btnData.setBtnValue(playerAction.cacu());
        //================================串================================================
        if(playerAction.playerTotalMoves()==1){
            reans="%22"+playerAction.playerValueData(0)+""+judgementOperationToURL()+""+playerAction.playerValueData(1)+"%22";
            playerData.setUserLog(playerAction.playerTotalMoves(), playerAction.btnFID[0]+judgementOperationToURL2()+playerAction.btnFID[1]);
        }else{
            if(recordLM1==playerAction.iWasPressedBtn[1]){
                reans="%22"+playerAction.playerValueData(0)+judgementOperationToURL()+"%22";
                playerData.setUserLog(playerAction.playerTotalMoves(), playerAction.btnFID[0]+judgementOperationToURL2());
            }else if(recordLM1==playerAction.iWasPressedBtn[0]){
                reans="%22"+judgementOperationToURL()+playerAction.playerValueData(1)+"%22";
                playerData.setUserLog(playerAction.playerTotalMoves(), judgementOperationToURL2()+playerAction.btnFID[1]);
            }else{
                reans="%22"+playerAction.playerValueData(0)+""+judgementOperationToURL()+""+playerAction.playerValueData(1)+"%22";
                playerData.setUserLog(playerAction.playerTotalMoves(), playerAction.btnFID[0]+judgementOperationToURL2()+playerAction.btnFID[1]);
            }
        }

        if(playerAction.playerTotalMoves()==1){
            recordLM1=playerAction.iWasPressedBtn[1];
            lM[0]=reans;
        }else if(playerAction.playerTotalMoves()==2){
            recordLM2=recordLM1;
            recordLM1=playerAction.iWasPressedBtn[1];
            lM[1]=reans;
        }
        reans=null;
        //=================================================================================
        pressed();
        playerData.setFinishSeconds();
        stringTogether();

        playerAction.resetValue();
        goNextQuestion(btnData);
        btn.setText(btnData.setBtnValue());
    }

    void stringTogether(){
        String url = null;
        switch (playerAction.playerLimitMoves()){
            case 1:
                url="http://203.68.252.72:80/24game/userlogroutes.php?"+"sendr1="+playerData.imei+"&sendr2="+
                        playerData.startTime()+"&sendr3="+playerData.eid+"&sendr4="+playerData.endTime()+
                        "&sendr5="+playerData.userLog[0]+"&sendr6=N"+"&sendr7=N";
                userlogFinal=url;
                break;
            case 2:
                if(playerAction.playerTotalMoves()==1) {
                    url = "http://203.68.252.72:80/24game/userlogroutes.php?" + "sendr1=" + playerData.imei + "&sendr2=" +
                            playerData.startTime() + "&sendr3=" + playerData.eid + "&sendr4=" + playerData.endTime() +
                            "&sendr5=" + playerData.userLog[0]+"&sendr6=N"+"&sendr7=N"+"&sendr8=2";
                    array.add(url);
                }else{
                    url = "http://203.68.252.72:80/24game/userlogroutes.php?" + "sendr1=" + playerData.imei + "&sendr2=" +
                            playerData.startTime() + "&sendr3=" + playerData.eid + "&sendr4=" + playerData.endTime() +
                            "&sendr5=" + playerData.userLog[0] + "&sendr6=" + playerData.userLog[1]+"&sendr7=N";
                    userlogFinal=url;
                }
                break;
            default:
                if(playerAction.playerTotalMoves()==1){
                    url="http://203.68.252.72:80/24game/userlogroutes.php?"+"sendr1="+playerData.imei+"&sendr2="+
                            playerData.startTime()+"&sendr3="+playerData.eid+"&sendr4="+playerData.endTime()+
                            "&sendr5="+playerData.userLog[0]+"&sendr6=N"+"&sendr7=N"+"&sendr8=2";
                    array.add(url);
                }else if(playerAction.playerTotalMoves()==2){
                    url="http://203.68.252.72:80/24game/userlogroutes.php?"+"sendr1="+playerData.imei+"&sendr2="+
                            playerData.startTime()+"&sendr3="+playerData.eid+"&sendr4="+playerData.endTime()+
                            "&sendr5="+playerData.userLog[0]+"&sendr6="+playerData.userLog[1]+"&sendr7=N"+"&sendr8=2";
                    array.add(url);
                }else{
                    url = "http://203.68.252.72:80/24game/userlogroutes.php?" + "sendr1=" + playerData.imei + "&sendr2=" +
                            playerData.startTime() + "&sendr3=" + playerData.eid + "&sendr4=" + playerData.endTime() +
                            "&sendr5=" + playerData.userLog[0] + "&sendr6=" + playerData.userLog[1] + "&sendr7=" +
                            playerData.userLog[2];
                    userlogFinal=url;
                }
                Log.d("MainActivity", url);
                url=null;
        }
    }

    void goNextQuestion(BtnData btnData){//是否過關or答錯，要有生命才可接關
        if(life.isLive()) {
            getHintTimes=0;
            String tmp=btnData.setBtnValue();
            if (tmp.equals("24") && playerAction.playerTotalMoves() == playerAction.playerLimitMoves()) {
                //myScore.setFinishSeconds(recordTimeSeconds);//紀錄結束時間
                yOrN = true;
                playerData.isRight(yOrN);
                userlogFinal +="&sendr8="+playerData.tOrF();
                array.add(userlogFinal);
                problem++;
                back();
                //loadPostLogin2(userlogFinal);
                //getNumberData();
                /*if (recordTimeSeconds + 20 < 300) totalTime = recordTimeSeconds + 20;
                else totalTime = 300;
                countDownTimer.cancel();*/
                myScore.win();
                allReset();
            } else if (playerAction.playerTotalMoves() == playerAction.playerLimitMoves()) {
                //myScore.setFinishSeconds(recordTimeSeconds);//紀錄結束時間
                //loadPostLogin2(userlogFinal);
                //getNumberData();
                myScore.win=false;
                yOrN = false;
                life.used();
                skip.setText(""+life.totalLife);
                playerData.isRight(yOrN);
                if(fromSkip==false) array.add(userlogFinal);
                problem++;
                back();
                allReset();
            }

        }
        if(life.totalLife==0){
            countDownTimer.cancel();
            skip.setText("遊戲結束");
            stime.setText("Game Over");
            goRankView();
        }

        userlogFinal=null;
    }

    void back(){
        if(array.size()>0 && ((problem%10)==0) || (life.totalLife==0)){
            Iterator i = array.iterator();
            while(i.hasNext()){
                String s=(String)i.next();
                loadPostLogin(s);
            }
            i.remove();
            for(int q=0; q<array.size(); q++) array.remove(q);
        }
    }
    void playerScore(){
        /*Toast.makeText(getApplicationContext(), "時間 : "+myScore.lastSeconds+", "+myScore.finishSeconds+", "+
                (myScore.lastSeconds - myScore.finishSeconds), Toast.LENGTH_SHORT).show();*/
        //if((myScore.lastSeconds-myScore.finishSeconds)<0) Toast.makeText(getApplicationContext(), "時間錯誤", Toast.LENGTH_SHORT).show();
        score.setText("分數: "+myScore.totalScore());
        if(myScore.combo>1){
            comImg.setVisibility(View.VISIBLE);
        }else{
            comImg.setVisibility(View.INVISIBLE);
        }
    }

    void pressed(){//運算過後第一個按鈕要做的事
        if(playerAction.pressBtnIDStep1()==0){A.setText("");}
        else if(playerAction.pressBtnIDStep1()==1){B.setText("");}
        else if(playerAction.pressBtnIDStep1()==2){C.setText("");}
        else if(playerAction.pressBtnIDStep1()==3){D.setText("");}
    }

    void allReset(){//全部重來(接下題用)
        int tmp;
        boolean b=false;
        playerData.setLastSeconds();
        allBtnCanPress();//設定所有按鈕皆可按
        tmp=recordTimeSeconds;
        //Log.d("MainActivity", "===FFFFFF111FFFFFF=="+tmp);
        myScore.setFinishSeconds(tmp);//紀錄結束時間
        playerScore();
        if(yOrN && firstRound==false) {
            if (recordTimeSeconds + 20 < 300) totalTime = recordTimeSeconds + 20;
            else totalTime = 300;
            countDownTimer.cancel();
            b=true;
        }
        if(yOrN && connect) countDownTimer();
        //Log.d("MainActivity", "===FFFFFF222FFFFFF=="+tmp);
        getNumberData();
        playerAction.allReset();
        //playerScore();
        if(b)tmp=totalTime;
        else tmp=recordTimeSeconds;
        //Log.d("MainActivity", "===LLLLLLLLLLLLL=="+tmp);
        myScore.setLastSeconds(tmp);//紀錄起始時間
        if(firstRound) myScore.setLastSeconds(180);
        firstRound=false;
        yOrN=true;
        hint.setText("提示");
        fromSkip = false;
        playerData.reset();
    }

    void reset(){//重新此局
        allBtnCanPress();
        setBtnNumber();
        playerAction.resetValue();
        hint.setText("提示");
        playerData.reset();
    }

    void btnValueIs0(Button btn, BtnData btnData){//按鈕上不顯示0
        if(Integer.parseInt(btnData.setBtnValue())==0){
            btn.setText("");
            btn.setBackgroundColor(btnData.clicked());
        }
    }

    String judgementOperationToURL(){
        if(playerAction.iWasPressedOperation.equals("+")) return "%2b";//代替%2b
        else if(playerAction.iWasPressedOperation.equals("/")) return "%2f";
        else if(playerAction.iWasPressedOperation.equals("-")) return "-";
        else if (playerAction.iWasPressedOperation.equals("*")) return "x";
        return "";
    }
    String judgementOperationToURL2(){
        if(playerAction.iWasPressedOperation.equals("+")) return "p";//代替%2b
        else if(playerAction.iWasPressedOperation.equals("/")) return "/";
        else if(playerAction.iWasPressedOperation.equals("-")) return "-";
        else if (playerAction.iWasPressedOperation.equals("*")) return "x";
        return "";
    }

    void goRankView(){//切換至Rank畫面
        for(int i=0; i<10; i++){
            if (Integer.parseInt(rkScore[i])<myScore.totalScore && myScore.totalScore!=0) {
                insertUserName();
                break;
            }else if(i==9) intent();
        }
    }

    void intent(){
        Intent intent = new Intent();
        intent.putExtra("PlayerName", playerName);
        intent.putExtra("SCORE", myScore.totalScore);
        intent.putExtra("Name", rkName);
        intent.putExtra("Score", rkScore);
        intent.putExtra("Date", rkDate);
        intent.setClass(MainActivity.this, RankingViewMain.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    void insertUserName() {//進入排行榜時讓玩家輸入姓名
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View v = inflater.inflate(R.layout.insert_user_name, null);
        //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("恭喜進入排行榜，請輸入您的名字")
                .setView(v)
                .setCancelable(false)//設定無法點擊Dialog以外的地方
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.editText1));
                        if (editText.getText().toString().trim().length()==0 || editText.getText().toString().length()>10) {
                            Toast.makeText(getApplicationContext(), "輸入錯誤，請重新輸入", Toast.LENGTH_SHORT).show();
                            insertUserName();
                        }
                        playerName = editText.getText().toString();
                        if(editText.getText().toString().trim().length()>0 && editText.getText().toString().length()<=10) intent();
                    }
                })
                .show();
    }

    String []rkName = new String[10];
    String []rkScore = new String[10];
    String []rkDate = new String[10];
    void  rankingData() {//public synchronized
        for (int i = 0; i < 10; i++) rkName[i] = rkScore[i] = rkDate[i] = "-1";
        Context mContext;
        mContext = this;
        RequestQueue mqueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, "http://203.68.252.72:80/24game/rankshow.php", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int n = 0;
                            do {
                                JSONObject crabsname = (JSONObject) response.get(n);
                                String s = crabsname.getString("name");
                                rkName[n] = s;
                                s = crabsname.getString("score");
                                rkScore[n] = s;
                                s = crabsname.getString("date");
                                rkDate[n] = s;
                                s = null;
                                n++;
                            } while (n < 10);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show(); //如果Respon呼叫失敗
                rankingData();
            }
        });
        mqueue.add(crabjson);
    }



    void  toHintV1(String toDB){//public synchronized
        Context mContext;
        mContext = this;
        RequestQueue mqueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, toDB, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject crabsname = (JSONObject) response.get(0);
                    String s = crabsname.getString("Formula");
                    if("no anses".equals(s)||"NO ANSES".equals(s)) toHintV1("http://203.68.252.72:80/24game/ans.php?INDEX=9&EID=" + eid);
                    else hint.setText(""+s);
                    s=null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show(); //如果Respon呼叫失敗
            }
        });
        mqueue.add(crabjson);
    }

    void toHintV2(String toDB){//public synchronized
        Context mContext;
        mContext = this;
        RequestQueue mqueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, toDB, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject crabsname = (JSONObject) response.get(0);
                    String s = crabsname.getString("formula");
                    if("no anses".equals(s)||"NO ANSES".equals(s)||"NOANS".equals(s)) toHintV2("http://203.68.252.72:80/24game/subans.php?EID="+eid);
                    else {
                        hint.setText(""+s);
                    }
                    s=null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Err: "+e.getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show(); //如果Respon呼叫失敗
            }
        });
        mqueue.add(crabjson);
        mContext=null;
    }

    String []send=new String[4];

    void headURL(){
        send[0]="sendr1="+playerData.imei;
        send[1]="&sendr2="+playerData.startTime();
        send[2]="&sendr3="+playerData.eid;
        send[3]="&sendr4="+version;
        String url="http://203.68.252.72:80/24game/userlog.php?"+send[0]+send[2]+send[1]+send[3];
        Log.d("HEAD:", url);
        array.add(url);
    }

    RequestQueue mQueue;
    public void loadPostLogin(String url)
    {
        //Toast.makeText(getApplicationContext(),"ST: "+playerData.startTime()+"EID: "+playerData.eid,Toast.LENGTH_LONG).show();
        if(mQueue == null) {
            mQueue = Volley.newRequestQueue(getApplicationContext());
        }
            //Log.d("MainActivity", url);

        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>() {
             @Override
             public void onResponse(JSONArray response) {
                //Toast.makeText(getApplicationContext(),"GJ",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
             public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(),"Error:"+error.getMessage(),Toast.LENGTH_LONG).show(); //如果Respon呼叫失敗
             }
        });
        mQueue.add(crabjson);
        url = null;
        //mQueue=null;
        // crabjson = null;

    }

    //===================================設定按鈕上的值================================================
    void setDataField(){//記住資料庫中ABCD欄位的資料
        int zero=0;//計算資料中有幾個0
        for(int i=0; i<4; i++) if(btnValue[i]==0) zero++;
        playerAction.setLimitMoves(zero);
        fieldA.initialFieldProcessing("A",btnValue[0],randomPosition.getNumber());//欄位名稱、值、位置
        fieldB.initialFieldProcessing("B",btnValue[1],randomPosition.getNumber());
        fieldC.initialFieldProcessing("C",btnValue[2],randomPosition.getNumber());
        fieldD.initialFieldProcessing("D",btnValue[3],randomPosition.getNumber());
    }

    void setBtnNumber(){//設定按鈕的數字，剛從資料庫用出來的
        if(fieldA.position()==0){
            A.setText(""+fieldA.btnValue());
            btnData0.setFieldInBtnData(fieldA.fieldID,fieldA.fieldValue);//在Btn中設定按鈕從資料庫抓下來的欄位名稱與值
        }else if(fieldA.position()==1){
            B.setText(""+fieldA.btnValue());
            btnData1.setFieldInBtnData(fieldA.fieldID,fieldA.fieldValue);
        }else if(fieldA.position()==2){
            C.setText(""+fieldA.btnValue());
            btnData2.setFieldInBtnData(fieldA.fieldID,fieldA.fieldValue);
        }else if(fieldA.position()==3){
            D.setText(""+fieldA.btnValue());
            btnData3.setFieldInBtnData(fieldA.fieldID,fieldA.fieldValue);
        }

        if(fieldB.position()==0){
            A.setText(""+fieldB.btnValue());
            btnData0.setFieldInBtnData(fieldB.fieldID,fieldB.fieldValue);
        }else if(fieldB.position()==1){
            B.setText(""+fieldB.btnValue());
            btnData1.setFieldInBtnData(fieldB.fieldID,fieldB.fieldValue);
        }else if(fieldB.position()==2){
            C.setText(""+fieldB.btnValue());
            btnData2.setFieldInBtnData(fieldB.fieldID,fieldB.fieldValue);
        }else if(fieldB.position()==3){
            D.setText(""+fieldB.btnValue());
            btnData3.setFieldInBtnData(fieldB.fieldID,fieldB.fieldValue);
        }

        if(fieldC.position()==0){
            A.setText(""+fieldC.btnValue());
            btnData0.setFieldInBtnData(fieldC.fieldID,fieldC.fieldValue);
        }else if(fieldC.position()==1){
            B.setText(""+fieldC.btnValue());
            btnData1.setFieldInBtnData(fieldC.fieldID,fieldC.fieldValue);
        }else if(fieldC.position()==2){
            C.setText(""+fieldC.btnValue());
            btnData2.setFieldInBtnData(fieldC.fieldID,fieldC.fieldValue);
        }else if(fieldC.position()==3){
            D.setText(""+fieldC.btnValue());
            btnData3.setFieldInBtnData(fieldC.fieldID,fieldC.fieldValue);
        }

        if(fieldD.position()==0){
            A.setText(""+fieldD.btnValue());
            btnData0.setFieldInBtnData(fieldD.fieldID,fieldD.fieldValue);
        }else if(fieldD.position()==1){
            B.setText(""+fieldD.btnValue());
            btnData1.setFieldInBtnData(fieldD.fieldID,fieldD.fieldValue);
        }else if(fieldD.position()==2){
            C.setText(""+fieldD.btnValue());
            btnData2.setFieldInBtnData(fieldD.fieldID,fieldD.fieldValue);
        }else if(fieldD.position()==3){
            D.setText(""+fieldD.btnValue());
            btnData3.setFieldInBtnData(fieldD.fieldID,fieldD.fieldValue);
        }
        btnValueIs0(A, btnData0);
        btnValueIs0(B, btnData1);
        btnValueIs0(C, btnData2);
        btnValueIs0(D, btnData3);
    }

    void countDownTimer(){//倒數
        countDownTimer=new CountDownTimer(totalTime*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                recordTimeSeconds = (int)(millisUntilFinished / 1000);
                if ((millisUntilFinished / 1000) <= 59)
                    stime.setText("時間: " + recordTimeSeconds % 60 + "秒");
                else
                    stime.setText("時間" + recordTimeSeconds / 60 + "分" + recordTimeSeconds % 60 + "秒");
            }

            @Override
            public void onFinish() {
                playerAction.totalMoves=playerAction.playerLimitMoves();
                goNextQuestion(btnData0);
                life.totalLife=0;
                if(array.size()>0) back();
                if(life.isLive()==false) {
                    stime.setText("Game Over");
                    goRankView();
                }
            }
        };
        countDownTimer.start();
    }

    //===================================連資料庫=====================================
    /*void getNumberData(){
        eid="000";
        for(int i=0; i<4; i++) btnValue[i]=6;
        setDataField();//記住四格欄位的資料
        setBtnNumber();//設定按鈕的數字
        btnValue[0]=btnValue[1]=btnValue[2]=btnValue[3]=0;
        lM[0]=null;
        lM[1]=null;
        randomPosition.reset();
        playerData.setEID(eid);
        headURL();
        recordLM1=-2;
        connect=true;
    }*/

    void getNumberData(){
        while (true){
            int count=0;
            int tmp = (int) (Math.random() * 1361 + 1);
            eid = String.valueOf(topic.t[tmp][0]);
            btnValue[0] = topic.t[tmp][1];
            btnValue[1] = topic.t[tmp][2];
            btnValue[2] = topic.t[tmp][3];
            btnValue[3] = topic.t[tmp][4];
            for(int i=0; i<4; i++){
                if(btnValue[i]>9) count++;
            }
            if(count==0) break;
        }
        setDataField();//記住四格欄位的資料
        setBtnNumber();//設定按鈕的數字
        btnValue[0]=btnValue[1]=btnValue[2]=btnValue[3]=0;
        lM[0]=null;
        lM[1]=null;
        randomPosition.reset();
        playerData.setEID(eid);
        headURL();
        recordLM1=-2;
        connect=true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // Show home screen when pressing “back” button,
            //  so that this app won’t be closed accidentally
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ChooseVersionMain.class);
            startActivity(intent);
            countDownTimer.cancel();
            MainActivity.this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    /*public void getNumberData(){//抓資料庫題目資料
        //Toast.makeText(this, String.valueOf(eid), Toast.LENGTH_SHORT).show();
        Context mContext;
        mContext=this;
        RequestQueue mqueue = Volley.newRequestQueue(mContext);
        url = "http://120.127.16.69:80/24game/questions.php";
        Log.d("MainActivity", "================1================");
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject crabsname = (JSONObject) response.get(0);
                    String tmp;
                    tmp=crabsname.getString("EID").toString();
                    eid=tmp;
                    tmp= crabsname.getString("A").toString();
                    btnValue[0]= Integer.parseInt(tmp);
                    tmp= crabsname.getString("B").toString();
                    btnValue[1]= Integer.parseInt(tmp);
                    tmp= crabsname.getString("C").toString();
                    btnValue[2]= Integer.parseInt(tmp);
                    tmp= crabsname.getString("D").toString();
                    btnValue[3]= Integer.parseInt(tmp);
                    setDataField();//記住四格欄位的資料
                    setBtnNumber();//設定按鈕的數字
                    Log.d("MainActivity", "\n================2================\n");
                    btnValue[0]=btnValue[1]=btnValue[2]=btnValue[3]=0;
                    lM[0]=null;
                    lM[1]=null;
                    randomPosition.reset();
                    playerData.setEID(eid);
                    headURL();
                    //loadPostLogin();
                    recordLM1=-2;
                    connect=true;
                    Log.d("MainActivity", "\n================3================\n");
                } catch (JSONException e) {
                    connect=false;
                    allReset();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity", "\n================4================\n");
                connect=false;
                allReset();
                Log.d("MainActivity", "\n================5================\n");
            }
        });
        mqueue.add(crabjson);
        mqueue=null;
        mContext=null;
        crabjson=null;
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tw.com.game/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://tw.com.game/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


