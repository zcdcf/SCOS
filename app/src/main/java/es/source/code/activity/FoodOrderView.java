package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.GoalRow;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.fragment.OrderedMealFragment;
import es.source.code.fragment.UnOrderedMealFragment;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;
import es.source.code.model.User;

public class FoodOrderView extends AppCompatActivity {

    @BindView(R.id.vpOrderFoodInfo)
    ViewPager vpOrderFoodInfo;
    @BindView(R.id.tlIfOrder)
    TabLayout tlIfOrder;

    private String[] tabNames = {"未下单菜","已下单菜"};
    private ArrayList<String> tabIndicator = new ArrayList<>();
    private ArrayList<Food> orderedFoodLists = new ArrayList<>();
    private ArrayList<Food> unOrderedFoodLists = new ArrayList<>();
    private ArrayList<Fragment> fragmentsLists = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_view);
        ButterKnife.bind(this);

        int page = -1;
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("userInfo");

        initContent();
        FragmentStatePagerAdapter myAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()){
            private String[] tabNames = {"未下单菜","已下单菜"};
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

        vpOrderFoodInfo.setAdapter(myAdapter);

        tlIfOrder.setupWithViewPager(vpOrderFoodInfo);


        if(intent!=null) {
            page = intent.getIntExtra("page",0);
        }
        if(page!=-1) {
            vpOrderFoodInfo.setCurrentItem(page);
        }
    }

    private void initContent() {
        MenuData menuData = (MenuData) getApplication();

        tabIndicator = new ArrayList<>();
        tabIndicator.addAll(Arrays.asList(tabNames));

        ArrayList<ArrayList<Food>> foodLists = menuData.getFoodLists();
        for(int i=0; i<foodLists.size(); i++) {
            for(int j=0; j<foodLists.get(i).size(); j++) {
                if(foodLists.get(i).get(j).getOrdered()==GlobalConst.ORDERED) {
                    orderedFoodLists.add(foodLists.get(i).get(j));
                } else if(foodLists.get(i).get(j).getSubmited()==GlobalConst.SUBMITTED){
                    unOrderedFoodLists.add(foodLists.get(i).get(j));
                }
            }
        }

        UnOrderedMealFragment unOrderedMealFragment = new UnOrderedMealFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("unOrderedFoodsData",unOrderedFoodLists);
        bundle.putSerializable("userInfo",user);
        bundle.putString("pageTitle",tabNames[0]);
        unOrderedMealFragment.setArguments(bundle);
        fragmentsLists.add(unOrderedMealFragment);

        OrderedMealFragment orderedMealFragment = new OrderedMealFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("userInfo",user);
        bundle1.putParcelableArrayList("orderedFoodsData",orderedFoodLists);
        bundle1.putString("pageTitle",tabNames[1]);
        orderedMealFragment.setArguments(bundle1);
        fragmentsLists.add(orderedMealFragment);

    }

}
