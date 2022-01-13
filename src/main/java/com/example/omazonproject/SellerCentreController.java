package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This controller is responsible to control the events happening in the Seller Centre
 */
public class SellerCentreController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    // The seller name menu button(manage profile and logout) at seller centre
    private MenuButton sellerNameMenuButton;

    @FXML
    // The menu button(customer review, delivery status and orders) at seller centre
    private MenuButton menuButton;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    public List<Product> products = new ArrayList<>();

    private ProductListener productListener;

    private void getProduct() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet Result = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            Result = statement.executeQuery("SELECT * FROM product_info WHERE sellerEmail = '" + Seller.getEmail() + "'");

            int count = 0;
            while (Result.next()) {
                // add all product information that belongs to the seller into a products list
                products.add(count, new Product());
                products.get(count).setSellerName(Result.getString("sellerName"));
                products.get(count).setSellerEmail(Result.getString("sellerEmail"));
                products.get(count).setProductName(Result.getString("name"));
                products.get(count).setProductPrice(Result.getDouble("price"));
                products.get(count).setCategory(Result.getString("category"));
                products.get(count).setDescription(Result.getString("description"));
                products.get(count).setNumOfOneStars(Result.getInt("numOfOneStars"));
                products.get(count).setNumOfTwoStars(Result.getInt("numOfTwoStars"));
                products.get(count).setNumOfThreeStars(Result.getInt("numOfThreeStars"));
                products.get(count).setNumOfFourStars(Result.getInt("numOfFourStars"));
                products.get(count).setNumOfFiveStars(Result.getInt("numOfFiveStars"));
                products.get(count).setNumberOfSales((Result.getInt("numberOfSales")));
                products.get(count).setProductImagePath(Result.getString("imageName"));
                products.get(count).setProductStock(Result.getInt("numOfStock"));
                products.get(count).setAddress(Result.getString("sellerAddress"));
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (Result != null) {
                try {
                    Result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connectDB != null) {
                try {
                    connectDB.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void initialize() {

        // fill-in seller name in the menu button
        sellerNameMenuButton.setText("  " + Seller.getSellerName());

        // call the method to add all product information that belongs to the seller into a products list
        getProduct();

        productListener = new ProductListener() {
            @Override
            public void onClickListener(Product product) throws IOException {
                changeScene(product);
            }
        };

        int column = 0;
        int row = 1;

        try {

            for (int i = 0; i < products.size(); i++) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("product-template.fxml"));
                AnchorPane anchorPane = loader.load();

                ProductController productController = loader.getController();
                // set product name, product price and product image
                // display it at seller product page
                productController.setData(products.get(i), productListener);


                if (column == 4) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(28));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    // Manage profile selection at sellerNameMenuButton at seller centre
    public void manageProfilePressed(ActionEvent event) throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-profile-page.fxml"));
        root = fxmlLoader.load();

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Logout selection at sellerNameMenuButton at seller centre
    public void logoutPressed(ActionEvent event) throws IOException {
        // Forward user to seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Add product button at seller centre
    public void addProductButtonPressed(ActionEvent event) throws IOException {
        // Forward user to seller add product page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-add-product-page.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Please add your product");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        resetScene();
    }


    @FXML
    // Customer review selection at menuButton at seller centre
    public void customerReviewPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-review-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // your performance selection at menuButton at seller centre
    public void yourPerformancePressed(ActionEvent event) throws IOException {
        // show the seller's performance
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-performance-template.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Your Performance");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void changeScene(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("seller-edit-page.fxml"));
        ProductController productController = new ProductController();

        stage = (Stage) ((Node) productController.event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillSellerProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(product);
    }

    public void resetScene() {
        // clear the contents in the grid
        grid.getChildren().clear();
        products.clear();

        getProduct();
        productListener = new ProductListener() {
            @Override
            public void onClickListener(Product product) throws IOException {
                changeScene(product);
            }
        };

        int column = 0;
        int row = 1;

        try {

            for (Product product : products) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("product-template.fxml"));
                AnchorPane anchorPane = loader.load();

                ProductController productController = loader.getController();
                // set product name, product price and product image
                // display it at seller product page
                productController.setData(product, productListener);


                if (column == 4) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(28));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

