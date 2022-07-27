package model;

import java.util.ArrayList;

public class Improvement {

    private final String name;
    private int productionRate;
    private int foodRate;
    private int goldRate;
    private int price;
    private Tile tile;
    private ArrayList<Resource> givenResources;
    private final ArrayList<String> land;
    public Improvement(String name, int productionRate, int foodRate, int goldRate, int price, ArrayList<Resource> givenResources, ArrayList<String> land) {
        this.name = name;
        this.productionRate = productionRate;
        this.foodRate = foodRate;
        this.goldRate = goldRate;
        this.price = price;
        this.givenResources = givenResources;
        this.land = land;
    }

    public String getName() {
        return name;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public int getGoldRate() {
        return goldRate;
    }

    public int getFoodRate() {
        return foodRate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setFoodRate(int foodRate) {
        this.foodRate = foodRate;
    }

    public void setGoldRate(int goldRate) {
        this.goldRate = goldRate;
    }

    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }


    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public ArrayList<Resource> getGivenResources() {
        return givenResources;
    }

    public void setGivenResources(ArrayList<Resource> givenResources) {
        this.givenResources = givenResources;
    }

    public ArrayList<String> getLand() {
        return land;
    }
}
