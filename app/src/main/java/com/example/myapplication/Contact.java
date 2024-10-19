package com.example.myapplication;

public class Contact {
    private int id;
    private String name;
    private String phone;

    public Contact(String name, int id, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }
}
