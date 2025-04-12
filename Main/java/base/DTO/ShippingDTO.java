package base.DTO;

public class ShippingDTO {
    private int shippingId;
    private int clientId;
    private String postCode;
    private String address;
    private String city;

    public ShippingDTO() {
    }

    public ShippingDTO(int shippingId, int clientId, String postCode, String address, String city) {
        this.shippingId = shippingId;
        this.clientId = clientId;
        this.postCode = postCode;
        this.address = address;
        this.city = city;
    }

    @Override
    public String toString() {
        return "ShippingDTO{" +
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
