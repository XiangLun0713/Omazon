package com.example.omazonproject;

/**
 * This class is a blueprint for all ordered item, which will be used in the purchase page
 *
 * @author XiangLun
 */
public class OrderedItem {

    /**
     * An instance variable named productName with String data type
     */
    public String productName;

    /**
     * An instance variable named orderedImagePath with String data type
     */
    public String orderedImagePath;

    /**
     * An instance variable named quantity with int data type
     */
    public int quantity;

    /**
     * An instance variable named orderedImageName with String data type
     */
    public String orderedImageName;

    /**
     * A method to return productName in String
     */
    public String getProductName() {
        return productName;
    }

    /**
     * A method to receive a String parameter and set it as productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * A method to return orderedImagePath in String
     */
    public String getOrderedImagePath() {
        return orderedImagePath;
    }

    /**
     * A method to receive a String parameter and set it as orderedImagePath
     */
    public void setOrderedImagePath(String orderedImagePath) {
        this.orderedImagePath = orderedImagePath;
    }

    /**
     * A method to return quantity in int
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * A method to receive an int parameter and set it as quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * A method to return orderedImageName in String
     */
    public String getOrderedImageName() {
        return orderedImageName;
    }

    /**
     * A method to receive a String parameter and set it as orderedImageName
     */
    public void setOrderedImageName(String orderedImageName) {
        this.orderedImageName = orderedImageName;
    }
}
