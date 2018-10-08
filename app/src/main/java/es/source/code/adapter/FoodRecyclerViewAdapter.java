package es.source.code.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;

import static android.support.v4.content.ContextCompat.startActivity;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {
    private List<Food> foods;
    private Context context;
    private String pageTitle;
    private MenuData menuData;

    public FoodRecyclerViewAdapter(Context context, List<Food> foods, String pageTitle) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Food food = foods.get(position);
        menuData = (MenuData) context.getApplicationContext();

        holder.tvMealName.setText(food.getName());
        holder.tvMealPrice.setText(String.valueOf(food.getPrice()));

        if(menuData.getFood(pageTitle,position).getSubmited()==GlobalConst.SUBMITTED) {
            holder.bOrderThisMeal.setText("退菜");
        } else {
            holder.bOrderThisMeal.setText("点菜");
        }

        holder.bOrderThisMeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.bOrderThisMeal.getText().equals("点菜")) {
                    Toast toast = Toast.makeText(getContext(), "点菜成功", Toast.LENGTH_SHORT);
                    toast.show();
                    holder.bOrderThisMeal.setText("退菜");
                    menuData.setSubmited(pageTitle,position);
                    menuData.addCount(pageTitle,position);
                } else if(holder.bOrderThisMeal.getText().equals("退菜")) {
                    Toast toast = Toast.makeText(getContext(), "退菜成功", Toast.LENGTH_SHORT);
                    toast.show();
                    holder.bOrderThisMeal.setText("点菜");
                    menuData.setUnSubmited(pageTitle,position);
                    menuData.minusCount(pageTitle,position);
                }
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
            int position = getAdapterPosition();
            if(v.getId()==R.id.bOrderThisMeal) {
                /*
                Toast toast = Toast.makeText(getContext(), "点菜成功", Toast.LENGTH_SHORT);
                toast.show();
                */
            } else {
                Intent intent  = new Intent(context,FoodDetailed.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("page",menuData.getFood(pageTitle,position).getFoodID());
                context.startActivity(intent);
                Toast toast = Toast.makeText(getContext(), "点菜", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}



