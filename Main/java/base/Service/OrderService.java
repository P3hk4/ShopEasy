package base.Service;

import base.Entity.Manufacturer;
import base.Entity.Order;
import base.Entity.OrderProduct;
import base.Repository.OrderProductRepository;
import base.Repository.OrderRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public Order getOrderByOrderId(int id){
        return orderRepository.findOrderByOrderId(id);
    }

    public List<Order> getOrdersByClientId(int clientId){
        return orderRepository.findOrdersByClientId(clientId);
    }

    public List<Order> getOrdersByShippingId(int shippingId){
        return orderRepository.findOrdersByShippingId(shippingId);
    }

    public List<Order> getOrdersByDate(Date date){
        return orderRepository.findOrdersByDate(date);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAllBy();
    }

    public void saveOrder(Order order){
        try {
            orderRepository.save(order);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void deleteOrderById(int id){
        clearOrderById(id);
        orderRepository.delete(getOrderByOrderId(id));
    }

    public void clearOrderById(int id){
        Order order = getOrderByOrderId(id);
        List<OrderProduct> orderProducts = orderProductRepository.findOrderProductsByOrderId(id);
        orderProductRepository.deleteAll(orderProducts);
        order.setTotalCost(0);
        orderRepository.save(order);
    }

    public List<Order> getAllOrdersWithZeroTotalCost(){
        return orderRepository.findOrdersWithZeroTotalCost();
    }

    public List<Integer> getAllOrderIdsWithZeroTotalCost(){
        return orderRepository.findOrderIdsWithZeroTotalCost();
    }

    public void saveAllOrders(List<Order> orders){
        try {
            orderRepository.saveAll(orders);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

}
