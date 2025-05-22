package base.ControllerWeb;

import base.DataGenerationScrypt.DataGenerator;
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
        return "index";
    }

    @RequestMapping("/generate-data")
    public String index(){
        System.out.println("START GENERATION METHOD");
        DataGenerator dataGenerator = new DataGenerator();
        categoryService.saveAllCategories(dataGenerator.generateCategories());
        System.out.println("CATEGORIES GENERATED" + categoryService.getAllCategories());
        clientService.saveAllClients(dataGenerator.generateClients());
        System.out.println("CLIENTS GENERATED" + clientService.getAllClients());
        manufacturerService.saveAllManufacturers(dataGenerator.generateManufacturers());
        System.out.println("Manufacturers GENERATED" + manufacturerService.getAllManufacturers());
        productService.saveAllProducts(dataGenerator.generateProducts(categoryService.getAllCategories(),manufacturerService.getAllManufacturers()));
        System.out.println("PRODUCTS GENERATED" + productService.getAllProducts());
        shippingService.saveAllShippings(dataGenerator.generateShippings(clientService.getAllClientsIds()));
        System.out.println("SHIPPINGS GENERATED" + shippingService.getAllShippings());
        orderService.saveAllOrders(dataGenerator.generateOrders(clientService.getAllClientsIds(), shippingService.getAllShippingIds()));
        System.out.println("ORDERS GENERATED" + orderService.getAllOrders());
        orderProductService.saveAllOrderProducts(dataGenerator.generateOrderProducts(orderService.getAllOrderIdsWithZeroTotalCost(),productService.getAllProductIds()));
        System.out.println("ORDERPRODUCTS GENERATED" + orderProductService.getOrderProductsByOrderId(10));
        return "index";
    }

    @RequestMapping("/registration")
    public String registration(){
        return "registration";
    }

    @RequestMapping("/login")
    public String login(){return "login";}

    @RequestMapping("/loginafter")
    public String loginafter(){return "login";}

    @RequestMapping("/shop")
    public String shop(){return "shop";}

    @RequestMapping("/shop-single")
    public String shopsingle(){return "shop-single";}

    @RequestMapping("/basket")
    public String basket(){return "basket";}

    @RequestMapping("/account")
    public String account(){return "account";}
}
