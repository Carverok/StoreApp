package fileManagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Customer;
import model.Product;
import model.Sale;

public class StoreAppFileReader {
    private BufferedReader customerBufferedReader;
    private BufferedReader productBufferedReader;
    private BufferedReader saleBufferedReader;

    public StoreAppFileReader() {
        try {
            this.customerBufferedReader = new BufferedReader(new FileReader("D:\\Mis documentos\\NetBeansProjects\\StoreApp\\src\\data\\Customer.txt"));
            this.productBufferedReader = new BufferedReader(new FileReader("D:\\Mis documentos\\NetBeansProjects\\StoreApp\\src\\data\\Product.txt"));
            this.saleBufferedReader = new BufferedReader(new FileReader("D:\\Mis documentos\\NetBeansProjects\\StoreApp\\src\\data\\Sale.txt"));
        } catch (FileNotFoundException fileNotFoundException) {
            JOptionPane.showMessageDialog(null, fileNotFoundException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Customer> getCustomerList() {
        List<Customer> customers = null;
        Customer customer = null;
        try {
            customers = new ArrayList();
            String fullLine = this.customerBufferedReader.readLine();
            while (fullLine != null) {
                String[] splitLine = fullLine.split(",");
                
                customer = new Customer();
                customer.setId(splitLine[0]);
                customer.setName(splitLine[1]);                
                customers.add(customer);
                
                fullLine = this.customerBufferedReader.readLine();
            }

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, ioException.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }

        return customers;
    }

    public List<Product> getProductList() {
        List<Product> products = null;
        Product product = null;
        try {
            products = new ArrayList<>();
            String fullLine = this.productBufferedReader.readLine();
            while (fullLine != null) {
                String[] splitLine = fullLine.split(",");
                
                product = new Product();
                product.setCode(splitLine[0]);
                product.setPrice(Double.parseDouble(splitLine[1]));
                product.setDescription(splitLine[2]);
                products.add(product);
                
                fullLine = this.productBufferedReader.readLine();
            }

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, ioException.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }

        return products;
    }
    
    public List<Sale> getSaleList() {
        List<Sale> sales = null;
        Sale sale = null;
        try {
            sales = new ArrayList<>();
            String fullLine= this.saleBufferedReader.readLine();
            
            while (fullLine != null) {
                String[] splitLine = fullLine.split(",");
                
                sale = new Sale();
                sale.setCustomerId(splitLine[0]);
                sale.setProductCode(splitLine[1]);
                sale.setPaymentMethod(splitLine[2]);
                sale.setQuantity(Integer.parseInt(splitLine[3]));
                sales.add(sale);
                
                fullLine = this.saleBufferedReader.readLine();
            }
        }catch(IOException ioException) {
            JOptionPane.showMessageDialog(null, ioException.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }
        
        return sales;
    }

    public List<Product> getProductsBy(List<Sale> sales, List<Product> products, String customerId, String paymentMethod) {
        List<Product> response = new ArrayList<>();
        
        for (Sale sale : sales) {
            if (sale.getCustomerId().equals(customerId) && sale.getPaymentMethod().equals(paymentMethod)) {
                for (Product product : products) {
                    if (product.getCode().equals(sale.getProductCode())) {
                        product.setPrice(sale.getQuantity() * product.getPrice());
                        response.add(product);
                    }
                }
            }
        }

        return response;
    }
}
