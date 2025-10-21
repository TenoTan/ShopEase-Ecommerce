package com.example.ecommerce.controller;

import com.example.ecommerce.model.*;
import com.example.ecommerce.service.*;
import com.example.ecommerce.dto.OrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping({"/", "/ecom.html"})
    public String home() {
        return "ecom";
    }

    @GetMapping("/Aboutus.html")
    public String aboutUs() {
        return "Aboutus";
    }

    @GetMapping("/aboutuslogin.html")
    public String aboutUsLogin() {
        return "aboutuslogin";
    }

    @GetMapping("/customer.html")
    public String customerSignup(Model model) {
        return "customer";
    }

    @GetMapping("/customerslogin.html")
    public String customerLogin() {
        return "customerslogin";
    }

    @GetMapping("/seller.html")
    public String sellerSignup() {
        return "seller";
    }

    @GetMapping({"/fashion.html", "/beauty.html", "/phones.html", "/books.html", "/shoes.html", "/furniture.html", "/toys.html", "/appliances.html"})
    public String redirectToLoginFromCategories() {
        return "redirect:/customerslogin.html";
    }

    @GetMapping("/postlogin.html")
    public String postLogin() {
        return "postlogin";
    }

    @GetMapping("/sellerlogin.html")
    public String sellerLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials. Please try again.");
        }
        return "sellerlogin";
    }

    @GetMapping("/adminlogin.html")
    public String adminLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid Admin ID or password");
        }
        return "adminlogin";
    }

    @GetMapping("/product.html")
    public String product(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) String priceRange,
            Model model
    ) {
        List<Product> products;

        if (categoryId != null) {
            products = productService.getProductsByCategoryId(categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProductsByKeyword(keyword);
        } else {
            products = productService.getAllProducts();
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            String[] range = priceRange.split("-");
            if (range.length == 2) {
                try {
                    BigDecimal minPrice = new BigDecimal(range[0]);
                    BigDecimal maxPrice = new BigDecimal(range[1]);
                    products = products.stream()
                            .filter(p -> p.getPrice() != null &&
                                    p.getPrice().compareTo(minPrice) >= 0 &&
                                    p.getPrice().compareTo(maxPrice) <= 0)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price range format: " + priceRange);
                }
            }
        }

        Map<Long, ProductRatingInfo> productRatings = new HashMap<>();
        for (Product product : products) {
            Double avgRating = ratingService.getAverageRatingForProduct(product.getProductId());
            int ratingValue = avgRating != null ? (int) Math.round(avgRating) : 0;
            int ratingCount = ratingService.getRatingsByProductId(product.getProductId()).size();
            productRatings.put(product.getProductId(), new ProductRatingInfo(ratingValue, ratingCount));
        }

        if (minRating != null) {
            products = products.stream()
                    .filter(p -> {
                        ProductRatingInfo ratingInfo = productRatings.get(p.getProductId());
                        return ratingInfo != null && ratingInfo.getAvgRating() >= minRating;
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("products", products);
        model.addAttribute("productRatings", productRatings);
        model.addAttribute("categories", categoryService.getAllCategories());

        if (categoryId != null) model.addAttribute("selectedCategoryId", categoryId);
        if (minRating != null) model.addAttribute("minRating", minRating);
        if (priceRange != null) model.addAttribute("priceRange", priceRange);
        if (keyword != null) model.addAttribute("keyword", keyword);

        return "product";
    }

    private static class ProductRatingInfo {
        private final int avgRating;
        private final int count;

        public ProductRatingInfo(int avgRating, int count) {
            this.avgRating = avgRating;
            this.count = count;
        }

        public int getAvgRating() {
            return avgRating;
        }

        public int getCount() {
            return count;
        }
    }

    @GetMapping("/cart.html")
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Customer> customerOpt = customerService.getCustomerByEmail(email);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Optional<Cart> cartOpt = cartService.getCartByCustomerId(customer.getId());

            Cart cart;
            if (cartOpt.isEmpty()) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart = cartService.saveCart(cart);
            } else {
                cart = cartOpt.get();
            }

            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());
            BigDecimal totalPrice = cartService.calculateCartTotal(cart.getCartId());
            if (totalPrice == null) {
                totalPrice = BigDecimal.ZERO;
            }

            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
        } else {
            model.addAttribute("error", "Customer not found. Please log in again.");
        }

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            logger.info("Adding product ID {} to cart", productId);

            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Optional<Cart> cartOpt = cartService.getCartByCustomerId(customer.getId());
            Cart cart;
            if (cartOpt.isEmpty()) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart = cartService.saveCart(cart);
            } else {
                cart = cartOpt.get();
            }

            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());
            boolean productExists = false;

            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId().equals(productId)) {
                    int newQuantity = item.getQuantity() + 1;
                    BigDecimal newTotalPrice = product.getPrice().multiply(new BigDecimal(newQuantity));

                    CartItem updatedItem = new CartItem();
                    updatedItem.setCart(cart);
                    updatedItem.setProduct(product);
                    updatedItem.setQuantity(newQuantity);
                    updatedItem.setTotalPrice(newTotalPrice);

                    cartItemService.saveCartItem(updatedItem);
                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(1);
                newItem.setTotalPrice(product.getPrice());
                cartItemService.saveCartItem(newItem);
            }

            redirectAttributes.addFlashAttribute("success", "Product added to cart successfully!");
            return "redirect:/cart.html";

        } catch (Exception e) {
            logger.error("Error adding product to cart: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to add product to cart: " + e.getMessage());
            return "redirect:/productdetail.html?id=" + productId;
        }
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Cart cart = cartService.getCartByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            cartService.removeItemFromCart(cart.getCartId(), productId);

            redirectAttributes.addFlashAttribute("success", "Item removed from cart successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove item from cart: " + e.getMessage());
        }
        return "redirect:/cart.html";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long productId,
                             @RequestParam Integer quantity,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Cart cart = cartService.getCartByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId().equals(productId)) {
                    if (quantity <= 0) {
                        cartService.removeItemFromCart(cart.getCartId(), productId);
                    } else {
                        item.setQuantity(quantity);
                        item.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
                        cartItemService.saveCartItem(item);
                    }
                    break;
                }
            }

            redirectAttributes.addFlashAttribute("success", "Cart updated successfully!");
        } catch (Exception e) {
            logger.error("Error updating cart: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to update cart: " + e.getMessage());
        }

        return "redirect:/cart.html";
    }

    @GetMapping("/fashion2.html")
    public String fashion() {
        return "fashion";
    }

    @GetMapping("/seller/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "selleradd";
    }

    @PostMapping("/seller/products/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam Long categoryId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            product.setSeller(seller);

            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            productService.saveProduct(product);

            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
            return "redirect:/sellerhomepage.html";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add product: " + e.getMessage());
            return "redirect:/seller/products/add";
        }
    }

    @GetMapping("/seller/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, Authentication authentication) {
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You don't have permission to edit this product");
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "selleredit";
    }

    @PostMapping("/seller/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!existingProduct.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("You don't have permission to edit this product");
            }

            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStockQuantity(product.getStockQuantity());
            existingProduct.setImageUrl(product.getImageUrl());

            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);

            productService.saveProduct(existingProduct);

            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
            return "redirect:/sellerhomepage.html";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
            return "redirect:/seller/products/edit/" + id;
        }
    }

    @PostMapping("/seller/updateProduct")
    public String updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("imageUrl") String imageUrl,
            RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);

            productService.saveProduct(product);

            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
            return "redirect:/sellerhomepage.html";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
            return "redirect:/seller/editProduct?productId=" + productId;
        }
    }

    @GetMapping("/seller/orders")
    public String viewSellerOrders(Model model, Authentication authentication) {
        String sellerEmail = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        List<OrderItem> orderItems = orderItemService.findOrderItemsBySellerId(seller.getId());
        model.addAttribute("orderItems", orderItems);
        return "sellerorder";
    }

    @PostMapping("/seller/products/delete")
    public String deleteProduct(@RequestParam Long productId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!product.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("You don't have permission to delete this product");
            }

            productService.deleteProduct(productId);

            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete product: " + e.getMessage());
        }

        return "redirect:/sellerhomepage.html";
    }

    @GetMapping("/seller/analytics")
    public String sellerAnalytics(Model model, Authentication authentication) {
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        model.addAttribute("seller", seller);

        return "selleranalytics";
    }

    @GetMapping("/seller/sales")
    public String sellerSales(Model model, Authentication authentication) {
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        model.addAttribute("seller", seller);

        return "sellersales";
    }

    @GetMapping("/productdetail.html")
    public String productDetail(@RequestParam Long id, Model model, Authentication authentication) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            model.addAttribute("product", product);

            Double avgRating = ratingService.getAverageRatingForProduct(id);
            int averageRating = avgRating != null ? (int) Math.round(avgRating) : 0;
            model.addAttribute("averageRating", averageRating);

            List<Rating> ratings = ratingService.getRatingsByProductId(id);
            model.addAttribute("ratingCount", ratings != null ? ratings.size() : 0);

            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                Customer customer = customerService.getCustomerByEmail(email).orElse(null);

                if (customer != null) {
                    boolean hasPurchased = orderService.hasCustomerPurchasedProduct(customer.getId(), id);
                    model.addAttribute("hasPurchased", hasPurchased);

                    Optional<Rating> existingRating = ratingService.getRatingByCustomerAndProduct(customer.getId(), id);
                    model.addAttribute("existingRating", existingRating.orElse(null));
                }
            }

            return "productdetail";
        } catch (Exception e) {
            System.err.println("Error in productDetail: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the product details.");
            return "error";
        }
    }

    @PostMapping("/submitRating")
    public String submitRating(@RequestParam Long productId,
                               @RequestParam Integer ratingValue,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            if (!orderService.hasCustomerPurchasedProduct(customer.getId(), productId)) {
                throw new RuntimeException("You must purchase this product before rating it");
            }

            Optional<Rating> existingRating = ratingService.getRatingByCustomerAndProduct(customer.getId(), productId);
            if (existingRating.isPresent()) {
                throw new RuntimeException("You have already rated this product");
            }

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Rating rating = new Rating();
            rating.setCustomer(customer);
            rating.setProduct(product);
            rating.setRating(ratingValue);

            ratingService.saveRating(rating);

            redirectAttributes.addFlashAttribute("success", "Rating submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/productdetail.html?id=" + productId;
    }

    @GetMapping("/orderconfirmation.html")
    public String showOrderConfirmation(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        Payment payment = paymentService.getPaymentByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        return "orderconfirmation";
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "payment";
    }

    @PostMapping("/payment/submit")
    @Transactional
    public String processPayment(@RequestParam String paymentMethod,
                                 @RequestParam(required = false) String cardNumber,
                                 @RequestParam(required = false) String expiryDate,
                                 @RequestParam(required = false) String cvv,
                                 @RequestParam(required = false) String cardholderName,
                                 @RequestParam(required = false) String upiId,
                                 @RequestParam(required = false) String bankName,
                                 @RequestParam(required = false) String walletNumber,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Cart cart = cartService.getCartByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                return "redirect:/cart.html";
            }

            Order order = new Order();
            order.setTime(LocalDateTime.now());
            order.setAmount(cartService.calculateCartTotal(cart.getCartId()));
            order.setCustomer(customer);
            order = orderService.saveOrder(order);

            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(cartItem.getTotalPrice());
                orderItemService.saveOrderItem(orderItem);
            }

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(order.getAmount());

            // Capture payment details based on method
            String paymentDetails = "";
            String lastFour = "";

            if ("Credit Card".equals(paymentMethod) || "Debit Card".equals(paymentMethod)) {
                if (cardNumber != null && cardNumber.length() >= 4) {
                    lastFour = cardNumber.substring(cardNumber.length() - 4);
                    paymentDetails = "Card: **** **** **** " + lastFour + " | Exp: " + expiryDate + " | Name: " + cardholderName;
                }
            } else if ("UPI".equals(paymentMethod)) {
                paymentDetails = "UPI ID: " + (upiId != null ? upiId : "N/A");
            } else if ("Net Banking".equals(paymentMethod)) {
                paymentDetails = "Bank: " + (bankName != null ? bankName : "N/A");
            } else if ("Wallet / Gift Card".equals(paymentMethod)) {
                if (walletNumber != null && walletNumber.length() >= 4) {
                    lastFour = walletNumber.substring(walletNumber.length() - 4);
                    paymentDetails = "Wallet/Card: ****" + lastFour;
                }
            } else if ("Cash on Delivery".equals(paymentMethod)) {
                paymentDetails = "Cash on Delivery";
            }

            payment.setCardNumberLastFour(lastFour.isEmpty() ? null : lastFour);
            payment.setPaymentDetails(paymentDetails);

            paymentService.savePayment(payment);

            cartItemService.deleteByCartId(cart.getCartId());

            redirectAttributes.addFlashAttribute("success",
                    "Order placed successfully! Order ID: " + order.getOrderId());
            return "redirect:/orderconfirmation.html?orderId=" + order.getOrderId();

        } catch (Exception e) {
            logger.error("Payment processing error: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "Payment failed: " + e.getMessage());
            return "redirect:/payment.html";
        }
    }


    @GetMapping("/myorders")
    public String myOrders() {
        return "myorders";
    }

    @GetMapping("/api/customer/orders")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCustomerOrders() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }

            String email = auth.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            List<Order> orders = orderService.getOrdersByCustomerId(customer.getId());

            List<Map<String, Object>> orderDTOs = orders.stream()
                    .map(order -> {
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", order.getOrderId());
                        dto.put("orderDate", order.getTime());
                        dto.put("status", "Pending");
                        dto.put("amount", order.getAmount());

                        if (order.getPayment() != null) {
                            Map<String, Object> paymentInfo = new HashMap<>();
                            paymentInfo.put("paymentMethod", order.getPayment().getPaymentMethod());
                            paymentInfo.put("amount", order.getPayment().getAmount());
                            dto.put("payment", paymentInfo);
                        }

                        if (order.getOrderItems() != null) {
                            List<Map<String, Object>> items = order.getOrderItems().stream()
                                    .map(item -> {
                                        Map<String, Object> itemMap = new HashMap<>();
                                        if (item.getProduct() != null) {
                                            itemMap.put("productName", item.getProduct().getName());
                                            itemMap.put("price", item.getProduct().getPrice());
                                            itemMap.put("imageUrl", item.getProduct().getImageUrl()); // ADDED
                                        }
                                        itemMap.put("quantity", item.getQuantity());
                                        itemMap.put("totalPrice", item.getTotalPrice());
                                        return itemMap;
                                    })
                                    .collect(Collectors.toList());
                            dto.put("items", items);
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(orderDTOs);

        } catch (Exception e) {
            logger.error("Error fetching customer orders: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
