package es.source.code.model;

import java.io.Serializable;

public class FoodStockInfo implements Serializable {
    private String foodName;
    private int stock;

    public FoodStockInfo(String foodName, int stock) {
        this.foodName = foodName;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

}
