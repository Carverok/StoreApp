package model;

public class Product
{
    private String code;
    private double price;
    private String description;

    public Product() {
        this.code = "";
        this.price = 0;
        this.description = "";
    }

    public Product(String code, double price, String description) {
        this.code = code;
        this.price = price;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    
}
