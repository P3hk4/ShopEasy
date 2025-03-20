package base.Service;

import base.Entity.Order;
import base.Entity.OrderProduct;
import base.Repository.OrderProductRepository;
import base.Repository.OrderRepository;
import base.Repository.ProductRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderProductService(OrderProductRepository orderProductRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderProduct getOrderProductByOrderProductId(int id){
        return orderProductRepository.findOrderProductByOrderProductId(id);
    }

    public List<OrderProduct> getOrderProductsByOrderId(int id){
        return orderProductRepository.findOrderProductsByOrderId(id);
    }

    public List<OrderProduct> getOrderProductsByProductId(int id){
        return orderProductRepository.findOrderProductsByOrderId(id);
    }

    public List<OrderProduct> getOrderProductsByOrderIds(List<Integer> orderIds){
        return orderProductRepository.findOrderProductsByOrderIds(orderIds);
    }

    public void saveOrderProduct(OrderProduct orderProduct){
        try {
            orderProductRepository.save(orderProduct);
            Order order = orderRepository.findOrderByOrderId(orderProduct.getOrderId());
            order.setTotalCost(order.getTotalCost() + orderProduct.getCount() *
                    productRepository.findProductByProductId(orderProduct.getProductId()).getPrice());
            orderRepository.save(order);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void saveAllOrderProducts(List<OrderProduct> orderProducts){
        try {
            for (OrderProduct orderProduct : orderProducts) {
                saveOrderProduct(orderProduct);
            }
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void deleteOrderProduct(OrderProduct orderProduct){
        orderProductRepository.delete(orderProduct);
    }

    public void deleteAllOrderProducts(List<OrderProduct> orderProducts){
        orderProductRepository.deleteAll(orderProducts);
    }

    public void deleteOrderProductById(int id){
        OrderProduct orderProduct = getOrderProductByOrderProductId(id);
        Order order = orderRepository.findOrderByOrderId(orderProduct.getOrderId());
        order.setTotalCost(order.getTotalCost() - orderProduct.getCount() *
                productRepository.findProductByProductId(orderProduct.getProductId()).getPrice());
        orderRepository.save(order);
        deleteOrderProduct(orderProduct);
    }

}
