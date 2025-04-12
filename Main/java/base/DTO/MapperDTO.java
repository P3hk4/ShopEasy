package base.DTO;

import base.Entity.*;
import org.springframework.stereotype.Service;

@Service
public class MapperDTO {

    public ClientDTO entityToDTO(Client client) {
        return new ClientDTO(client.getClientId(),client.getUsername(),client.getEmail(),client.getFirstName(),client.getLastName());
    }

    public Client dtoToEntity(ClientDTO clientDTO) {
        return new Client(clientDTO.getId(),clientDTO.getUsername(),clientDTO.getEmail(), clientDTO.getFirstName(),clientDTO.getLastName());
    }

    public CategoryDTO entityToDTO(Category category) {
        return new CategoryDTO(category.getCategoryId(),category.getName());
    }

    public Category dtoToEntity(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getId(),categoryDTO.getName());
    }

    public ProductDTO entityToDTO(Product product) {
        return new ProductDTO(product.getProductId(),product.getCategoryId(),product.getManufacturerId(),product.getName(),product.getDescription(),product.getPrice());}

    public Product dtoToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getProductId(),productDTO.getCategoryId(),productDTO.getManufacturerId(),productDTO.getName(),productDTO.getDescription(),productDTO.getPrice());
    }

    public ManufacturerDTO entityToDTO(Manufacturer manufacturer) {
        return new ManufacturerDTO(manufacturer.getManufacturerId(),manufacturer.getCountry(),manufacturer.getName());
    }

    public Manufacturer dtoToEntity(ManufacturerDTO manufacturerDTO) {
        return new Manufacturer(manufacturerDTO.getManufacturerId(),manufacturerDTO.getCountry(),manufacturerDTO.getName());
    }

    public ShippingDTO entityToDTO(Shipping shipping){
        return new ShippingDTO(shipping.getShippingId(),shipping.getClientId(),shipping.getPostCode(),shipping.getAddress(),shipping.getCity());
    }

    public Shipping dtoToEntity(ShippingDTO shippingDTO) {
        return new Shipping(shippingDTO.getClientId(), shippingDTO.getClientId(), shippingDTO.getPostCode(), shippingDTO.getAddress(), shippingDTO.getCity());
    }
}