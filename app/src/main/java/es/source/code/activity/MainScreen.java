package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreen extends AppCompatActivity {

    @BindView(R.id.bOrderMeal)
    Button bOrderMeal;

    @BindView(R.id.bCheckOrdering)
    Button bCheckOrdering;

    @BindView(R.id.bLoginOrSign)
    Button bLoginOrSign;

    @BindView(R.id.ibOrderMeal)
    ImageButton ibOrderMeal;

    @BindView(R.id.ibCheckOrdering)
    ImageButton ibCheckOrdering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        Log.i("current state","onCreate");

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("intentFrom");

        if(activityName!=null&& activityName.equals(SCOSEntry.class.getName())) {
            String strFromEntry = intent.getStringExtra("fromEntry");

                //https://blog.csdn.net/maxbyzhou/article/details/52157234
            if (!strFromEntry.equals(GlobalConst.INFO_ENTRY_TO_MAIN_SCREEN)) {
                bOrderMeal.setVisibility(View.INVISIBLE);
                bCheckOrdering.setVisibility(View.INVISIBLE);
                ibOrderMeal.setVisibility(View.INVISIBLE);
                ibCheckOrdering.setVisibility(View.INVISIBLE);
            }
        } else {
            bOrderMeal.setVisibility(View.INVISIBLE);
            bCheckOrdering.setVisibility(View.INVISIBLE);
            ibOrderMeal.setVisibility(View.INVISIBLE);
            ibCheckOrdering.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("current state:","onRestart");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainScreen.this, SCOSEntry.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        this.finish();
    }


    @OnClick({R.id.ibLoginOrSign, R.id.bLoginOrSign})
    public void onViewClicked(View view) {
        Intent intent = new Intent(MainScreen.this, LoginOrRegister.class);
        startActivityForResult(intent,GlobalConst.MAINSCREEN_REQUEST_CODE);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    private boolean loginSuccess = false;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GlobalConst.MAINSCREEN_REQUEST_CODE && !loginSuccess) {
            if(resultCode==GlobalConst.LOGIN_RETURN_RESULT_CODE) {
                bOrderMeal.setVisibility(View.INVISIBLE);
                bCheckOrdering.setVisibility((View.INVISIBLE));
                ibCheckOrdering.setVisibility(View.INVISIBLE);
                ibOrderMeal.setVisibility(View.INVISIBLE);
            } else if(resultCode==GlobalConst.LOGIN_SUCCESS_RESULT_CODE || loginSuccess) {
                bOrderMeal.setVisibility(View.VISIBLE);
                bCheckOrdering.setVisibility((View.VISIBLE));
                ibCheckOrdering.setVisibility(View.VISIBLE);
                ibOrderMeal.setVisibility(View.VISIBLE);
                loginSuccess = true;
            }
        }
    }

}
