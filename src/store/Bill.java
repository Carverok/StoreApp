package store;

import java.util.Date;
import java.util.List;
import model.Customer;
import model.Product;

public class Bill {
    private String code;
    private Date date;
    private Customer customer;
    private List<Product> products;
    private int totalItems;
    private double totalPrice;        
    private String paymentMethod;

    public Bill() {
        this.code = "";
        this.date = new Date();
        this.customer = null;
        this.products = null;
        this.totalItems = 0;
        this.totalPrice = 0;
        this.paymentMethod = "";
    }

    public Bill(String code, Date date, Customer customer, List<Product> products, int totalItems, double totalPrice, String paymentMethod) {
        this.code = code;
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
