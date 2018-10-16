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
import es.source.code.adapter.UnOrderedFoodRecyclerViewAdapter;
import es.source.code.model.Food;
import es.source.code.model.MenuData;

public class UnOrderedMealFragment extends Fragment {
    @BindView(R.id.rvUnOrderedMeal)
    RecyclerView rvUnOrderedMeal;
    Unbinder unbinder;
    @BindView(R.id.tvMealTotalCountUn)
    TextView tvMealTotalCountUn;
    @BindView(R.id.tvMealTotalPriceUn)
    TextView tvMealTotalPriceUn;
    @BindView(R.id.bSubmit)
    Button bSubmit;

    private ArrayList<Food> foods;
    private String pageTitle;
    private MenuData menuData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.fragment_un_ordered_meal, container, false);
        unbinder = ButterKnife.bind(this, view);

        menuData = (MenuData) getContext().getApplicationContext();
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvUnOrderedMeal.setHasFixedSize(true);
        rvUnOrderedMeal.setLayoutManager(llm);

        Bundle bundle = getArguments();
        if (bundle == null) throw new AssertionError();
        foods = bundle.getParcelableArrayList("unOrderedFoodsData");
        pageTitle = bundle.getString("pageTitle");

        if (foods == null) {
            tvMealTotalCountUn.setText("菜品总数:" + 0);
        } else {
            tvMealTotalCountUn.setText("菜品总数:" + String.valueOf(foods.size()));
        }

        tvMealTotalPriceUn.setText("订单总价" + calTotalPrice(foods));
        UnOrderedFoodRecyclerViewAdapter adapter = new UnOrderedFoodRecyclerViewAdapter(getContext(), foods, pageTitle, tvMealTotalPriceUn, tvMealTotalCountUn);
        rvUnOrderedMeal.setAdapter(adapter);
        return view;
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

    @OnClick(R.id.bSubmit)
    public void onViewClicked() {
        for(int i=0; i<foods.size(); i++) {
            menuData.setOrdered(foods.get(i).getType(),foods.get(i).getPosition());
        }
        Toast toast = Toast.makeText(getContext(),"订单提交成功",Toast.LENGTH_SHORT);
        toast.show();
    }
}
