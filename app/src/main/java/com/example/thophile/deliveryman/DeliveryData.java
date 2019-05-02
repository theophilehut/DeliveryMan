package com.example.thophile.deliveryman;

import java.util.ArrayList;

public class DeliveryData {
    private String restaurantAdress;
    private String deliveryAdress;
    private String pickupTime;

    public DeliveryData(String restaurantAdress, String deliveryAdress, String pickupTime) {
        this.restaurantAdress = restaurantAdress;
        this.deliveryAdress = deliveryAdress;
        this.pickupTime = pickupTime;
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

    public ArrayList<String> toStringArrayList(){
        ArrayList<String> array = new ArrayList<String>();
        array.add(restaurantAdress);
        array.add(deliveryAdress);
        array.add(pickupTime);
        return array;
    }

}
