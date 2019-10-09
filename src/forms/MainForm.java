package forms;

import fileManagement.StoreAppFileReader;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Customer;
import model.Product;
import model.Sale;
import store.Accounting;
import store.Bill;

public class MainForm extends javax.swing.JFrame { 
        private StoreAppFileReader storeAppFileReader;
        
        private List<Customer> customerList;
        private List<Product> productList;
        private List<Sale> saleList;
        
        private List<Bill> billList;
        private int billCount;
        
        private DefaultTableModel defaultTableModel;

    public MainForm() {
        initComponents();
        
        this.storeAppFileReader = new StoreAppFileReader();
        this.billList = new ArrayList<>();
        this.billCount = 1;
        
        prepareTextArea(txtCustomers);
        prepareTextArea(txtProducts);
        prepareTextArea(txtSales);
        prepareTextArea(txtBills);
        prepareTextArea(txtNotes);
        
        this.defaultTableModel = new DefaultTableModel();
        prepareTable(this.defaultTableModel);
        showTable(this.defaultTableModel);
        
        fillLists();
        showLists();
        getBills();
        showBills();
        
        getNotes();
    }
    
    private void fillLists() {
        this.customerList = storeAppFileReader.getCustomerList();
        this.productList = storeAppFileReader.getProductList();
        this.saleList = storeAppFileReader.getSaleList();
    }
    
    private void showLists() {
        String text = null;
        for (Customer customer : this.customerList) {
            text = new StringBuilder()
                    .append("Cédula: " + customer.getId() + "\n")
                    .append("nombre: " + customer.getName()+ "\n")
                    .append("\n").toString();
            
            txtCustomers.append(text);
        }
        
        for (Product product : this.productList) {
            text = new StringBuilder()
                .append("Código: " + product.getCode()+ "\n")
                .append("Descripción: " + product.getDescription()+ "\n")
                .append("Precio: $" + product.getPrice()+ "\n")
                .append("\n").toString();
            
            txtProducts.append(text);
        }
        
        for (Sale sale : this.saleList) {
            text = new StringBuilder()
                .append("Cédula del cliente: " + sale.getCustomerId()+ "\n")
                .append("Código del producto: " + sale.getProductCode()+ "\n")
                .append("Medio de pago: " + sale.getPaymentMethod()+ "\n")
                .append("Cantidad: " + sale.getQuantity() + "\n")
                .append("\n").toString();
            
            txtSales.append(text);
        }
    }
    
    private void getBills() {      
        for (Customer customer : this.customerList) {
            //cash
            Bill cashBill = new Bill();
            double totalCash = 0;
            String cashBillCode = new StringBuilder()
                    .append("BC0")
                    .append(String.valueOf(billCount)).toString();

            cashBill.setCode(cashBillCode);
            cashBill.setCustomer(customer);
            
            List<Product> cashProducts = new ArrayList<>();
            cashProducts = storeAppFileReader.getProductsBy(this.saleList, this.productList, customer.getId(), "cash");
            for (Product cashProduct : cashProducts) {
                totalCash = totalCash + cashProduct.getPrice();
            }          
            
            cashBill.setProducts(cashProducts);
            cashBill.setTotalItems(cashProducts.size());
            cashBill.setTotalPrice(totalCash);
            cashBill.setPaymentMethod("cash");
            
            this.billList.add(cashBill);
            billCount++;
            
            //credit
            Bill creditBill = new Bill();
            double totalCredit = 0;
            String creditBillCode = new StringBuilder()
                    .append("BC0")
                    .append(String.valueOf(billCount)).toString();

            creditBill.setCode(creditBillCode);
            creditBill.setCustomer(customer);
            
            List<Product> creditProducts = new ArrayList<>();
            creditProducts = storeAppFileReader.getProductsBy(this.saleList, this.productList, customer.getId(), "credit");
            for (Product creditProduct : creditProducts) {
                totalCredit = totalCredit + creditProduct.getPrice();
            }
            
            creditBill.setProducts(creditProducts);
            creditBill.setTotalItems(creditProducts.size());
            creditBill.setTotalPrice(totalCredit);
            creditBill.setPaymentMethod("credit");
            
            this.billList.add(creditBill);
            billCount++;
            
            String customerType = Accounting.getCustomerType(totalCash, totalCredit);
            fillTable(this.defaultTableModel, customerType, customer);
        }
    }
    
    private void showBills() {
        String text = "";
        for (Bill bill : this.billList) {
            text = new StringBuilder()
                    .append("Código de la factura: " + bill.getCode() + "\n")
                    .append("Fecha: " + bill.getDate() + "\n")
                    .append("Cédula del cliente:" + bill.getCustomer().getId() + "\n")
                    .append("Nombre del cliente:" + bill.getCustomer().getName() + "\n")
                    .append("Productos:" + "\n").toString();            
            
            for (Product product : bill.getProducts()) {
                text = text + new StringBuilder()
                        .append("\tCódigo: " + product.getCode() + "\n")
                        .append("\tDescripción: " + product.getDescription() + "\n")
                        .append("\tPrecio: $" + product.getPrice() + "\n").toString();
            }
            
            text = text + new StringBuilder()
                .append("Total de items: " + bill.getTotalItems() + "\n")
                .append("Total factura: " + bill.getTotalPrice() + "\n")
                .append("Medio de pago: " + bill.getPaymentMethod() + "\n")
                .append("------------------------------------------------------------------------------------------")
                .append("\n").toString();
            
            txtBills.append(text);
        }
    }
    
    private void prepareTextArea(JTextArea textArea) {
        Font font = new Font("Verdana", Font.ITALIC, 10);
        textArea.setFont(font);     
        textArea.setLineWrap (true);
        textArea.setWrapStyleWord(true);        
        textArea.setEditable(false);
    }
    
    private void prepareTable(DefaultTableModel defaultTableModel) {
        defaultTableModel.addColumn("Tipo de cliente");
        defaultTableModel.addColumn("Cédula del cliente");
        defaultTableModel.addColumn("Nombre del cliente");
    }
    
    private void fillTable(DefaultTableModel tableModel, String customerType, Customer customer) {
        String[] rowData = new String[3];
        rowData[0] = customerType;
        rowData[1] = customer.getId();
        rowData[2] = customer.getName();
        
        tableModel.addRow(rowData);
    }
    
    private void showTable(DefaultTableModel defaultTableModel) {
        tblCasifications.setModel(defaultTableModel);
    }
    
    private void getNotes() {
        String code = Accounting.getMoreItemsPurchaseCode(this.billList);
        String top3Purchases = Accounting.getTop3Purchases(this.billList);
        
        String note = new StringBuilder()
                .append("La compra con más artículos es la factura con código: \n")
                .append(code)
                .append("\n\n")
                .append("Los precios de las 3 facturas más costosas son: \n")
                .append(top3Purchases).toString();
        
        txtNotes.setText(note);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCustomers = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtProducts = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSales = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCasifications = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtBills = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("INSTITUCIÓN UNIVERSITARIA ITM");

        jLabel2.setText("TECNOLOGÍA EN SISTEMAS DE INFORMACIÓN");

        jLabel3.setText("TALLER EVALUABLE #2");

        jLabel4.setText("Contenido de los archivos");

        txtCustomers.setColumns(20);
        txtCustomers.setRows(5);
        jScrollPane1.setViewportView(txtCustomers);

        jLabel5.setText("Customer.txt");

        jLabel6.setText("Product.txt");

        txtProducts.setColumns(20);
        txtProducts.setRows(5);
        jScrollPane2.setViewportView(txtProducts);

        txtSales.setColumns(20);
        txtSales.setRows(5);
        jScrollPane3.setViewportView(txtSales);

        jLabel7.setText("Sale.txt");

        jLabel8.setText("Clasificación de los clientes");

        tblCasifications.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblCasifications);

        jLabel9.setText("Facturas detalladas");

        txtBills.setColumns(20);
        txtBills.setRows(5);
        jScrollPane5.setViewportView(txtBills);

        jLabel10.setText("Observaciones");

        txtNotes.setColumns(20);
        txtNotes.setRows(5);
        jScrollPane6.setViewportView(txtNotes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(376, 376, 376))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(344, 344, 344))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(403, 403, 403))))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane6))
                .addGap(0, 50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    private static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tblCasifications;
    private javax.swing.JTextArea txtBills;
    private javax.swing.JTextArea txtCustomers;
    private javax.swing.JTextArea txtNotes;
    private javax.swing.JTextArea txtProducts;
    private javax.swing.JTextArea txtSales;
    // End of variables declaration//GEN-END:variables
}
