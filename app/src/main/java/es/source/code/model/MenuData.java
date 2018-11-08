package es.source.code.model;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import es.source.code.activity.R;

public class MenuData extends Application {

    private static final String TAG = "MenuData state:";
    private String[][] mealNames = {{"凉拌木耳","鸭舌"},{"红烧牛肉","童子鸡"},{"肉蟹煲","皮皮虾"},{"黄酒","啤酒"}};
    private int[][] mealType = {{0,0},{1,1},{2,2},{3,3}};
    private ArrayList<ArrayList<Food>> foodLists = new ArrayList<>();
    private ArrayList<ArrayList<String>> foodNameLists = new ArrayList<>();
    private boolean hasInitialized =false;
    private int[][] foodImageID= {{R.drawable.cold_fungus,R.drawable.duck_tongue},{R.drawable.braised_beef,R.drawable.son_chicken},
            {R.drawable.meat_crab,R.drawable.pipixia},{R.drawable.yellow_wine,R.drawable.beer}};
    static private int foodNums = 0;
    private ArrayList<Food> submittedFoodList = new ArrayList<>();
    private ArrayList<Food> orderedFoodList = new ArrayList<>();
    private boolean foodSpeciesUpdate = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    public ArrayList<ArrayList<Food>> getFoodLists() {
        return foodLists;
    }

    public void addNewFood(Food food) {
        if(!hasExist(food)) {
            Log.i(TAG,"Add new food in the menu");
            foodLists.get(food.getType()).add(food);
            foodNameLists.get(food.getType()).add(food.getName());
            refreshFoodID();
        } else {
            Log.i(TAG,"the food has exist in the menu");
        }
        Log.i(TAG,"type"+food.getType()+ " List size = "+String.valueOf(foodNameLists.get(food.getType()).size()));
    }

    private void initData() {
        if(!hasInitialized) {
            Log.i(TAG,"initialize the MenuData");
            for (int i = 0; i < mealNames.length; i++) {
                ArrayList<Food> foodsInfo = new ArrayList<>();
                ArrayList<String> foodsNameInfo = new ArrayList<>();
                foodNums = 0;
                for (int j = 0; j < mealNames[i].length; j++) {
                    Food foodInfo = new Food(mealNames[i][j], mealPrice[i][j], mealType[i][j],foodImageID[i][j],j,foodNums);
                    foodsInfo.add(foodInfo);
                    foodsNameInfo.add(foodInfo.getName());
                    foodNums++;
                }
                foodLists.add(foodsInfo);
                foodNameLists.add(foodsNameInfo);
            }
            hasInitialized = true;
        }
    }

    public void setOrdered(String mealType, int position) {
        Food food = foodLists.get(changePageTitleToNum(mealType)).get(position);
        food.setOrdered();
        food.setUnSubmited();
        updateSubmittedFoodlist(food,GlobalConst.DELETE_ITEM);
        updateOrderedFoodlist(food,GlobalConst.ADD_ITEM);
    }

    public void setOrdered(int mealType, int position) {
        Food food = foodLists.get(mealType).get(position);
        food.setOrdered();
        food.setUnSubmited();
        updateSubmittedFoodlist(food,GlobalConst.DELETE_ITEM);
        updateOrderedFoodlist(food,GlobalConst.ADD_ITEM);
    }

    public void updateOrderedFoodlist(Food food, int operation) {
        if(operation==GlobalConst.ADD_ITEM) {
            orderedFoodList.add(food);
        } else if(operation==GlobalConst.DELETE_ITEM) {
            for(int i=0; i<orderedFoodList.size();i++) {
                if(orderedFoodList.get(i).getName().equals(food.getName())) {
                    orderedFoodList.remove(i);
                }
            }
        }
    }
    public void setUnOrdered(String mealType, int position) {
        Food food = foodLists.get(changePageTitleToNum(mealType)).get(position);
        food.setUnOrdered();
    }

    public void setUnOrdered(int mealType, int position) {
        Food food = foodLists.get(mealType).get(position);
        food.setUnOrdered();
    }

    public ArrayList<Food> getOrderedFoodlist() {
        return orderedFoodList;
    }
    public void setSubmited(String mealType, int position) {
        Food food = foodLists.get(changePageTitleToNum(mealType)).get(position);
        food.setSubmited();
        updateSubmittedFoodlist(food,GlobalConst.ADD_ITEM);
    }

    public void setSubmited(int mealType, int position) {
        Food food = foodLists.get(mealType).get(position);
        food.setSubmited();
        updateSubmittedFoodlist(food,GlobalConst.ADD_ITEM);
    }

    public ArrayList<Food> getSubmittedFoodlist() {
        return submittedFoodList;
    }

    public void updateSubmittedFoodlist(Food food, int operation) {
        if(operation==GlobalConst.ADD_ITEM) {
            submittedFoodList.add(food);
        } else if(operation==GlobalConst.DELETE_ITEM) {
            for(int i=0; i<submittedFoodList.size();i++) {
                if(submittedFoodList.get(i).getName().equals(food.getName())) {
                    submittedFoodList.remove(i);
                }
            }
        }
    }

    public void setUnSubmited(String mealType, int position) {
        Food food = foodLists.get(changePageTitleToNum(mealType)).get(position);
        food.setUnSubmited();
        updateSubmittedFoodlist(food,GlobalConst.DELETE_ITEM);
    }

    public void setUnSubmited(int mealType, int position) {
        Food food = foodLists.get(mealType).get(position);
        food.setUnSubmited();
        updateSubmittedFoodlist(food,GlobalConst.DELETE_ITEM);
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

    public void decreaseStock(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).decreaseStock();
    }

    public void increaseStock(String mealType, int position) {
        foodLists.get(changePageTitleToNum(mealType)).get(position).increaseStock();
    }

    public void setStock(int mealType, int position, int stock) {
        foodLists.get(mealType).get(position).setStock(stock);
    }

    public String[][] getMealNames() {
        return mealNames;
    }

    public void setMealNames(String[][] mealNames) {
        this.mealNames = mealNames;
    }


    public double[][] getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(double[][] mealPrice) {
        this.mealPrice = mealPrice;
    }

    private double[][] mealPrice = {{12, 16},{36,40},{40,60},{20,10}};

    public int[][] getMealType() {
        return mealType;
    }

    public void setMealType(int[][] mealType) {
        this.mealType = mealType;
    }


    public int[][] getFoodImageID() {
        return foodImageID;
    }

    public void setFoodImageID(int[][] foodImageID) {
        this.foodImageID = foodImageID;
    }



    public static int getFoodNums() {
        return foodNums;
    }

    public static void setFoodNums(int foodNums) {
        MenuData.foodNums = foodNums;
    }

    public ArrayList<ArrayList<String>> getFoodNameList() {
        return foodNameLists;
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


    public boolean isFoodSepciesUpdate() {
        return foodSpeciesUpdate;
    }

    public void setFoodSepciesUpdate(boolean foodSepciesUpdate) {
        this.foodSpeciesUpdate = foodSepciesUpdate;
    }

    private void refreshFoodID() {
        int foodNums = 0;
        for(int i=0; i<foodLists.size(); i++) {
            for(int j=0; j<foodLists.get(i).size(); j++) {
                foodLists.get(i).get(j).setFoodID(foodNums);
                Log.i(TAG,foodLists.get(i).get(j).getName()+" ID is"+foodNums);
                foodNums++;
            }
        }
    }

    // check if the food exist in the menu,if exist, update the stock
    private boolean hasExist(Food food) {
        for(int i=0; i<foodLists.size(); i++) {
            for(int j=0; j<foodLists.get(i).size(); j++) {
                if(foodLists.get(i).get(j).getName().equals(food.getName())) {
                    foodLists.get(i).get(j).setStock(food.getStock());
                    Log.i(TAG,food.getName()+ "stock has been changed to "+foodLists.get(i).get(j).getStock());
                    return true;
                }
            }
        }
        return false;
    }

    public Food getFoodInfo(String name, int type) {
        for(int i=0; i<foodLists.get(type).size(); i++) {
            if(foodLists.get(type).get(i).getName().equals(name)) {
                return foodLists.get(type).get(i);
            }
        }
        return null;
    }
}
