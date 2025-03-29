package base.DTO;

import base.Entity.Category;
import base.Entity.Client;
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
}
