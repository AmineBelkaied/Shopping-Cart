package com.example.shoppingcart;

import com.example.shoppingcart.entities.*;
import com.example.shoppingcart.enums.Status;
import com.example.shoppingcart.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ShoppingCartApplication implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final PacketService packetService;
    private final PacketItemService packetItemService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartApplication.class);

    public ShoppingCartApplication(CategoryService categoryService, ProductService productService, CustomerService customerService, PacketService packetService, PacketItemService packetItemService, PacketItemService packetItemService1) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.customerService = customerService;
        this.packetService = packetService;
        this.packetItemService = packetItemService1;
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        Customer customer = initCustomer();
        initProducts();

        // Add the first packet
        LOGGER.info("FIRST PACKET PREPARATION ... ");
        Packet firstPacket = prepareAndValidatePacket(customer);
        LOGGER.info("The first packet is validated : {}" , firstPacket.getStatus());
        List<PacketItem> firstPacketItems = packetItemService.getAllPacketItemsByPacketId(firstPacket.getId());
        firstPacketItems.stream()
                .map(PacketItem::getProduct)
                .forEach(product -> LOGGER.info("After the validation of the first packet - the product {} quantity is {} ", product.getName(), product.getAvailableStock()));


        // Add a second packet to the same customer
        LOGGER.info("SECOND PACKET PREPARATION ... ");
        Packet secondPacket = prepareAndValidatePacket(customer);
        LOGGER.info("The second packet is validated : {}" , secondPacket.getStatus());
        List<PacketItem> secondPacketItems = packetItemService.getAllPacketItemsByPacketId(firstPacket.getId());
        secondPacketItems.stream()
                .map(PacketItem::getProduct)
                .forEach(product -> LOGGER.info("After the validation of the second packet - the product {} quantity is {} ", product.getName(), product.getAvailableStock()));
        // Cancel the second packet
        packetService.cancelPacket(secondPacket, 1000L);
        LOGGER.info("The second packet has been canceled : {}" , secondPacket.getStatus());
        packetItemService.getAllPacketItemsByPacketId(secondPacket.getId()).stream()
                .map(PacketItem::getProduct)
                .forEach(product -> LOGGER.info("After the cancelation of the second packet - the product {} quantity is {} ", product.getName(), product.getAvailableStock()));


        // Add a third packet to the same customer
        LOGGER.info("THIRD PACKET PREPARATION ... ");
        Packet thirdPacket = prepareAndValidatePacket(customer);
        LOGGER.info("The third packet is validated : {}" , thirdPacket.getStatus());
        List<PacketItem> thirdPacketItems = packetItemService.getAllPacketItemsByPacketId(firstPacket.getId());
        thirdPacketItems.stream()
                .map(PacketItem::getProduct)
                .forEach(product -> LOGGER.info("After the validation of the third packet - the product {} quantity is {} ", product.getName(), product.getAvailableStock()));
        // Cancel the third packet
        try {
            packetService.cancelPacket(thirdPacket, 0L);
        } catch (Exception e) {
            LOGGER.error("Failed to cancel packet with ID: {}", thirdPacket.getId(), e);
        }
        LOGGER.info("The third packet has been canceled : {}" , thirdPacket.getStatus());
        packetItemService.getAllPacketItemsByPacketId(thirdPacket.getId()).stream()
                .map(PacketItem::getProduct)
                .forEach(product -> LOGGER.info("After the cancelation of the third packet - the product {} quantity is {} ", product.getName(), product.getAvailableStock()));

        List<Packet> packets = packetService.getPacketsByCustomerId(customer.getId());
        LOGGER.info("This is the list associated to the customer : {}" , customer.getName());
        packets.forEach(packet -> LOGGER.info("Packet ID: {}, Status: {}", packet.getId(), packet.getStatus()));
    }

    private Packet prepareAndValidatePacket(Customer customer) throws Exception {
        Packet packet = initPacket(customer);
        LOGGER.info("--- ADDING PRODUCTS TO THE PACKET ---");
        addProductsToPacket(packet);
        packetService.validatePacket(packet);
        return packet;
    }

    private void addProductsToPacket(Packet packet) throws Exception {
        LOGGER.info("Retrieving the list of available products ...");
        List<Product> products = productService.getAvailableProducts();

        if(!products.isEmpty()) {
            Product freshFit = products.getFirst();
            packetService.addProductToShoppingCart(packet, freshFit, 3);
            LOGGER.info("The product >> {} has been added to the shopping cart.", freshFit.getName());
            Product wearWave = products.get(1);
            packetService.addProductToShoppingCart(packet, wearWave, 1);
            LOGGER.info("The product >> {} has been added to the shopping cart.", wearWave.getName());
            Product flexFit = products.get(2);
            packetService.addProductToShoppingCart(packet, flexFit, 2);
            LOGGER.info("The product >> {} has been added to the shopping cart.", flexFit.getName());
            Product jean = products.get(3);
            packetService.addProductToShoppingCart(packet, jean, 4);
            LOGGER.info("The product >> {} has been added to the shopping cart.", jean.getName());


            packetService.deleteProductFromShoppingCart(packet.getId(), wearWave);
            LOGGER.info("The product >> {} has been deleted to the shopping cart.", wearWave.getName());
            packetService.deleteProductFromShoppingCart(packet.getId(), flexFit);
            LOGGER.info("The product >> {} has been deleted to the shopping cart.", flexFit.getName());
        }

    }

    private Customer initCustomer() {
        Customer customer = new Customer();
        customer.setName("Paul Bk");
        customer.setAddress("Paris, France");
        customer.setPhoneNumber("+33 6 12 34 56 78");
        customer.setEmail("paulbk@gmail.com");
        return customerService.createCustomer(customer);
    }

    private Packet initPacket(Customer customer) {
        Packet packet = new Packet();
        packet.setCreationDate(new Date());
        packet.setStatus(Status.IN_PROGRESS); // The packet is not confirmed yet
        packet.setCustomer(customer);
        return packetService.createPacket(packet);
    }


    private void initProducts() throws Exception {
        // T-shirt Category
        Category category1 = new Category();
        category1.setName("Shirt");
        category1.setDescription("T-shirt Category");

        // Pant Category
        Category category2 = new Category();
        category2.setName("Pant");
        category2.setDescription("Pant Category");

        Category shirtCategory = categoryService.createCategory(category1);
        Category pantCategory = categoryService.createCategory(category2);

        // FreshFit shirt
        Product product1 = new Product();
        product1.setName("FreshFit");
        product1.setDescription("Fresh Fit Product");
        product1.setCategory(shirtCategory);
        product1.setReference("FRT001");
        product1.setAvailableStock(20);
        product1.setTemporaryStock(20);
        product1.setPrice(70.0);

        // WearWave shirt
        Product product2 = new Product();
        product2.setName("WearWave");
        product2.setDescription("WearWave Product");
        product2.setCategory(shirtCategory);
        product2.setReference("WW001");
        product2.setAvailableStock(15);
        product2.setTemporaryStock(15);
        product2.setPrice(95.0);

        // FlexFit pant
        Product product3 = new Product();
        product3.setName("FlexFit");
        product3.setDescription("FlexFit Product");
        product3.setCategory(pantCategory);
        product3.setReference("FT001");
        product3.setAvailableStock(12);
        product3.setTemporaryStock(12);
        product3.setPrice(120.0);

        // Jean pant
        Product product4 = new Product();
        product4.setName("Jean");
        product4.setDescription("Jean Product");
        product4.setCategory(pantCategory);
        product4.setReference("JN001");
        product4.setAvailableStock(42);
        product4.setTemporaryStock(42);
        product4.setPrice(80.0);

        productService.createProduct(product1);
        productService.createProduct(product2);
        productService.createProduct(product3);
        productService.createProduct(product4);
    }

}
