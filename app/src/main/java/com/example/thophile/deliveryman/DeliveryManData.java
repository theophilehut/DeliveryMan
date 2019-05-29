package com.example.thophile.deliveryman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryManData {

    public static int DM_STATUSWAITING = 1;
    public static int DM_STATUSOFFLINE = 0;
    public static int DM_STATUSPROPOSAL = 2;
    public static int DM_STATUSONCOURSE = 3;


    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String description;
    private int status;

    private DeliveryData currentDelivery;
    private List<DeliveryData> pastDeliveries;

    public void setCurrentDelivery(DeliveryData currentDelivery) {
        this.currentDelivery = currentDelivery;
    }

    public void setPastDeliveries(List<DeliveryData> pastDeliveries) {
        this.pastDeliveries = pastDeliveries;
    }

    public DeliveryData getCurrentDelivery() {
        return currentDelivery;
    }

    public void finishCurrentDelivery(){
        this.currentDelivery.setStatus(DeliveryData.STATUSFINISHED);
        if (this.pastDeliveries == null){
            pastDeliveries = new ArrayList<DeliveryData>();
        }
        this.pastDeliveries.add(this.currentDelivery);
    }
    public List<DeliveryData> getPastDeliveries() {
        return pastDeliveries;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DeliveryManData() {
        setCurrentDelivery(new DeliveryData());
    }

    public void updateDeliveryMan(DeliveryManData dm){
        this.setName(dm.getName());
        this.setPhone(dm.getPhone());
        this.setEmail(dm.getEmail());
        this.setPassword(dm.getPassword());
        this.setUsername(dm.getUsername());
        this.setCurrentDelivery(dm.getCurrentDelivery());
        this.setPastDeliveries(dm.getPastDeliveries());
        this.setDescription(dm.getDescription());
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
