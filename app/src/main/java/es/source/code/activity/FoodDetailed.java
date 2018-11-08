package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.fragment.FoodDetailedFragment;
import es.source.code.model.Food;
import es.source.code.model.MenuData;

public class FoodDetailed extends AppCompatActivity {

    @BindView(R.id.vpDetailedFoodInfo)
    ViewPager vpDetailedFoodInfo;

    private String TAG = "FoodDetailed state:";
    private ArrayList<FoodDetailedFragment> fragmentsLists = new ArrayList<>();
    private MenuData menuData;
    private ArrayList<ArrayList<Food>> foodLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        setContentView(R.layout.activity_food_detailed);
        ButterKnife.bind(this);

        menuData = (MenuData) getApplication();
        initContent();

        FragmentStatePagerAdapter myAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            private String[] tabNames = {"冷菜", "热菜", "海鲜", "酒水"};

            @Override
            public Fragment getItem(int position) {
                return fragmentsLists.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsLists.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabNames[position];
            }
        };

        vpDetailedFoodInfo.setAdapter(myAdapter);

        int page = -1;
        Intent intent = getIntent();
        if(intent!=null) {
            page = intent.getIntExtra("page",0);
            Log.i(TAG,"get page is "+page);
        }
        if(page!=-1) {
            vpDetailedFoodInfo.setCurrentItem(page);
        }
    }

    private void initContent() {
        foodLists = menuData.getFoodLists();
        for (int i = 0; i < foodLists.size(); i++) {
            Log.i("FoodDetailed foodlist size = ", String.valueOf(foodLists.get(i).size()));
            for (int j = 0; j < foodLists.get(i).size(); j++) {
                FoodDetailedFragment foodDetailedFragment = new FoodDetailedFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("foodData", foodLists.get(i).get(j));
                foodDetailedFragment.setArguments(bundle);
                fragmentsLists.add(foodDetailedFragment);
            }
        }
    }

    public void addContent() {

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
