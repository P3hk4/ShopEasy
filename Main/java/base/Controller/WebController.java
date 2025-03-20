package base.Controller;

import base.DataGenerationScrypts.DataGenerator;
import base.Entity.Client;
import base.Entity.Order;
import base.Entity.OrderProduct;
import base.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShippingService shippingService;


    @RequestMapping("/")
    public String homePage(Model model){
        return "homePage";
    }

    @RequestMapping("/index")
    public String index(Model model){
//        DataGenerator dataGenerator = new DataGenerator();
//        categoryService.saveAllCategories(dataGenerator.generateCategories());
//        clientService.saveAllClients(dataGenerator.generateClients());
//        manufacturerService.saveAllManufacturers(dataGenerator.generateManufacturers());
//        productService.saveAllProducts(dataGenerator.generateProducts(categoryService.getAllCategories(),manufacturerService.getAllManufacturers()));
//        shippingService.saveAllShippings(dataGenerator.generateShippings(clientService.getAllClientsId()));
//        orderService.saveAllOrders(dataGenerator.generateOrders(clientService.getAllClientsId(), shippingService.getAllShippingIds()));
//        orderProductService.saveAllOrderProducts(dataGenerator.generateOrderProducts(orderService.getAllOrderIdsWithZeroTotalCost(),productService.getAllProductIds()));


        model.addAttribute("attribute",null);
        return "index";
    }



}
