package base.Service;

import base.Repository.ClientRepository;
import base.Entity.Client;
import org.hibernate.StaleObjectStateException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public Client getClientByEmail(String email){
        return clientRepository.findClientByEmail(email);
    }

    public void updateClient(Client client){
        Client oldClient = clientRepository.findClientByClientId(client.getClientId());
        if (client.getFirstName() != null) oldClient.setFirstName(client.getFirstName());
        if (client.getLastName() != null) oldClient.setLastName(client.getLastName());
        if (client.getEmail() != null) oldClient.setEmail(client.getEmail());
        if (client.getPassword() != null) oldClient.setPassword(passwordEncoder.encode(client.getPassword()));
        if (client.getUsername() != null) oldClient.setUsername(client.getUsername());
        if (client.getRole() != null) oldClient.setRole(client.getRole());
        clientRepository.save(oldClient);
    }

    public void saveClient(Client client){
        try {
            String HashPassword =  passwordEncoder.encode(client.getPassword());
            client.setPassword(HashPassword);
            clientRepository.save(client);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void saveAllClients(List<Client> clients){
        for (Client client : clients) {
            String HashPassword =  passwordEncoder.encode(client.getPassword());
            client.setPassword(HashPassword);
        }
        clientRepository.saveAll(clients);
    }

    public Client getClientById(int id){
        return clientRepository.findClientByClientId(id);
    }

    public Client getClientByUsername(String username){
        return clientRepository.findClientByUsername(username);
    }

    public List<Client> getAllClients(){
        return clientRepository.findAllClientsBy();
    }

    public List<Integer> getAllClientsIds(){
        return clientRepository.findAllClientIds();
    }

    public void deleteClientById(int id){
        clientRepository.delete(getClientById(id));
    }

}
