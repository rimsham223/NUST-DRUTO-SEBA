package nds.pkg2;

import java.util.Date;

public class productData {

    //attributes
    private Integer id;
    private String productid;
    private String productName;
    private String type;
    private Integer stock;
    private Double price;
    private String status;
    private String image;
    private Date date;
    private Integer quantity;

    //constructor
    public productData(Integer id, String productid, String productName,String type,
            Integer stock, Double price, String status, String image, Date date) {

        this.id = id;
        this.productid = productid;
        this.productName = productName;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
   
    }
    public productData( Integer id, String productid, String productName,String type,Integer quantity, Double price, String image, Date date){
        this.id = id;
        this.productid = productid;
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.date = date;
    }

    //getter setters
    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productid;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return type;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getProductStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
}
