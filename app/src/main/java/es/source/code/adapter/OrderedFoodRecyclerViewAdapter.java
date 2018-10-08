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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.MenuData;


public class OrderedFoodRecyclerViewAdapter extends RecyclerView.Adapter<OrderedFoodRecyclerViewAdapter.ViewHolder> {
    private List<Food> foods;
    private Context context;
    private String pageTitle;
    private MenuData menuData;

    public OrderedFoodRecyclerViewAdapter(Context context, List<Food> foods, String pageTitle) {
        this.context = context;
        this.foods = foods;
        this.pageTitle = pageTitle;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oredered_meal, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (foods != null) {
            Log.i("use adapter:","order"+foods.size());
            Food food = foods.get(position);
            menuData = (MenuData) context.getApplicationContext();

            holder.tvMealNameOrdered.setText(food.getName());
            holder.tvMealPriceOrdered.setText(String.valueOf(food.getPrice()));
            holder.tvMealCountOrdered.setText(String.valueOf(food.getCount()));
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
        @BindView(R.id.tvMealNameOrdered)
        TextView tvMealNameOrdered;
        @BindView(R.id.tvMealPriceOrdered)
        TextView tvMealPriceOrdered;
        @BindView(R.id.tvMoreInfoOrdered)
        TextView tvMoreInfoOrdered;
        @BindView(R.id.tvMealCountOrdered)
        TextView tvMealCountOrdered;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
