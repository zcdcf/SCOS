package es.source.code.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.fragment.MealFragment;
import es.source.code.model.Food;
import es.source.code.model.FoodStockInfo;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;
import es.source.code.model.User;
import es.source.code.service.SeverObserverService;

public class FoodView extends AppCompatActivity {

    @BindView(R.id.vpFoodInfo)
    ViewPager vpFoodInfo;
    @BindView(R.id.tlFoodType)
    TabLayout tlFoodType;

    private String[] tabName = {"冷菜","热菜","海鲜","酒水"};
    private ArrayList<String> tabIndicators;
    private ArrayList<MealFragment> fragmentLists;
    private ArrayList<ArrayList<Food>> foodLists = new ArrayList<>();
    private MenuData menuData;
    private User user;
    private FragmentStatePagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userInfo");

        initContent();

        myAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()){
            private String[] tabNames = {"冷菜","热菜","海鲜","酒水"};
            @Override
            public Fragment getItem(int position) {
                return fragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return fragmentLists.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabNames[position];
            }
        };

        vpFoodInfo.setAdapter(myAdapter);

        tlFoodType.setupWithViewPager(vpFoodInfo);

        bindSeverObserverService();
    }

    private void bindSeverObserverService() {
        Log.i("FoodView state:","bind SeverObserverService");
        Intent intent2 = new Intent(this,SeverObserverService.class);
        bindService(intent2, connection, BIND_AUTO_CREATE);
    }

    Messenger serviceMessenger;
    Messenger activityMessenger;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            Log.i("FoodView State:","get Service messenger");
            activityMessenger = new Messenger(sMessageHandler);
            Message msg = new Message();
            msg.what = GlobalConst.BIND_MSG;
            msg.replyTo = activityMessenger;
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
    }


    private void initContent() {
        menuData = (MenuData) getApplication();
        tabIndicators = new ArrayList<>();
        tabIndicators.addAll(Arrays.asList(tabName));

        /*
        test addNewFood
        menuData.addNewFood(new Food("小鸡炖蘑菇",54,1));
        */

        foodLists = menuData.getFoodLists();
        fragmentLists= new ArrayList<>();
        for(int i=0; i<tabName.length;i++) {
            MealFragment mealFragment = new MealFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("foodsData",foodLists.get(i));
            bundle.putString("pageTitle",tabName[i]);
            mealFragment.setArguments(bundle);
            fragmentLists.add(mealFragment);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_view_main, menu);
        return true;
    }


    @SuppressLint("HandlerLeak")
    private Handler sMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("FoodView state:", "start to handle message");
            switch (msg.what) {
                case GlobalConst.STOCK_HAS_UPDATE: {
                    Log.i("FoodView state:", "get update info");
                    Bundle bundle = msg.getData();
                    ArrayList<ArrayList<FoodStockInfo>> foodStockList = new ArrayList<>();
                    for (int i = 0; i < bundle.size(); i++) {
                        foodStockList.add((ArrayList<FoodStockInfo>) bundle.get(String.valueOf(i)));
                    }

                    refreshStock(foodStockList);
                    for (int i = 0; i < foodStockList.size(); i++) {
                        for (int j = 0; j < foodStockList.get(i).size(); j++) {
                            FoodStockInfo foodStock = foodStockList.get(i).get(j);
                            Log.i("stockInfo:", foodStock.getFoodName() + " " + foodStock.getStock());
                        }
                    }
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSubmitted:
                Intent intent = new Intent(FoodView.this,FoodOrderView.class);
                intent.putExtra("page",GlobalConst.pageSubmittedMeals);
                intent.putExtra("userInfo",user);
                startActivity(intent);
                break;
            case R.id.menuItemOrderedMeal:
                Intent intent1 = new Intent(FoodView.this,FoodOrderView.class);
                intent1.putExtra("page",GlobalConst.pageOrderedMeals);
                intent1.putExtra("userInfo",user);
                startActivity(intent1);
                break;
            case R.id.menuItemHelp:break;
            case R.id.menuUpdate:
                if(item.getTitle().equals(getString(R.string.startUpdate))) {
                    Log.i("FoodView state:","start updating");
                    Message msgStart = new Message();
                    msgStart.what = GlobalConst.START_UPDATE_FOODINFO;
                    try {
                        serviceMessenger.send(msgStart);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    item.setTitle(R.string.stopUpdate);
                } else {
                    Log.i("FoodVIew state:","stop updating");
                    item.setTitle(R.string.startUpdate);
                    Message msgStop = new Message();
                    msgStop.what = GlobalConst.STOP_UPDATE_FOODINFO;
                    try {
                        serviceMessenger.send(msgStop);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void refreshStock(ArrayList<ArrayList<FoodStockInfo>> foodStockList) {
        for(int i=0; i<foodStockList.size(); i++) {
            for(int j=0; j<foodStockList.get(i).size(); j++) {
                menuData.setStock(i,j,foodStockList.get(i).get(j).getStock());
            }
            if(vpFoodInfo.getCurrentItem() == i) {
                fragmentLists.get(i).refreshStock(foodStockList.get(i));
                Log.i("Page", String.valueOf(i) + " data has refreshed");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
