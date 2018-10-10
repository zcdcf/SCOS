package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SCOSEntry extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        ButterKnife.bind(this);
    }

    // get the start coordination
    // https://blog.csdn.net/izheer/article/details/52036445
    private double startX = 0;
    private double finishX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:startX = event.getX();
                                         Log.i("startX:",startX+" ");
                                         break;
            case MotionEvent.ACTION_UP:finishX = event.getX();
                                       Log.i("finishX:",finishX+" ");
                                       if( (startX-finishX)>10 ) {
                                           String fromEntry = "fromEntry";
                                           Log.i("SCOSEntry:","to MainScreen");
                                           Intent intent = new Intent(SCOSEntry.this,MainScreen.class);
                                           intent.putExtra(fromEntry, GlobalConst.INFO_ENTRY_TO_MAIN_SCREEN);
                                           intent.putExtra("intentFrom",SCOSEntry.class.getName());
                                           startActivity(intent);
                                               //https://www.jianshu.com/p/5ee0bbf8a9fd
                                               //add transition animation
                                           overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                                       }
                                       break;
        }

        return false;
    }

}
