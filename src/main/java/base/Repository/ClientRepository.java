package base.Repository;

import base.DTO.ClientDTO;
import base.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByEmail(String email);

    Client findClientByUsername(String username);

    Client findClientByClientId(int id);

    Optional<Client> getSecurityClientByUsername(String username);

    List<Client> findAllClientsBy();

    @Query("SELECT c.clientId FROM Client c")
    List<Integer> findAllClientIds();

}
