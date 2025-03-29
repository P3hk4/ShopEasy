package base.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Shipping")
public class Shipping {

    @Id
    @Column(name = "Shipping_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int shippingId;

    @Column(name = "Client_id")
    private int clientId;

    @Column(name = "Post_Code")
    private String postCode;

    @Column(name = "Address")
    private String address;

    @Column(name = "City")
    private String city;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Shipping_id")
    private List<Order> orders;


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Shipping() {
    }

    public Shipping(int clientId, String postCode, String address, String city) {
        this.clientId = clientId;
        this.postCode = postCode;
        this.address = address;
        this.city = city;
    }

    public Shipping(int shippingId, int clientId, String postCode, String address, String city) {
        this.shippingId = shippingId;
        this.clientId = clientId;
        this.postCode = postCode;
        this.address = address;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Shipping{" +
                "shippingId=" + shippingId +
                ", clientId=" + clientId +
                ", postCode='" + postCode + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
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

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
