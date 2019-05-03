package com.example.thophile.deliveryman;

import java.util.ArrayList;

public class DeliveryData {
    private String restaurantAdress;
    private String deliveryAdress;
    private String pickupTime;
    private int deliveryID;

    public DeliveryData(int deliveryID, String restaurantAdress, String deliveryAdress, String pickupTime) {
        this.restaurantAdress = restaurantAdress;
        this.deliveryAdress = deliveryAdress;
        this.pickupTime = pickupTime;
    }

    public DeliveryData (DeliveryData deliveryData){
        this.deliveryID = deliveryData.deliveryID;
        this.restaurantAdress = deliveryData.restaurantAdress;
        this.deliveryAdress = deliveryData.deliveryAdress;
        this.pickupTime = deliveryData.pickupTime;
    }

    public String getRestaurantAdress() {
        return restaurantAdress;
    }

    public void setRestaurantAdress(String restaurantAdress) {
        this.restaurantAdress = restaurantAdress;
    }

    public String getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(String deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public ArrayList<String> toStringArrayList(){
        ArrayList<String> array = new ArrayList<String>();
        array.add(restaurantAdress);
        array.add(deliveryAdress);
        array.add(pickupTime);
        return array;
    }

}
