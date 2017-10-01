package tw.com.game;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ChooseVersionMain extends Activity implements View.OnClickListener{
    Button v1, v2, v3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_version_main);
        v1 = (Button)findViewById(R.id.v1);
        v2 = (Button)findViewById(R.id.v2);
        v3 = (Button)findViewById(R.id.v3);
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//3代表沒有提示 2代表提示一點點 1代表提示全部
            case R.id.v1:
                version(3);
                break;
            case R.id.v2:
                version(1);
                break;
            default:
                version(2);
        }
    }
    void version(int i){
        Intent intent = new Intent();
        intent.putExtra("VERSION", i);
        intent.setClass(ChooseVersionMain.this, MainActivity.class);
        startActivity(intent);
        //ChooseVersionMain.this.finish();
    }
}
