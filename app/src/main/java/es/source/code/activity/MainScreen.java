package es.source.code.activity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static es.source.code.activity.R.layout.activity_main_screen_gridview_item;

public class MainScreen extends AppCompatActivity {

    @BindView(R.id.gvMainScreenNav)
    GridView gvMainScreenNav;


    private int[] navItemIcon = {R.drawable.checkbox_png, R.drawable.formatlist_png, R.drawable.help_png, R.drawable.login_png};
    private String[] navItemName = {"点菜", "查看订单", "登录/注册", "帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        Log.i("current state", "onCreate");

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("intentFrom");

        List<Map<String, Object>> navLists = new ArrayList<Map<String, Object>>();
        for (int j = 0; j < navItemIcon.length; j++) {
            Map<String, Object> navItem = new HashMap<String, Object>();
            navItem.put("icon", navItemIcon[j]);
            navItem.put("name", navItemName[j]);
            navLists.add(navItem);
        }

        String [] from = {"icon","name"};
        int [] to = {R.id.ibGridViewItemImage,R.id.bGridViewItemButton};

        if(activityName.equals(SCOSEntry.class.getName())) {
            String strFromEntry = intent.getStringExtra("fromEntry");

            //https://blog.csdn.net/maxbyzhou/article/details/52157234
            if (!strFromEntry.equals(GlobalConst.INFO_ENTRY_TO_MAIN_SCREEN)) {
                gvMainScreenNav.setAdapter(new SimpleAdapter(this,navLists.subList(2,4),R.layout.activity_main_screen_gridview_item,from,to));
            } else {
                gvMainScreenNav.setAdapter(new SimpleAdapter(this,navLists,R.layout.activity_main_screen_gridview_item,from,to));
            }
        }




    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("current state:", "onRestart");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainScreen.this, SCOSEntry.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        this.finish();
    }


    private boolean loginSuccess = false;

    public void

}
