package com.just2fast.ushop;

import java.util.List;

public class Person {
    String title;
    int price;
    List<String> size;

    public Person(String title,int price,List<String> size){
        this.price = price;
        this.size = size;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Person{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", size=" + size +
                '}';
    }

}
