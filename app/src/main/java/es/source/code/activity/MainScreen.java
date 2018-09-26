package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainScreen.this,SCOSEntry.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
        this.finish();
    }
}
