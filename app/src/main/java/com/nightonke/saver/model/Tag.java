package com.nightonke.saver.model;

/**
 * Created by 伟平 on 2015/11/3.
 */

public class Tag {

    private int id;
    private String name;
    private int weight;
    private int dragId;

    public int getDragId() {
        return dragId;
    }

    public void setDragId(int dragId) {
        this.dragId = dragId;
    }

    public String toString() {
        return "Tag(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "weight = " + weight + ")";
    }

    public Tag() {

    }

    public Tag(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    public void set(Tag tag) {
        this.id = tag.id;
        this.name = tag.name;
        this.weight = tag.weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
