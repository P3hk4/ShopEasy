package base.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Manufacturers")
public class Manufacturer{

    @Id
    @Column(name = "Manufacturer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int manufacturerId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Country")
    private String country;

    @OneToMany
    @JoinColumn(name = "Product_id")
    private List<Product> products;

    public Manufacturer() {
    }

    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Manufacturer(int manufacturerId, String name, String country) {
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "manufacturerId=" + manufacturerId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
