package model;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private String id;

    private String name;

    private String openTime;
    private String closeTime;
    private String  openDays;
    private String  address;

    private List<Book> books;

    public Library(){
        this.name = "No Book";
        books=new ArrayList<Book>();

    }
    public Library(String name, List<Book> books){
        this.name = name;
        this.books= books;
    }
    public String getName() {return name;}
    public void setName(String name){this.name=name;}
    public List<Book> getBooks(){return books;}

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOpenDays() {
        return openDays;
    }

    public void setOpenDays(String openDays) {
        this.openDays = openDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
