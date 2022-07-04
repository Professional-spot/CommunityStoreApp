package com.dzhosef.communitystore;


public class HelperClass {
    String name,product,userid,task,unit;
    Double price;
    String amount;

    long time;

    public HelperClass(String name,String product,Double price,String userid,String task,long time,String amount,String unit){
        this.name=name;
        this.amount=amount;
        this.unit=unit;
        this.product =product;
        this.price =price;
        this.userid=userid;
        this.task=task;
        this.time=time;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }


}
