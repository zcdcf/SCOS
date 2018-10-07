package es.source.code.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.adapter.FoodRecyclerViewAdapter;

public class MealFragment extends Fragment {

    @BindView(R.id.rvColdMeal)
    RecyclerView rvColdMeal;
    Unbinder unbinder;

    private ArrayList<Food> foods;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.fragment_meal, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvColdMeal.setHasFixedSize(true);
        rvColdMeal.setLayoutManager(llm);

        Bundle bundle = getArguments();
        if (bundle == null) throw new AssertionError();
        foods = bundle.getParcelableArrayList("foodsData");

        FoodRecyclerViewAdapter adapter = new FoodRecyclerViewAdapter(getContext(), foods);
        rvColdMeal.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
