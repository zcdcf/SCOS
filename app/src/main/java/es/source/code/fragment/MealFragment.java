package es.source.code.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.adapter.FoodRecyclerViewAdapter;
import es.source.code.model.FoodStockInfo;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;

public class MealFragment extends Fragment {

    @BindView(R.id.rvColdMeal)
    RecyclerView rvColdMeal;
    Unbinder unbinder;

    private ArrayList<Food> foods;
    private String pageTitle;
    private MenuData menuData;

    private FoodRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.fragment_meal, container, false);
        unbinder = ButterKnife.bind(this, view);

        menuData = (MenuData) getContext().getApplicationContext();
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvColdMeal.setHasFixedSize(true);
        rvColdMeal.setLayoutManager(llm);

        Bundle bundle = getArguments();
        if (bundle == null) throw new AssertionError();
        foods = bundle.getParcelableArrayList("foodsData");
        pageTitle = bundle.getString("pageTitle");

        adapter = new FoodRecyclerViewAdapter(getContext(), foods, pageTitle);
        rvColdMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void refreshStock(ArrayList<FoodStockInfo> foodStockInfoArrayList) {
        for(int i=0; i<foodStockInfoArrayList.size(); i++) {
            foods.get(i).setStock(foodStockInfoArrayList.get(i).getStock());
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMessageEvent(GlobalConst.NewFoodMessageEvent event) {
        Log.i("MealFragment state:","receive food update info");
        if(event.isHasNewFood()) {
            refreshFoodsList();
        }
    }

    public void refreshFoodsList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
