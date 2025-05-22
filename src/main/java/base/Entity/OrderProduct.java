package base.Entity;

import jakarta.persistence.*;

@Entity(name = "Order_Product")
public class OrderProduct {

    @Id
    @Column(name = "Order_Product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderProductId;

    @Column(name = "Order_id")
    private int orderId;

    @Column(name = "Product_id")
    private int productId;

    @Column(name = "Count")
    private int count;

    public OrderProduct(int orderId, int productId, int count) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
    }

    public OrderProduct() {
    }

    public OrderProduct(int orderProductId, int orderId, int productId, int count) {
        this.orderProductId = orderProductId;
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "orderProductId=" + orderProductId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", count=" + count +
                '}';
    }

    public int getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(int orderProductId) {
        this.orderProductId = orderProductId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
