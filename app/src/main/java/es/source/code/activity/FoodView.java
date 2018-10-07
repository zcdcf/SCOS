package es.source.code.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.adapter.FoodRecyclerViewAdapter;
import es.source.code.fragment.MealFragment;
import es.source.code.model.Food;

public class FoodView extends AppCompatActivity {

    @BindView(R.id.vpFoodInfo)
    ViewPager vpFoodInfo;
    @BindView(R.id.tlFoodType)
    TabLayout tlFoodType;

    private String[] tabName = {"冷菜","热菜","海鲜","酒水"};
    private ArrayList<String> tabIndicators;
    private ArrayList<MealFragment> fragmentLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);
        ButterKnife.bind(this);

        initContent();

        FragmentStatePagerAdapter myAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()){
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
    }


    private void initContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.addAll(Arrays.asList(tabName));

        initData();
        fragmentLists= new ArrayList<>();
        for(int i=0; i<tabName.length;i++) {
            MealFragment mealFragment = new MealFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("foodsData",foodLists.get(i));
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

    ArrayList<ArrayList<Food>> foodLists = new ArrayList<>();
    String[][] MealNames = {{"凉拌木耳","鸭舌"},{"红烧牛肉","童子鸡"},{"肉蟹煲","啤酒皮皮虾"},{"黄酒","啤酒"}};
    double[][] MealPrice = {{12, 16},{36,40},{40,60},{20,10}};

    private void initData() {
        for(int i=0; i<tabName.length;i++) {
            ArrayList<Food> foodsInfo = new ArrayList<>();
            for (int j = 0; j < MealNames[i].length; j++) {
                Food foodInfo = new Food(MealNames[i][j],MealPrice[i][j]);
                foodsInfo.add(foodInfo);
            }
            foodLists.add(foodsInfo);
        }
    }
}
