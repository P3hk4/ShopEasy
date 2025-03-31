package base.DTO;

public class ManufacturerDTO {

    private int manufacturerId;
    private String country;
    private String name;

    public ManufacturerDTO(int manufacturerId, String country, String name) {
        this.manufacturerId = manufacturerId;
        this.country = country;
        this.name = name;
    }

    public ManufacturerDTO() {
    }

    @Override
    public String toString() {
        return "ManufacturerDTO{" +
                "manufacturerId=" + manufacturerId +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
