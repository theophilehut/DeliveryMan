package com.example.thophile.deliveryman;

import java.util.ArrayList;

public class DeliveryData {

    public static int STATUSINVALID = -1;
    public static int STATUSWAITING = 0;
    public static int STATUSPROPOSAL = 1;
    public static int STATUSACCEPTED = 2;
    public static int STATUSFINISHED = 3;

    private String restaurantAdress;
    private String deliveryAdress;
    private String pickupTime;
    private int status;
    private String restaurantKey;
    private String customerKey;
    private String deliverymanKey;

    public DeliveryData(int deliveryID, String restaurantAdress, String deliveryAdress, String pickupTime) {
        this.restaurantAdress = restaurantAdress;
        this.deliveryAdress = deliveryAdress;
        this.pickupTime = pickupTime;
    }

    public DeliveryData(String restaurantAdress, String deliveryAdress, String pickupTime, int status) {
        this.restaurantAdress = restaurantAdress;
        this.deliveryAdress = deliveryAdress;
        this.pickupTime = pickupTime;
        this.status = status;
    }

    public DeliveryData (DeliveryData deliveryData){
        this.restaurantAdress = deliveryData.restaurantAdress;
        this.deliveryAdress = deliveryData.deliveryAdress;
        this.pickupTime = deliveryData.pickupTime;
    }

    public DeliveryData() {
        this.status = STATUSINVALID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getDeliverymanKey() {
        return deliverymanKey;
    }

    public void setDeliverymanKey(String deliverymanKey) {
        this.deliverymanKey = deliverymanKey;
    }

    public int getStatus() {
        return status;
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
