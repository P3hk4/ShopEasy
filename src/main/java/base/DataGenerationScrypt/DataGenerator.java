package base.DataGenerationScrypt;

import base.Entity.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static base.DataGenerationScrypt.StringsReaderAndWriter.readStringsFromFile;

public class DataGenerator {

    public ArrayList<Category> generateCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<String> strings = readStringsFromFile("Main\\resources\\Data\\Categories.txt");
        for (int i = 0; i < (strings != null ? strings.size() : 0); ++i){
            categories.add(new Category(strings.get(i)));
        }
        return categories;
    }

    public ArrayList<Manufacturer> generateManufacturers(){
        ArrayList<Manufacturer> manufacturers = new ArrayList<>();
        ArrayList<String> strings = readStringsFromFile("Main\\resources\\Data\\Manufacturers.txt");
        List<List<String>> twoStrings = StringsSplitter.splitStrings(strings);
        for (int i = 0; i < (twoStrings != null ? twoStrings.get(0).size() : 0); ++i ){
        manufacturers.add(new Manufacturer(twoStrings.get(0).get(i),twoStrings.get(1).get(i)));
        }
        return manufacturers;
    }

    public ArrayList<Client> generateClients(){
        ArrayList<Client> clients = new ArrayList<>();
        ArrayList<String> emails = readStringsFromFile("Main\\resources\\Data\\Emails.txt");
        ArrayList<String> maleNames = readStringsFromFile("Main\\resources\\Data\\MaleNames.txt");
        ArrayList<String> femaleNames = readStringsFromFile("Main\\resources\\Data\\FemaleNames.txt");
        ArrayList<String> maleLastNames = readStringsFromFile("Main\\resources\\Data\\MaleLastnames.txt");
        ArrayList<String> femaleLastNames = readStringsFromFile("Main\\resources\\Data\\FemaleLastnames.txt");
        for (int i = 0; i < (emails != null ? emails.size() : 0); ++i){
            String username = emails.get(i).split("@")[0];
            if ((int) Math.round(Math.random()) == 1) {
                clients.add(new Client(username,emails.get(i),username,"ROLE_USER",femaleNames.get((int)(Math.random() * (100))),femaleLastNames.get((int)(Math.random() * (100)))));
            } else {
                clients.add(new Client(username,emails.get(i),username,"ROLE_USER",maleNames.get((int)(Math.random() * (100))),maleLastNames.get((int)(Math.random() * (100)))));
            }
        }
    return clients;
    }

    public ArrayList<Product> generateProducts(List<Category> categories, List<Manufacturer> manufacturers){
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 5000 ; ++i){
            Category tempCategory = categories.get((int)(Math.random() * (categories.size())));
            Manufacturer tempManufacturer = manufacturers.get((int)(Math.random() * (manufacturers.size())));
            products.add(new Product(
                    tempCategory.getCategoryId(),
                    tempManufacturer.getManufacturerId(),
                    tempCategory.getName()+" от компании " + tempManufacturer.getName(),
                    "Description",
                    (int)(Math.random() * (50000))
                    ));
        }
        return products;
    }

    public ArrayList<Shipping> generateShippings(List<Integer> clientIds){
        ArrayList<Shipping> shippings = new ArrayList<>();
        ArrayList<String> cities = readStringsFromFile("Main\\resources\\Data\\Cities.txt");
        ArrayList<String> addresses = readStringsFromFile("Main\\resources\\Data\\Addresses.txt");
        for (int i = 0; i < 2000; ++i){
            shippings.add(new Shipping(
                    clientIds.get((int)(Math.random() * (clientIds.size()))),
                    Integer.toString((int)(100000 + Math.random() * (899999))),
                    addresses.get((int)(Math.random() * (addresses.size())))+" д. "+(int)(Math.random() * (200)),
                    cities.get((int)(Math.random() * (cities.size())))
            ));
        }
        return shippings;
    }

    public ArrayList<Order> generateOrders(List<Integer> clientIds, List<Integer> shippingIds){
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < 2000; ++i){
            orders.add(new Order(
                    shippingIds.get((int)(Math.random() * (shippingIds.size()))),
                    clientIds.get((int)(Math.random() * (clientIds.size()))),
                    0,
                    generateRandomString(12),
                    Date.from(generateRandomDate(LocalDate.of(2020, 1, 1),LocalDate.of(2025, 2, 28)).atStartOfDay(ZoneId.systemDefault()).toInstant())
            ));
        }
        return orders;
    }

    public ArrayList<OrderProduct> generateOrderProducts(List<Integer> orderIds, List<Integer> productIds){
        ArrayList<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < orderIds.size(); ++i){
            for (int j = 0; j < (int)( 1 + Math.random() * (10)); ++j){
                orderProducts.add(new OrderProduct(
                        orderIds.get(i),
                        productIds.get((int)(Math.random() * (productIds.size()))),
                        (int)( 1 + Math.random() * (3) )
                ));
            }
        }
        return orderProducts;
    }

    public static LocalDate generateRandomDate(LocalDate start, LocalDate end) {
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        Random random = new Random();
        long randomDays = random.nextInt((int) daysBetween + 1);
        return start.plusDays(randomDays);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }



}
