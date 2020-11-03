package main.java.entities;


public class Order {
    private int id;
    private int petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;

    public Order() {
    }

    public Order(int id, int petId, int quantity, String shipDate, String status, boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public long getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(int value) {
        this.petId = value;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int value) {
        this.quantity = value;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String value) {
        this.shipDate = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean value) {
        this.complete = value;
    }

    @Override
    public String toString() {
        return "Order: \n" +
                "id = " + this.id + "\n" +
                "PetID = " + this.petId + "\n" +
                "Quantity = " + this.quantity + "\n" +
                "ShipDate = " + this.shipDate + "\n" +
                "Status = " + this.status + "\n" +
                "Complete = " + this.complete + "\n";
    }
}


