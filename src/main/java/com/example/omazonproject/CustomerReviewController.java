package com.example.omazonproject;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This class is responsible to control the events happening in the customer review page
 *
 * @author XiangLun
 */
public class CustomerReviewController {
    @FXML
    private Label percentageLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        // forward the user back to the seller centre
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        // create a list to store sales data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // create an array to store the number of ratings for the bar chart
        int[] ratings = new int[5];

        // create a series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // connect to the database and retrieve all the seller's products
        Connection connection = null;
        PreparedStatement psGetProducts = null;
        ResultSet resultset = null;

        int totalNumOfSales = 0;
        try {
            DatabaseConnection db = new DatabaseConnection();
            connection = db.getConnection();
            psGetProducts = connection.prepareStatement("SELECT * FROM product_info WHERE sellerEmail = ?");
            psGetProducts.setString(1, Seller.getEmail());
            resultset = psGetProducts.executeQuery();

            // read the data from the result set and add the data into the list
            if (resultset.isBeforeFirst()) {
                while (resultset.next()) {
                    pieChartData.add(new PieChart.Data(resultset.getString("name"), resultset.getInt("numberOfSales")));
                    totalNumOfSales += resultset.getInt("numberOfSales");
                }
            } else {
                System.out.println("No products");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psGetProducts != null) {
                try {
                    psGetProducts.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // display the pie chart in a fade-in and slide-up animation
        pieChart.setTitle("Sales");
        pieChart.setData(pieChartData);
        TranslateTransition translate = new TranslateTransition(Duration.seconds(.8), pieChart);
        translate.setFromY(pieChart.getLayoutY() + 10);
        translate.setToY(pieChart.getLayoutY());
        translate.play();
        FadeTransition fadeInPieChart = new FadeTransition(Duration.seconds(.8), pieChart);
        fadeInPieChart.setFromValue(0);
        fadeInPieChart.setToValue(1);
        fadeInPieChart.play();

        // set up the property for the label
        percentageLabel.setTextFill(Color.BLACK);
        percentageLabel.setStyle("-fx-font: 10 arial;");

        // set event handler for every segment of the pie chart
        for (final PieChart.Data data : pieChart.getData()) {
            int finalTotalNumOfSales = totalNumOfSales;
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                // if segment clicked,
                // locate the percentage label on mouse clicked
                percentageLabel.setTranslateX(event.getSceneX() - percentageLabel.getLayoutX());
                percentageLabel.setTranslateY(event.getSceneY() - percentageLabel.getLayoutY());

                // set up the percentage value
                percentageLabel.setText(String.format("%.2f%%", data.getPieValue() * 100 / finalTotalNumOfSales));

                // display the label in a fade in transition
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(.6), percentageLabel);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();

                // retrieve the rating of that particular product
                Connection connection1 = null;
                PreparedStatement psGetRating = null;
                ResultSet resultSet = null;

                try {
                    // search for the specific product in the database
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    connection1 = databaseConnection.getConnection();
                    psGetRating = connection1.prepareStatement("SELECT * FROM product_info WHERE name = ? AND sellerEmail = ?");
                    psGetRating.setString(1, data.getName());
                    psGetRating.setString(2, Seller.getEmail());
                    resultSet = psGetRating.executeQuery();

                    if (resultSet.next()) {
                        // store the product's ratings in the ratings array
                        ratings[0] = Integer.parseInt(resultSet.getString("numOfOneStars"));
                        ratings[1] = Integer.parseInt(resultSet.getString("numOfTwoStars"));
                        ratings[2] = Integer.parseInt(resultSet.getString("numOfThreeStars"));
                        ratings[3] = Integer.parseInt(resultSet.getString("numOfFourStars"));
                        ratings[4] = Integer.parseInt(resultSet.getString("numOfFiveStars"));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally{
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (psGetRating != null) {
                        try {
                            psGetRating.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection1 != null) {
                        try {
                            connection1.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // update the bar graph
                updateBarChart(ratings);
            });
        }
    }

    /**
     * This method is used to update the bar chart whenever it is called
     * @param ratings an array which stores the number of ratings for each category
     *                index 0 -> one-star rating
     *                index 1 -> two-star rating
     *                index 2 -> three-star rating
     *                index 3 -> four-star rating
     *                index 4 -> five-star rating
     *
     * @author XiangLun
     */
    private void updateBarChart(int[] ratings){
        // clear the data displayed before
        barChart.getData().clear();
        barChart.layout();

        // create a series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // pass-in the data
        series.getData().add(new XYChart.Data<>("1", ratings[0]));
        series.getData().add(new XYChart.Data<>("2", ratings[1]));
        series.getData().add(new XYChart.Data<>("3", ratings[2]));
        series.getData().add(new XYChart.Data<>("4", ratings[3]));
        series.getData().add(new XYChart.Data<>("5", ratings[4]));

        // update the bar chart
        barChart.getData().add(series);
    }
}