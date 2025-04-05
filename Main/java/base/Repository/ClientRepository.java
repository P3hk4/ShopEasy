package base.Repository;

import base.DTO.ClientDTO;
import base.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT new base.DTO.ClientDTO(c.clientId, c.username, c.email, c.firstName, c.lastName) FROM Client c")
    List<ClientDTO> findAllClientDTO();

    @Query("SELECT new base.DTO.ClientDTO(c.clientId, c.username, c.email, c.firstName, c.lastName) FROM Client c WHERE c.clientId = :id")
    ClientDTO findClientDTOById(@Param("id")int id);

    @Query("SELECT new base.DTO.ClientDTO(c.clientId, c.username, c.email, c.firstName, c.lastName) FROM Client c WHERE c.username = :username")
    ClientDTO findClientDTOByUsername(@Param("username")String username);

    @Query("SELECT new base.DTO.ClientDTO(c.clientId, c.username, c.email, c.firstName, c.lastName) FROM Client c WHERE c.email = :email")
    ClientDTO findClientDTOByEmail(@Param("email")String email);
}
