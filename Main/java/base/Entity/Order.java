package base.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity(name = "Orders")
public class Order {

    @Id
    @Column(name = "Order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @Column(name = "Shipping_id")
    private int shippingId;

    @Column(name = "Client_id")
    private int clientId;

    @Column(name = "Total_Cost")
    private int totalCost;

    @Column(name = "Track_Number")
    private String trackNumber;

    @Column(name = "Date")
    private Date date;

    @OneToMany
    @JoinColumn(name = "Order_id")
    private List<OrderProduct> orderProducts;

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Order() {
    }

    public Order(int shippingId, int clientId, int totalCost, String trackNumber, Date date) {
        this.shippingId = shippingId;
        this.clientId = clientId;
        this.totalCost = totalCost;
        this.trackNumber = trackNumber;
        this.date = date;
    }

    public Order(int orderId, int shippingId, int clientId, int totalCost, String trackNumber, Date date) {
        this.orderId = orderId;
        this.shippingId = shippingId;
        this.clientId = clientId;
        this.totalCost = totalCost;
        this.trackNumber = trackNumber;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", shippingId=" + shippingId +
                ", clientId=" + clientId +
                ", totalCost=" + totalCost +
                ", trackNumber='" + trackNumber + '\'' +
                ", date=" + date +
                '}';
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
