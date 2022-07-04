package com.dzhosef.communitystore;


public class HelperClass2 {
    String username,location;
    Long number;

    public HelperClass2(String username, Long number, String location){

        this.username =username;
        this.number =number;
        this.location=location;

    }



    public String getName() {
        return username;
    }
    public void setName(String username) {
        this.username = username;
    }

    public Long getNumber() {
        return number;
    }
    public void setNumber(Long number) {
        this.number = number;
    }

    public String getAddress() {
        return location;
    }
    public void setAddress(String location) {
        this.location = location;
    }


}
