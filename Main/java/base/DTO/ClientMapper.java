package base.DTO;

import base.Entity.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientMapper {

    public ClientDTO toDTO(Client client) {
        return new ClientDTO(client.getClientId(),client.getUsername(),client.getEmail(),client.getFirstName(),client.getLastName());
    }

    public Client toEntity(ClientDTO clientDTO) {
        return new Client(clientDTO.getId(),clientDTO.getUsername(),clientDTO.getEmail(), clientDTO.getFirstName(),clientDTO.getLastName());
    }
}
