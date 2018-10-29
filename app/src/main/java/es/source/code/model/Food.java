package es.source.code.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Food implements Parcelable {
    private String name;
    private double price;
    private int type;
    private int ordered = GlobalConst.UNORDERED;
    private int submited = GlobalConst.NOT_SUBMITTED;
    private int count = 0;
    private String remark = "无";
    private int imageId;
    private int foodID;
    private int position;
    private int stock = 3;

    public Food(String name, double price, int type, int imageId, int position, int foodID) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.imageId = imageId;
        this.position = position;
        this.foodID = foodID;
    }

    protected Food(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        type = in.readInt();
        ordered = in.readInt();
        count = in.readInt();
        stock = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public int getOrdered() {
        return this.ordered;
    }

    public void setOrdered() {
        this.ordered = GlobalConst.ORDERED;
    }

    public void setUnOrdered() {
        this.ordered = GlobalConst.UNORDERED;
    }

    public int getSubmited() {
        return this.submited;
    }

    public void setSubmited() {
        this.submited = GlobalConst.SUBMITTED;
    }

    public void setUnSubmited() {
        this.submited = GlobalConst.NOT_SUBMITTED;
    }

    public void addCount() {
        this.count++;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getFoodID() {
        return this.foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void increaseStock() {
        stock++;
    }

    public void decreaseStock() {
        stock--;
    }

    public void minusCount() {
        if(this.count>0) {
            this.count--;
        } else {
            this.count = 0;
        }
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(type);
        dest.writeInt(ordered);
        dest.writeInt(count);
        dest.writeInt(stock);
    }
}
