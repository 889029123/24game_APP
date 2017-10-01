package tw.com.game;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class RankingViewMain extends Activity implements View.OnClickListener{
    Button btn;
    TextView[]txvName = new TextView[10];
    TextView []txvScore = new TextView[10];
    TextView []txvDate = new TextView[10];
    String []rkName = new String[10];
    String []rkScore = new String[10];
    String []rkDate = new String[10];
    int score, rkNum=111;
    String playerName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ranking_main);
        findView();
        playerName = getIntent().getStringExtra("PlayerName");
        score = getIntent().getIntExtra("SCORE", 0);
        rkName = getIntent().getStringArrayExtra("Name");
        rkScore = getIntent().getStringArrayExtra("Score");
        rkDate = getIntent().getStringArrayExtra("Date");

        userSetRK();
    }


    void findView(){
        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);
        txvName[0] = (TextView)findViewById(R.id.R_name_1);
        txvName[1] = (TextView)findViewById(R.id.R_name_2);
        txvName[2] = (TextView)findViewById(R.id.R_name_3);
        txvName[3] = (TextView)findViewById(R.id.R_name_4);
        txvName[4] = (TextView)findViewById(R.id.R_name_5);
        txvName[5] = (TextView)findViewById(R.id.R_name_6);
        txvName[6] = (TextView)findViewById(R.id.R_name_7);
        txvName[7] = (TextView)findViewById(R.id.R_name_8);
        txvName[8] = (TextView)findViewById(R.id.R_name_9);
        txvName[9] = (TextView)findViewById(R.id.R_name_10);
        txvScore[0] = (TextView)findViewById(R.id.R_score_1);
        txvScore[1] = (TextView)findViewById(R.id.R_score_2);
        txvScore[2] = (TextView)findViewById(R.id.R_score_3);
        txvScore[3] = (TextView)findViewById(R.id.R_score_4);
        txvScore[4] = (TextView)findViewById(R.id.R_score_5);
        txvScore[5] = (TextView)findViewById(R.id.R_score_6);
        txvScore[6] = (TextView)findViewById(R.id.R_score_7);
        txvScore[7] = (TextView)findViewById(R.id.R_score_8);
        txvScore[8] = (TextView)findViewById(R.id.R_score_9);
        txvScore[9] = (TextView)findViewById(R.id.R_score_10);
        txvDate[0] = (TextView)findViewById(R.id.R_time_1);
        txvDate[1] = (TextView)findViewById(R.id.R_time_2);
        txvDate[2] = (TextView)findViewById(R.id.R_time_3);
        txvDate[3] = (TextView)findViewById(R.id.R_time_4);
        txvDate[4] = (TextView)findViewById(R.id.R_time_5);
        txvDate[5] = (TextView)findViewById(R.id.R_time_6);
        txvDate[6] = (TextView)findViewById(R.id.R_time_7);
        txvDate[7] = (TextView)findViewById(R.id.R_time_8);
        txvDate[8] = (TextView)findViewById(R.id.R_time_9);
        txvDate[9] = (TextView)findViewById(R.id.R_time_10);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(RankingViewMain.this, ChooseVersionMain.class);
        startActivity(intent);
        RankingViewMain.this.finish();
    }



    public void setField(){
        int n=0;
        boolean first = true;
        for(int i=0; i<10; i++){
            n=i;
            if(first == false) n=i-1;
            if(Integer.parseInt(rkScore[i])<score && first && score!=0){
                Date d = new Date();
                CharSequence s1  = DateFormat.format("yyyy-MM-dd%2bkk:mm:ss", d.getTime());
                String t= (String) s1;
                d=null;
                n=i - 1;
                txvName[i].setText(playerName);
                txvScore[i].setText(""+score);
                txvDate[i].setText(""+t);
                first = false;
            }else {
                txvName[i].setText(rkName[n]);
                txvScore[i].setText(rkScore[n]);
                txvDate[i].setText(rkDate[n]);
            }
        }
    }

    void userSetRK(){
        for(int i=0; i<10; i++) {
            if(score==0) break;
            if (score > Integer.parseInt(rkScore[i])){
                if(playerName.equals("")==false) insertScoreToDB();
                break;
            }
        }
        setField();
    }



    RequestQueue rkQ;
    void insertScoreToDB(){
        if(rkQ == null) {
            rkQ = Volley.newRequestQueue(getApplicationContext());
        }
        //Log.d("MainActivity", url);

        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.POST, "http://120.127.16.69/24game/rankin.php?name="+
                playerName +"&score=" + score, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getApplicationContext(),"GJ",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"Error:"+error.getMessage(),Toast.LENGTH_LONG).show(); //如果Respon呼叫失敗
            }
        });
        rkQ.add(crabjson);

    }


}