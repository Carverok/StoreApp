package model;

import java.util.ArrayList;
import java.util.List;

public class Customer
{
    private String id;
    private String name;

    public Customer() {
        this.id = "";
        this.name = "";
    }

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
