package es.source.code.model;

import android.app.Application;

import java.util.ArrayList;

import es.source.code.activity.R;

public class MenuData extends Application {
    private String[][] mealNames = {{"凉拌木耳","鸭舌"},{"红烧牛肉","童子鸡"},{"肉蟹煲","皮皮虾"},{"黄酒","啤酒"}};
    private double[][] mealPrice = {{12, 16},{36,40},{40,60},{20,10}};
    private int[][] mealType = {{0,0},{1,1},{2,2},{3,3}};
    private ArrayList<ArrayList<Food>> foodLists = new ArrayList<>();
    private boolean hasInitialized =false;
    private int[][] foodImageID= {{R.drawable.cold_fungus,R.drawable.duck_tongue},{R.drawable.braised_beef,R.drawable.son_chicken},
        {R.drawable.meat_crab,R.drawable.pipixia},{R.drawable.yellow_wine,R.drawable.beer}};
    static private int foodNums = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    public ArrayList<ArrayList<Food>> getFoodLists() {
        initData();
        return foodLists;
    }

    public void addNewFood(Food food) {
        foodLists.get(food.getType()).add(food);
    }

    private void initData() {
        if(!hasInitialized) {
            for (int i = 0; i < mealNames.length; i++) {
                ArrayList<Food> foodsInfo = new ArrayList<>();
                for (int j = 0; j < mealNames[i].length; j++) {
                    Food foodInfo = new Food(mealNames[i][j], mealPrice[i][j], mealType[i][j],foodImageID[i][j],j,foodNums);
                    foodsInfo.add(foodInfo);
                    foodNums++;
                }
                foodLists.add(foodsInfo);
            }
            hasInitialized = true;
        }
    }

    public void setOrdered(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).setOrdered();
    }

    public void setOrdered(int mealType, int position) {
        foodLists.get(mealType).get(position).setOrdered();
    }

    public void setUnOrdered(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).setUnOrdered();
    }

    public void setUnOrdered(int mealType, int position) {
        foodLists.get(mealType).get(position).setUnOrdered();
    }

    public void setSubmited(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).setSubmited();
    }

    public void setSubmited(int mealType, int position) {
        foodLists.get(mealType).get(position).setSubmited();
    }

    public void setUnSubmited(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).setUnSubmited();
        foodLists.get(changePageTitleToNum(mealType)).get(position).setUnOrdered();
    }

    public void setUnSubmited(int mealType, int position) {
        foodLists.get(mealType).get(position).setUnSubmited();
        foodLists.get(mealType).get(position).setUnOrdered();
    }

    public Food getFood(String mealType, int position) {
        return foodLists.get(changePageTitleToNum(mealType)).get(position);
    }

    public void addCount(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).addCount();
    }

    public void minusCount(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).minusCount();
    }

    public void minusCount(int mealType, int position) {
        foodLists.get(mealType).get(position).minusCount();
    }

    public void setRemark(int mealType, int position, String remark) {
        foodLists.get(mealType).get(position).setRemark(remark);
    }

    private int changePageTitleToNum(String mealType) {
        switch (mealType) {
            case "冷菜":return GlobalConst.TYPE_COLD_MEAL;
            case "热菜":return GlobalConst.TYPE_HOT_MEAL;
            case "海鲜":return GlobalConst.TYPE_SEA_FOOD;
            case "酒水":return GlobalConst.TYPE_LIQUOR;
            default:break;
        }
        return -1;
    }
}
