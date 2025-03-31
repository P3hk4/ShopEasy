package base.DTO;

public class ProductDTO {

    private int productId;
    private int categoryId;
    private int manufacturerId;
    private String name;
    private String description;
    private int price;

    public ProductDTO(int productId, int categoryId, int manufacturerId, String name, String description, int price) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ProductDTO() {
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", manufacturerId=" + manufacturerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
