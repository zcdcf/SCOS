package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreen extends AppCompatActivity {

    @BindView(R.id.gvMainScreenNav)
    GridView gvMainScreenNav;


    private int[] navItemIcon = {R.drawable.login_png, R.drawable.help_png, R.drawable.checkbox_png, R.drawable.formatlist_png };
    private String[] navItemName = {"登录/注册", "帮助", "点菜", "查看订单"};

    private List<Map<String, Object>> navLists = new ArrayList<Map<String, Object>>();

    private void initData(List<Map<String,Object>> navLists) {
        for (int j = 0; j < navItemIcon.length; j++) {
            Map<String, Object> navItem = new HashMap<String, Object>();
            navItem.put("icon", navItemIcon[j]);
            navItem.put("name", navItemName[j]);
            navLists.add(navItem);
        }
    }

    private String[] from = {"icon", "name"};
    private int[] to = {R.id.ibGridViewItemImage, R.id.bGridViewItemButton};
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        Log.i("current state", "onCreate");

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("intentFrom");

        initData(navLists);

        if (activityName != null && activityName.equals(SCOSEntry.class.getName())) {
            String strFromEntry = intent.getStringExtra("fromEntry");

            //https://blog.csdn.net/maxbyzhou/article/details/52157234
            if (!strFromEntry.equals(GlobalConst.INFO_ENTRY_TO_MAIN_SCREEN)) {
                simpleAdapter = new SimpleAdapter(this, navLists.subList(0, 2), R.layout.activity_main_screen_gridview_item, from, to);
                gvMainScreenNav.setAdapter(simpleAdapter);
            } else {
                simpleAdapter = new SimpleAdapter(this, navLists, R.layout.activity_main_screen_gridview_item, from, to);
                gvMainScreenNav.setAdapter(simpleAdapter);
            }
        } else {
            simpleAdapter = new SimpleAdapter(this, navLists.subList(0, 2), R.layout.activity_main_screen_gridview_item, from, to);
            gvMainScreenNav.setAdapter(simpleAdapter);
        }

        gvMainScreenNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:Intent intent = new Intent(MainScreen.this,LoginOrRegister.class);
                           startActivityForResult(intent, GlobalConst.MAINSCREEN_REQUEST_CODE);
                           overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                           break;
                }
            }
        });
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GlobalConst.MAINSCREEN_REQUEST_CODE && !loginSuccess) {
            if(resultCode==GlobalConst.LOGIN_RETURN_RESULT_CODE) {
                if(navLists.size()==4) {
                    navLists.remove(3);
                    navLists.remove(2);
                    simpleAdapter.notifyDataSetChanged();
                }

            } else if(resultCode==GlobalConst.LOGIN_SUCCESS_RESULT_CODE || loginSuccess) {
                    for(int i = 2;i<navItemIcon.length;i++){
                        Map<String, Object> navItem = new HashMap<String, Object>();
                        navItem.put("icon", navItemIcon[i]);
                        navItem.put("name", navItemName[i]);
                        navLists.add(navItem);
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        }


}
