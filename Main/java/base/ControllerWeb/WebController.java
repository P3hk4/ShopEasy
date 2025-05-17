package base.ControllerWeb;

import base.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
//        shippingService.saveAllShippings(dataGenerator.generateShippings(clientService.getAllClientsIds()));
//        orderService.saveAllOrders(dataGenerator.generateOrders(clientService.getAllClientsIds(), shippingService.getAllShippingIds()));
//        orderProductService.saveAllOrderProducts(dataGenerator.generateOrderProducts(orderService.getAllOrderIdsWithZeroTotalCost(),productService.getAllProductIds()));


        model.addAttribute("attribute",null);
        return "templatemo_559_zay_shop/index";
    }

    @RequestMapping("/registration")
    public String registration(){
        return "registration";
    }

    @RequestMapping("/test")
    public String test(){
        return "shop-single";
    }

}
