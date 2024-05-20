package nds.pkg2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Button prod_addbtn;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private Spinner<Integer> prod_spinner;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private productData prodData;
    private Image image;
    private SpinnerValueFactory<Integer> spin;
    private Alert alert;

    private String prodID;
    private String prod_type;
    private String prod_image;
    private String prod_date;

    public void setData(productData prodData) {
        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        prod_type = prodData.getProductType();
        prodID = prodData.getProductId();
        prod_name.setText(prodData.getProductName());
        prod_price.setText("Rs." + String.valueOf(prodData.getPrice()));
        String path = "File:" + prodData.getImage();
        image = new Image(path, 190, 94, false, true);

        prod_imageView.setImage(image);
        pr = prodData.getPrice();
    }

    private int qty;

    //Spinner
    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;

    public void addBtn() {
        AdminPage1Controller adp1 = new AdminPage1Controller();
        adp1.customprodID();
        qty = prod_spinner.getValue();
        String check = "";
        String checkAvailable = "SELECT status FROM product WHERE product_name = ?";

        connect = database.connectDB();
        try {
            prepare = connect.prepareStatement(checkAvailable);
            prepare.setString(1, prod_name.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }

            if (!check.equals("Available") || qty == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message.");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :3");
                alert.showAndWait();
            } else {
                int checkStck = 0;
                String checkStockQuery = "SELECT stock FROM product WHERE product_id = ?";
                prepare = connect.prepareStatement(checkStockQuery);
                prepare.setString(1, prodID);
                result = prepare.executeQuery();

                if (result.next()) {
                    checkStck = result.getInt("stock");
                }

                if (checkStck == 0) {
                    String updateStock = "UPDATE product SET product_name = ?, stock = ?, price = ?, status = 0 , image = ?, date = ?, product_type = ? WHERE product_id = ?";

                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();
                }

                if (checkStck < qty) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message.");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This Product is Out of Stock!");
                    alert.showAndWait();
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");
                    String insertData = "INSERT INTO customerprod (customprod_id,product_id, product_name, product_type,quantity, price,image, date, custom_name) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(data.cID));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, prod_type);
                    prepare.setInt(5, qty);
                    totalP = (qty * pr);
                    prepare.setDouble(6, totalP);

                    prepare.setString(7, prod_image);
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(8, sqlDate);
                    prepare.setString(9, data.username);

                    prepare.executeUpdate();

                    int upStock = checkStck - qty;

                    String updateStock = "UPDATE product SET product_name = ?, stock = ?, price = ?, status = ?, image = ?, date = ?, product_type = ? WHERE product_id = ?";

                    prepare = connect.prepareStatement(updateStock);
                    prepare.setString(1, prod_name.getText());

                    prepare.setInt(2, upStock);
                    prepare.setDouble(3, pr);
                    prepare.setString(4, check);
                    prepare.setString(5, prod_image);
                    prepare.setDate(6, java.sql.Date.valueOf(prod_date));
                    prepare.setString(7, prod_type);
                    prepare.setString(8, prodID);

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message.");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
    
                    adp1.menuDisplayTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setQuantity();
    }

}
