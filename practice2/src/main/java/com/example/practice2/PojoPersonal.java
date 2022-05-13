package com.example.practice2;

public class PojoPersonal {
    public PojoPersonal(int id, String FIO, String birthday, String address, String telephone) {
        this.id = id;
        this.FIO = FIO;
        this.birthday = birthday;
        this.address = address;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    int id;
    String FIO;
    String birthday;
    String address;
    String telephone;



}
