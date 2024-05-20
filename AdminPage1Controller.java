package nds.pkg2;

import java.io.File;
import java.sql.Connection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AdminPage1Controller implements Initializable {

    @FXML
    private AnchorPane admin_form1;

    @FXML
    private ImageView adminfrom1_logo_image;

    @FXML
    private Button customer_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_addbtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearbtn;

    @FXML
    private TableColumn<productData, String> inventory_col_IDProduct;

    @FXML
    private TableColumn<productData, String> inventory_col_ProductType;

    @FXML
    private TableColumn<productData, String> inventory_col_Productname;

    @FXML
    private TableColumn<productData, String> inventory_col_date;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private Button inventory_deletebtn;

    @FXML
    private AnchorPane inventory_from;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importbtn;

    @FXML
    private TableView<productData> inventory_tableview;

    @FXML
    private Button inventory_updatebtn;

    @FXML
    private Label label_username;

    @FXML
    private Button logout_btn;

    @FXML
    private Button menu_btn;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productid;

    @FXML
    private TextField inventory_productname;

    @FXML
    private TextField inventory_stock;

    @FXML
    private ComboBox<String> inventory_status;

    @FXML
    private ComboBox<String> inventory_producttype;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<productData, String> menu_col_ProductName;

    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridpane;

    @FXML
    private Button menu_paybtn;

    @FXML
    private Button menu_receiptbtn;

    @FXML
    private Button menu_removebtn;

    @FXML
    private ScrollPane menu_scrollpane;

    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private Label menu_total;

    //alert
    private Alert alert;

    //product card
    private ObservableList<productData> cardListData = FXCollections.observableArrayList();

    //SHOW DATA ON OUR TABLE
    //connecting database
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    private Image image;

    //add button
    public void InventoryAddbtn() {
        if (inventory_productid.getText().isEmpty()
                || inventory_productname.getText().isEmpty()
                || inventory_producttype.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blanks.");
            alert.showAndWait();
        } else {
            String checkProdID = "SELECT product_id FROM product WHERE product_id= '" + inventory_productid.getText() + "'";
            connect = database.connectDB();

            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message.");
                    alert.setHeaderText(null);
                    alert.setContentText(inventory_productid.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO product (product_id, product_name, stock, price, status, image, date, product_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productid.getText());
                    prepare.setString(2, inventory_productname.getText());
                    prepare.setString(8, (String) inventory_producttype.getSelectionModel().getSelectedItem());

                    // Set stock as an integer
                    try {
                        int stock = Integer.parseInt(inventory_stock.getText());
                        prepare.setInt(3, stock);
                    } catch (NumberFormatException e) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Stock must be an integer.");
                        alert.showAndWait();
                        return;
                    }

                    // Set price as a double
                    try {
                        double price = Double.parseDouble(inventory_price.getText());
                        prepare.setDouble(4, price);
                    } catch (NumberFormatException e) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Price must be a number.");
                        alert.showAndWait();
                        return;
                    }

                    prepare.setString(5, (String) inventory_status.getSelectionModel().getSelectedItem());

                    String path = data.path.replace("\\", "\\\\");
                    prepare.setString(6, path);

                    java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
                    prepare.setDate(7, sqlDate);

                    prepare.executeUpdate();

                    // Product added successfully
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inventoryUpdateBtn() {

        if (inventory_productid.getText().isEmpty()
                || inventory_productname.getText().isEmpty()
                || inventory_producttype.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null
                || data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blanks.");
            alert.showAndWait();
        } else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET product_id = ?, product_name = ?, product_type = ?, stock = ?, price = ?, status = ?, image = ?, date = ? WHERE id = ?";

            connect = database.connectDB();

            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Product ID: " + inventory_productid.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, inventory_productid.getText());
                    prepare.setString(2, inventory_productname.getText());
                    prepare.setString(3, (String) inventory_producttype.getSelectionModel().getSelectedItem());

                    try {
                        int stock = Integer.parseInt(inventory_stock.getText());
                        prepare.setInt(4, stock);
                    } catch (NumberFormatException e) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Stock must be an integer.");
                        alert.showAndWait();
                        return;
                    }

                    try {
                        double price = Double.parseDouble(inventory_price.getText());
                        prepare.setDouble(5, price);
                    } catch (NumberFormatException e) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Price must be a number.");
                        alert.showAndWait();
                        return;
                    }

                    prepare.setString(6, (String) inventory_status.getSelectionModel().getSelectedItem());
                    prepare.setString(7, path);
                    java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
                    prepare.setDate(8, sqlDate);
                    prepare.setInt(9, data.id);

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message.");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                    //TO UPDATE DATA FROM TABLEVIEW
                    inventoryShowData();
                    //TO CLEAR FIELDS
                    inventoryClearBtn();

                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message.");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //CLEAR BUTTON BEHAVIOUR
    public void inventoryClearBtn() {

        inventory_productid.setText("");
        inventory_productname.setText("");
        inventory_producttype.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_status.getSelectionModel().clearSelection();
        data.id = 0;
        data.path = "";
        inventory_imageView.setImage(null);

    }

    //delete product
    public void inventoryDelteBtn() {

        if (data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blanks.");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productid.getText());
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE id= " + data.id;

                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Informstion Message.");
                    alert.setHeaderText(null);
                    alert.setContentText("SuccessfullY Deleted.");
                    alert.showAndWait();

                    //TO UPDATE DATA FROM TABLEVIEW
                    inventoryShowData();
                    //TO CLEAR FIELDS
                    inventoryClearBtn();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message.");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }

        }

    }

    //BEHAVIOUR FOR IMPORT BUTTON
    public void inventoryImportbtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg","*jpeg"));

        File file = openFile.showOpenDialog(admin_form1.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 120, 131, false, true);

            inventory_imageView.setImage(image);
        }

    }

    //MERGE ALL DATA (inventory data)
    public ObservableList<productData> inventoryDataList() {

        ObservableList<productData> prodListData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";
        connect = database.connectDB();

        //ERROR HANDLING
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;
            while (result.next()) {

                prodData = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("product_type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                prodListData.add(prodData);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return prodListData;
    }

    //SHOW INVENTORY DATA
    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_IDProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_Productname.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_ProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("productStatus"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableview.setItems(inventoryListData);
    }

    public void inventorySelectData() {

        productData prodData = inventory_tableview.getSelectionModel().getSelectedItem();
        int num = inventory_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productid.setText(prodData.getProductId());
        inventory_productname.setText(prodData.getProductName());

        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        Image image = new Image(path, 120, 131, false, true);

        inventory_imageView.setImage(image);

    }

    //choose type
    private String[] typeList = {"Meal", "Drinks", "Stationary"};

    public void inventoryTypeList() {
        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_producttype.setItems(listData);
    }

    //Status List
    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {
        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);
    }

    //Menu list
    public ObservableList<productData> menuGetData() {

        String sql = "SELECT * FROM product";
        ObservableList<productData> listData = FXCollections.observableArrayList();

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("product_type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return listData;
    }

    //display menu card
    public void menuDisplayCard() {
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridpane.getChildren().clear();
        menu_gridpane.getRowConstraints().clear();
        menu_gridpane.getColumnConstraints().clear();

        for (int i = 0; i < cardListData.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();
                cardC.setData(cardListData.get(i));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                menu_gridpane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }

    //DISPLAY ORDER
    public ObservableList<productData> menuGetOrder() {
        customprodID();
        ObservableList<productData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customerprod WHERE customprod_id = " + cID;

        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;
            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("product_type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prod);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return listData;
    }

    private ObservableList<productData> menuOrderListData;

    //show order data in table
    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        menu_col_ProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuOrderListData);
    }

    

    
    //MENU TOTAL
    private double totalP;

    public void menuGetTotal() {
        customprodID();
        String total = "SELECT customprod_id, SUM(price) AS total_price FROM customerprod  GROUP BY customprod_id HAVING customprod_id = ?";

        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(total);
            prepare.setInt(1, cID); // Assuming cID is an integer
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("total_price");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("Rs. " + totalP);
    }

    //MENU AMOUNT
    private double amount;
    private double change;

    public void menuAmount() {
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            if (amount < totalP) {
                menu_amount.setText("");
            } else {
                change = (amount - totalP);
                menu_change.setText("Rs. " + change);
            }
        }
    }

    //PAY BUTTON
    public void menuPayBtn() {

        if (totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customprod_id, total, date, custom_name) "
                    + "VALUES(?,?,?,?)";

            connect = database.connectDB();

            try {

                if (amount == 0) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Messaged");
                    alert.setHeaderText(null);
                    alert.setContentText("Something wrong :3");
                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        customprodID();
                        menuGetTotal();
                        prepare = connect.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        prepare.setString(3, String.valueOf(sqlDate));
                        prepare.setString(4, data.username);

                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();
                        alert.setContentText("Payment of Rs. " + totalP + " for custom product ID " + cID + " is successful.");
                        menuShowOrderData();
                        // LINK YOUR MAIN FORM
                    Parent root = FXMLLoader.load(getClass().getResource("feedback.fxml"));
                    
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    
                    stage.setTitle("Cafe Shop Management System");
                    stage.setMinWidth(200);
                    stage.setMinHeight(200);
                    
                    stage.setScene(scene);
                    stage.show();
                    
                    admin_form1.getScene().getWindow().hide();
                        
                        

                    } else {
                        alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //MENU RESTART
    public void menuRestart() {
        totalP = 0;
        change = 0;
        amount = 0;
        menu_total.setText("Rs.0.0");
        menu_amount.setText("");
        menu_change.setText("Rs.0.0");
    }
    //SELECT ORDER
    private int getid;
    
    public void menuSelectOrder() {
        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        // TO GET THE ID PER ORDER
        getid = prod.getId();

    }

    //REMOVE BUTTON
    public void menuRemoveBtn() {

        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM customerprod WHERE id = " + getid;
            connect = database.connectDB();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }

                menuShowOrderData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void switchForm(ActionEvent event) {
        //display dashboard
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_from.setVisible(false);
            menu_form.setVisible(false);
        } else if (event.getSource() == inventory_btn) {     //display inventory
            dashboard_form.setVisible(false);
            inventory_from.setVisible(true);
            menu_form.setVisible(false);
        } else if (event.getSource() == menu_btn) {    //display menu
            dashboard_form.setVisible(false);
            inventory_from.setVisible(false);
            menu_form.setVisible(true);

            //dipspaly menu cards in menu form
            menuDisplayCard();
        }
    }

    private int cID;

    public void customprodID() {
        String sql = "SELECT MAX(customprod_id) FROM customerprod";

        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customprod_id)");
            }

            String checkcID = "SELECT MAX(customprod_id) FROM receipt";

            prepare = connect.prepareStatement(checkcID);
            result = prepare.executeQuery();

            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customprod_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }
            data.cID = cID;

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    //logout
    public void logout() {

        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                //TO HIDE MAIN FORM
                logout_btn.getScene().getWindow().hide();

                //LINK YOUR LOGIN FORM AND SHOW IT
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setMinWidth(600);
                stage.setMinHeight(400);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //display username after welcome
    public void displayUsername() {

        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        label_username.setText(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        displayUsername();

        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();

        menuDisplayCard();
        menuGetOrder();
        menuDisplayTotal();
        menuShowOrderData();

    }

}
