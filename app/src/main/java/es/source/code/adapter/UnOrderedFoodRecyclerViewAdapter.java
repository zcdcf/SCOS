package es.source.code.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.MenuData;

public class UnOrderedFoodRecyclerViewAdapter extends RecyclerView.Adapter<es.source.code.adapter.UnOrderedFoodRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Food> foods;
    private Context context;
    private String pageTitle;
    private MenuData menuData;
    private TextView tvPrice;
    private TextView tvCount;

    public UnOrderedFoodRecyclerViewAdapter(Context context, ArrayList<Food> foods, String pageTitle, TextView tvPrice, TextView tvCount) {
        this.context = context;
        this.foods = foods;
        this.pageTitle = pageTitle;
        this.tvPrice = tvPrice;
        this.tvCount = tvCount;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public es.source.code.adapter.UnOrderedFoodRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unordered_meal, parent, false);

        return new es.source.code.adapter.UnOrderedFoodRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final es.source.code.adapter.UnOrderedFoodRecyclerViewAdapter.ViewHolder holder, final int position) {
        if (foods != null) {
            Log.i("use adapter","unordered"+foods.size());
            final Food food = foods.get(position);
            menuData = (MenuData) context.getApplicationContext();

            holder.tvMealNameUnOrdered.setText(food.getName());
            holder.tvMealPriceUnOrdered.setText(String.valueOf(food.getPrice()));
            holder.tvMealCountUnOrdered.setText(String.valueOf(food.getCount()));

            holder.bUnSubmitThisMealInOrderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.bUnSubmitThisMealInOrderView.getText().equals("退点")) {
                        Toast toast = Toast.makeText(getContext(), "退菜成功", Toast.LENGTH_SHORT);
                        toast.show();
                        menuData.setUnSubmited(food.getType(),food.getPosition());
                        menuData.minusCount(food.getType(),food.getPosition());
//                        foods.remove(position);
                        double price = calTotalPrice(foods);
                        tvPrice.setText("订单总价："+String.valueOf(price));
                        tvCount.setText("菜品总数："+foods.size());
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (foods == null) {
            return 0;
        } else {
            return foods.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvMealNameUnOrdered)
        TextView tvMealNameUnOrdered;
        @BindView(R.id.tvMealPriceUnOrdered)
        TextView tvMealPriceUnOrdered;
        @BindView(R.id.tvMealCountUnOrdered)
        TextView tvMealCountUnOrdered;
        @BindView(R.id.tvMoreInfoUnOrdered)
        TextView tvMoreInfoUnOrdered;
        @BindView(R.id.bUnSubmitThisMealInOrderView)
        Button bUnSubmitThisMealInOrderView;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
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
}
