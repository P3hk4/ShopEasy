package base.ControllerAPI;

import base.DTO.ClientDTO;
import base.DTO.OrderProductRequest;
import base.DTO.OrderRequest;
import base.DataGenerationScrypt.DataGenerator;
import base.Entity.Order;
import base.Entity.OrderProduct;
import base.Service.OrderProductService;
import base.Service.OrderService;
import base.Service.SecurityService.MyClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Order>> getAllOrders() {
        final List<Order> orders = orderService.getAllOrders();
        return orders != null && !orders.isEmpty()
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();

        }
        List<Order> orders = orderService.getOrdersByClientId(myClientDetails.getClient().getClientId());
        return orders != null
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> saveMyOrder(@RequestBody OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();
        }
        try {
            // Создаем заказ
            Order order = new Order();
            order.setShippingId(orderRequest.getShippingId());
            order.setClientId(myClientDetails.getClient().getClientId());
            order.setTotalCost(orderRequest.getTotalCost());
            order.setDate(new Date());
            order.setTrackNumber(DataGenerator.generateRandomString(12));
            orderService.saveOrder(order);
            // Создаем записи о товарах в заказе
            for (OrderProductRequest product : orderRequest.getProducts()) {
                OrderProduct orderProduct = new OrderProduct(
                        order.getOrderId(),
                        product.getProductId(),
                        product.getQuantity()
                );
                orderProductService.saveOrderProduct(orderProduct);
            }

            return ResponseEntity.ok("Заказ успешно создан. Номер заказа: " + order.getOrderId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании заказа: " + e.getMessage());
        }
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
