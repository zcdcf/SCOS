package es.source.code.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.source.code.activity.R;
import es.source.code.adapter.OrderedFoodRecyclerViewAdapter;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;
import es.source.code.model.User;


public class OrderedMealFragment extends Fragment {

    @BindView(R.id.rvOrderedMeal)
    RecyclerView rvOrderedMeal;
    Unbinder unbinder;
    @BindView(R.id.tvMealTotalCount)
    TextView tvMealTotalCount;
    @BindView(R.id.tvMealTotalPrice)
    TextView tvMealTotalPrice;
    @BindView(R.id.bPay)
    Button bPay;

    private ArrayList<Food> foods;
    private ArrayList<Food> newFoods;
    private String pageTitle;
    private User user;
    private MenuData menuData;
    private OrderedFoodRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.fragment_ordered_meal, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvOrderedMeal.setHasFixedSize(true);
        rvOrderedMeal.setLayoutManager(llm);

        menuData = (MenuData) getContext().getApplicationContext();

        Bundle bundle = getArguments();
        if (bundle == null) throw new AssertionError();
        foods = bundle.getParcelableArrayList("orderedFoodsData");
        pageTitle = bundle.getString("pageTitle");
        user = (User) bundle.getSerializable("userInfo");

        if (foods == null) {
            tvMealTotalCount.setText("菜品总数:" + 0);
        } else {
            tvMealTotalCount.setText("菜品总数:" + String.valueOf(foods.size()));
        }

        tvMealTotalPrice.setText("订单总价" + calTotalPrice(foods));
        adapter = new OrderedFoodRecyclerViewAdapter(getContext(), foods, pageTitle);
        rvOrderedMeal.setAdapter(adapter);
        return view;
    }

//测试能否刷新fragment的数据
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private double calTotalPrice(ArrayList<Food> foods) {
        double totalPrice = 0;
        if (foods == null) {
            totalPrice = 0;
        } else {
            for (int i = 0; i < foods.size(); i++) {
                totalPrice += foods.get(i).getPrice();
            }
        }
        return totalPrice;
    }

    @OnClick(R.id.bPay)
    public void onViewClicked() {
        if(user!=null) {
            if(user.isOldUser()==GlobalConst.IS_OLD_USER) {
                Toast toast = Toast.makeText(getContext(),"您好，老顾客，本次你可享受7 折优惠",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private ArrayList<Food> refreshData() {
        ArrayList<Food> newLists = new ArrayList<>();
        ArrayList<ArrayList<Food>> allFoodlists = menuData.getFoodLists();
        for(int i=0; i<allFoodlists.size(); i++) {
            for(int j=0; j<allFoodlists.get(i).size(); j++) {
                if(allFoodlists.get(i).get(j).getOrdered()==GlobalConst.ORDERED) {
                    newLists.add(allFoodlists.get(i).get(j));
                }
            }
        }
        return newLists;
    }
}
