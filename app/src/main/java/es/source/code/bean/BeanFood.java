package es.source.code.bean;

public class BeanFood {
    private int STOCK;
    private String NAME;
    private double PRICE;
    private int TYPE;


    @Override
    public String toString() {
        return NAME+" price="+PRICE+" type="+TYPE+" stock="+STOCK;
    }

    public void setSTOCK(int STOCK) {
        this.STOCK = STOCK;
    }

    public int getSTOCK() {
        return this.STOCK;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public double getPRICE() {
        return this.PRICE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public int getTYPE() {
        return TYPE;
    }
}
