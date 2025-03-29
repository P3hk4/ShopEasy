package base.Entity;


import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @Column(name = "Client_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int clientId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @OneToMany
    @JoinColumn(name = "Client_id")
    private List<Order> orders;

    @OneToMany
    @JoinColumn(name = "Client_id")
    private List<Shipping> shippings;

    public Client(int clientId, String username, String email, String firstName, String lastName) {
        this.clientId = clientId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(String username, String email, String password, String role, String firstName, String lastName) {
        this.username = username;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(int clientId, String username, String email, String password, String role, String firstName, String lastName) {
        this.clientId = clientId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
    }

    public Client() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
