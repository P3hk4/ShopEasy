package base.DTO;

import base.DTO.OrderProductRequest;

import java.util.List;

public class OrderRequest {
    private int shippingId;
    private int totalCost;
    private List<OrderProductRequest> products;

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public List<OrderProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductRequest> products) {
        this.products = products;
    }
}