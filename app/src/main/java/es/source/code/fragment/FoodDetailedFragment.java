package es.source.code.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;


public class FoodDetailedFragment extends Fragment {

    @BindView(R.id.ivDetailedFoodImage)
    ImageView ivDetailedFoodImage;
    @BindView(R.id.tvDetailedFoodName)
    TextView tvDetailedFoodName;
    @BindView(R.id.bDetailedSubmittedOrUn)
    Button bDetailedSubmittedOrUn;
    @BindView(R.id.tvDetailedFoodPrice)
    TextView tvDetailedFoodPrice;
    @BindView(R.id.etDetailedFoodRemark)
    EditText etDetailedFoodRemark;
    Unbinder unbinder;

    private Food food;
    MenuData menuData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.fragment_food_detailed, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());

        menuData = (MenuData) getContext().getApplicationContext();
        Bundle bundle = getArguments();
        assert bundle != null;
        food = bundle.getParcelable("foodData");

        tvDetailedFoodName.setText("菜名:"+food.getName());
        tvDetailedFoodPrice.setText("价格:"+String.valueOf(food.getPrice()));
        ivDetailedFoodImage.setImageResource(food.getImageId());
        if(food.getSubmited()==GlobalConst.SUBMITTED) {
            bDetailedSubmittedOrUn.setText("退点");
        } else {
            bDetailedSubmittedOrUn.setText("点菜");
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i("position:",String.valueOf(food.getPosition()));
        Log.i("type:",String.valueOf(food.getType()));
        Log.i("remark:",etDetailedFoodRemark.getText().toString());
        menuData.setRemark(food.getType(),food.getPosition(),etDetailedFoodRemark.getText().toString());
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bDetailedSubmittedOrUn)
    public void onViewClicked() {
        if(food.getSubmited()==GlobalConst.SUBMITTED) {
            menuData.setUnSubmited(String.valueOf(food.getType()),food.getPosition());
        } else {
            menuData.setSubmited(String.valueOf(food.getType()),food.getPosition());
        }
    }

    @OnTextChanged(R.id.etDetailedFoodRemark)
    public void onTextChange() {

    }

}
