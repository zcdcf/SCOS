package es.source.code.model;

import java.io.Serializable;

public class FoodStockInfo implements Serializable {
    private int positionInType;
    private int stock;

    public FoodStockInfo(int positionInType, int stock) {
        this.positionInType = positionInType;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


}
