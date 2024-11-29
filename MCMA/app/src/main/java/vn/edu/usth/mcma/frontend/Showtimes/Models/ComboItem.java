package vn.edu.usth.mcma.frontend.Showtimes.Models;

public class ComboItem {
    private String name;
    private String imageUrl;
    private double price;
    private int quantity;

    public ComboItem(String name, String imageUrl, double price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = 0;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}