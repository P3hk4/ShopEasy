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
        return "index";
    }

    @RequestMapping("/test")
    public String index(){
//        DataGenerator dataGenerator = new DataGenerator();
//        categoryService.saveAllCategories(dataGenerator.generateCategories());
//        clientService.saveAllClients(dataGenerator.generateClients());
//        manufacturerService.saveAllManufacturers(dataGenerator.generateManufacturers());
//        productService.saveAllProducts(dataGenerator.generateProducts(categoryService.getAllCategories(),manufacturerService.getAllManufacturers()));
//        shippingService.saveAllShippings(dataGenerator.generateShippings(clientService.getAllClientsIds()));
//        orderService.saveAllOrders(dataGenerator.generateOrders(clientService.getAllClientsIds(), shippingService.getAllShippingIds()));
//        orderProductService.saveAllOrderProducts(dataGenerator.generateOrderProducts(orderService.getAllOrderIdsWithZeroTotalCost(),productService.getAllProductIds()));

        return "index";
    }

    @RequestMapping("/registration")
    public String registration(){
        return "registration";
    }

    @RequestMapping("/about")
    public String about(){
        return "about";
    }

    @RequestMapping("/contact")
    public String contact(){
        return "contact";
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
