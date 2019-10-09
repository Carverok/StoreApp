package model;

public class Sale {
    private String customerId;
    private String productCode;
    private String paymentMethod;
    private int quantity;

    public Sale() {
        this.customerId = "";
        this.productCode = "";
        this.paymentMethod = "";
        this.quantity = 0;
    }

    public Sale(String customerId, String productCode, String paymentMethod, int quantity) {
        this.customerId = customerId;
        this.productCode = productCode;
        this.paymentMethod = paymentMethod;
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
