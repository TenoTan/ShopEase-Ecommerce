package com.example.ecommerce.controller;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.AuditLogRepository;
import com.example.ecommerce.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AuditLogRepository auditLogRepository;

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
        // Add any attributes needed for the registration form
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

        // Filter by category or search keyword
        if (categoryId != null) {
            products = productService.getProductsByCategoryId(categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProductsByKeyword(keyword);
        } else {
            products = productService.getAllProducts();
        }

        // Price range filtering
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

        // Rating calculations
        Map<Long, ProductRatingInfo> productRatings = new HashMap<>();
        for (Product product : products) {
            Double avgRating = ratingService.getAverageRatingForProduct(product.getProductId());
            int ratingValue = avgRating != null ? (int) Math.round(avgRating) : 0;
            int ratingCount = ratingService.getRatingsByProductId(product.getProductId()).size();
            productRatings.put(product.getProductId(), new ProductRatingInfo(ratingValue, ratingCount));
        }

        // Minimum rating filter
        if (minRating != null) {
            products = products.stream()
                    .filter(p -> {
                        ProductRatingInfo ratingInfo = productRatings.get(p.getProductId());
                        return ratingInfo != null && ratingInfo.getAvgRating() >= minRating;
                    })
                    .collect(Collectors.toList());
        }

        // Add attributes to model
        model.addAttribute("products", products);
        model.addAttribute("productRatings", productRatings);
        model.addAttribute("categories", categoryService.getAllCategories());

        // Maintain filter state
        if (categoryId != null) model.addAttribute("selectedCategoryId", categoryId);
        if (minRating != null) model.addAttribute("minRating", minRating);
        if (priceRange != null) model.addAttribute("priceRange", priceRange);
        if (keyword != null) model.addAttribute("keyword", keyword);

        return "product";
    }


    // Helper class to store rating information
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
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Find the customer by email
        Optional<Customer> customerOpt = customerService.getCustomerByEmail(email);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();

            // Get the customer's cart
            Optional<Cart> cartOpt = cartService.getCartByCustomerId(customer.getId());

            // If cart doesn't exist, create a new one
            Cart cart;
            if (cartOpt.isEmpty()) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart = cartService.saveCart(cart);
            } else {
                cart = cartOpt.get();
            }

            // Get cart items
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

            // Calculate total price
            BigDecimal totalPrice = cartService.calculateCartTotal(cart.getCartId());
            if (totalPrice == null) {
                totalPrice = BigDecimal.ZERO;
            }

            // Add cart data to the model
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
        } else {
            // Handle case where customer is not found
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

            // Get the authenticated user
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Get the product
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Get or create customer's cart
            Optional<Cart> cartOpt = cartService.getCartByCustomerId(customer.getId());
            Cart cart;
            if (cartOpt.isEmpty()) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart = cartService.saveCart(cart);
            } else {
                cart = cartOpt.get();
            }

            // Check if product already exists in cart
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());
            boolean productExists = false;

            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId().equals(productId)) {
                    // Update quantity if product already in cart
                    int newQuantity = item.getQuantity() + 1;
                    BigDecimal newTotalPrice = product.getPrice().multiply(new BigDecimal(newQuantity));

                    // Create new CartItem with updated values
                    CartItem updatedItem = new CartItem();
                    updatedItem.setCart(cart);
                    updatedItem.setProduct(product);
                    updatedItem.setQuantity(newQuantity);
                    updatedItem.setTotalPrice(newTotalPrice);

                    // Save updated item
                    cartItemService.saveCartItem(updatedItem);
                    productExists = true;
                    break;
                }
            }

            // Add new cart item if product not already in cart
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
            // Get the authenticated user
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Get the customer's cart
            Cart cart = cartService.getCartByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            // Remove the item from the cart
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
            // Get the authenticated user
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Get the customer's cart
            Cart cart = cartService.getCartByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            // Get the product
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Find the cart item to update
            List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId().equals(productId)) {
                    // Update quantity
                    if (quantity <= 0) {
                        // If quantity is 0 or negative, remove the item
                        cartService.removeItemFromCart(cart.getCartId(), productId);
                    } else {
                        // Otherwise update the quantity
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

    // Seller-specific mappings

    @GetMapping("/seller/products/add")
    public String addProductForm(Model model) {
        // Add a new empty product to the model
        model.addAttribute("product", new Product());

        // Add categories for dropdown
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
            // Get the current seller
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            // Set the seller and category for the product
            product.setSeller(seller);

            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // Save the product
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
        // Get the current seller
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Get the product
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product belongs to the current seller
        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You don't have permission to edit this product");
        }

        // Add product and categories to the model
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
            // Get the current seller
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            // Get the existing product
            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Check if the product belongs to the current seller
            if (!existingProduct.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("You don't have permission to edit this product");
            }

            // Update the product fields
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStockQuantity(product.getStockQuantity());
            existingProduct.setImageUrl(product.getImageUrl());

            // Update category if changed
            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);

            // Save the updated product
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
            // Fetch the product by ID
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Update the product fields
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);

            // Save the updated product
            productService.saveProduct(product);

            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
            return "redirect:/sellerhomepage.html"; // or wherever you want to redirect after update
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
            return "redirect:/seller/editProduct?productId=" + productId; // or back to edit page
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
            // Get the current seller
            String email = authentication.getName();
            Seller seller = sellerService.getSellerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));

            // Get the product
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Check if the product belongs to the current seller
            if (!product.getSeller().getId().equals(seller.getId())) {
                throw new RuntimeException("You don't have permission to delete this product");
            }

            // Delete the product
            productService.deleteProduct(productId);

            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete product: " + e.getMessage());
        }

        return "redirect:/sellerhomepage.html";
    }

    @GetMapping("/seller/analytics")
    public String sellerAnalytics(Model model, Authentication authentication) {
        // Get the current seller
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Add seller to the model
        model.addAttribute("seller", seller);

        // Here you would add analytics data to the model
        // This is a placeholder for actual analytics implementation

        return "selleranalytics";
    }

    @GetMapping("/seller/sales")
    public String sellerSales(Model model, Authentication authentication) {
        // Get the current seller
        String email = authentication.getName();
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Add seller to the model
        model.addAttribute("seller", seller);

        // Here you would add sales data to the model
        // This is a placeholder for actual sales implementation

        return "sellersales";
    }
    @GetMapping("/productdetail.html")
    public String productDetail(@RequestParam Long id, Model model, Authentication authentication) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            model.addAttribute("product", product);

            // Calculate average rating
            Double avgRating = ratingService.getAverageRatingForProduct(id);
            int averageRating = avgRating != null ? (int) Math.round(avgRating) : 0;
            model.addAttribute("averageRating", averageRating);

            // Get all ratings for the product
            List<Rating> ratings = ratingService.getRatingsByProductId(id);
            model.addAttribute("ratingCount", ratings != null ? ratings.size() : 0);

            // If user is logged in
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                Customer customer = customerService.getCustomerByEmail(email).orElse(null);

                if (customer != null) {
                    // Check if the user has purchased the product
                    boolean hasPurchased = orderService.hasCustomerPurchasedProduct(customer.getId(), id);
                    model.addAttribute("hasPurchased", hasPurchased);

                    // Check if the user has already rated the product
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
            // Get the authenticated customer
            String email = authentication.getName();
            Customer customer = customerService.getCustomerByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Verify customer has purchased the product
            if (!orderService.hasCustomerPurchasedProduct(customer.getId(), productId)) {
                throw new RuntimeException("You must purchase this product before rating it");
            }

            // Check if customer has already rated this product
            Optional<Rating> existingRating = ratingService.getRatingByCustomerAndProduct(customer.getId(), productId);
            if (existingRating.isPresent()) {
                throw new RuntimeException("You have already rated this product");
            }

            // Get the product
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Create and save the rating
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
        // Fetch the order
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        // Fetch the payment
        Payment payment = paymentService.getPaymentByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));
        // Add to model
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        return "orderconfirmation"; // Name of your Thymeleaf template
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "payment"; // This will render payment.html from your templates
    }
        @Autowired private OrderItemService orderItemService;
        @Autowired private PaymentService paymentService;

        @PostMapping("/payment/submit")
        @Transactional
        public String processPayment(@RequestParam String paymentMethod,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes, HttpServletRequest request) {
            try {
                // 1. Get authenticated customer
                String email = authentication.getName();
                Customer customer = customerService.getCustomerByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Customer not found"));

                // 2. Get cart and validate
                Cart cart = cartService.getCartByCustomerId(customer.getId())
                        .orElseThrow(() -> new RuntimeException("Cart not found"));
                List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

                if (cartItems.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                    return "redirect:/cart.html";
                }

                // 3. Create and save Order
                Order order = new Order();
                order.setTime(LocalDateTime.now());
                order.setAmount(cartService.calculateCartTotal(cart.getCartId()));
                order.setCustomer(customer);
                order = orderService.saveOrder(order);
                // Create audit log entry after order is saved
                AuditLog log = new AuditLog();
                log.setUserId(customer.getId());
                log.setAction("ORDER_PLACED");
                log.setTimestamp(LocalDateTime.now());
                log.setDetails("Order ID: " + order.getOrderId() + " placed successfully");
                // Add HttpServletRequest as method param
                auditLogRepository.save(log);



                // 4. Create OrderItems from CartItems
                for (CartItem cartItem : cartItems) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setTotalPrice(cartItem.getTotalPrice());
                    orderItemService.saveOrderItem(orderItem);
                }

                // 5. Create and save Payment
                Payment payment = new Payment();
                payment.setOrder(order); // order is the Order entity you just created
                payment.setPaymentMethod(paymentMethod);
                payment.setAmount(order.getAmount());
                paymentService.savePayment(payment);

                // 6. Clear cart items
                cartItemService.deleteByCartId(cart.getCartId());

                redirectAttributes.addFlashAttribute("success",
                        "Order placed successfully! Order ID: " + order.getOrderId());
                return "redirect:/orderconfirmation.html?orderId=" + order.getOrderId();

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "Payment failed: " + e.getMessage());
                return "redirect:/payment.html";
            }
        }

}
