package base.ControllerAPI;

import base.Entity.Order;
import base.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> orders = orderService.getAllOrders();
        return orders != null && !orders.isEmpty()
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Order> getOrderById(@PathVariable(name = "id") int id) {
        Order order = orderService.getOrderByOrderId(id);
        return order != null
                ? new ResponseEntity<>(order, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clients/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByClientsId(@PathVariable(name = "id") int id) {
        List<Order> orders = orderService.getOrdersByClientId(id);
        return orders != null
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/shippings/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByShippingsId(@PathVariable(name = "id") int id) {
        List<Order> orders = orderService.getOrdersByShippingId(id);
        return orders != null
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> save(@RequestBody Order order) {
        orderService.saveOrder(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrderById(@PathVariable(name = "id") int id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Заказ успено удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении заказа: " + e.getMessage());
        }
    }

}
