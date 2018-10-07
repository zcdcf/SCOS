package es.source.code.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.activity.R;
import es.source.code.model.Food;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {
    private List<Food> foods;
    private Context context;

    public FoodRecyclerViewAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Food food = foods.get(position);

        holder.tvMealName.setText(food.getName());
        holder.tvMealPrice.setText(String.valueOf(food.getPrice()));

        holder.bOrderThisMeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "点菜成功"+holder, Toast.LENGTH_SHORT);
                toast.show();
                holder.bOrderThisMeal.setText("退菜");
            }
        });
    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        @BindView(R.id.bOrderThisMeal)
        Button bOrderThisMeal;
        @BindView(R.id.tvMealName)
        TextView tvMealName;
        @BindView(R.id.tvMealPrice)
        TextView tvMealPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.bOrderThisMeal) {
                Toast toast = Toast.makeText(getContext(), "点菜成功", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getContext(), "点菜", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}



