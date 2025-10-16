-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: ecommerce
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `house_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,'Delhi','Delhi','India','123','Admin','$2a$10$rIpv1hg1dookI8Y485xOuOI7LCuscqGc1KMVOY8TG3mMjqFvx/thS','0987654321','Sheikh Sarai'),(2,'A-12, Lajpat Nagar','Delhi','India','A-12','Rohit Sharma','$2a$10$cIECFw.7NlkJvQ9J6rFtveB1MpSJ/ZBfPKXOm8wA1vM5qpGtpETcm','9876543210','Ring Road'),(3,'B-44, Andheri East','Mumbai','India','B-44','Priya Mehra','$2a$10$/W7wCTEjtIxZ49QGROYJ/ucRFMxlDI9xrv0BBHBtIDcI8GSMy9/Oq','9123456780','Western Express Hwy'),(4,'C-21, Banjara Hills','Hyderabad','India','C-21','Anil Kumar','$2a$10$YXMVAPqNhjddi8DViNain.Pq0N8pZ8mRTgFaGls4SOK0cSdij5M.6','9988776655','Road No. 2'),(5,'D-19, Salt Lake','Kolkata','India','D-19','Suman Ghosh','$2a$10$6oCX93t.6CaDV2BiufTTMO3OxyFTTcXIaLTCT3ZsdM2fta6JDBxkW','9001122334','Sector V'),(6,'E-33, Anna Nagar','Chennai','India','E-33','Lakshmi Iyer','$2a$10$iesUNuOfer5Yh4x14lTzCeTAN2ogeeb8z8.H/KNOTjeu/lEtVtL5.','9876012345','2nd Avenue'),(7,'F-18, Sector 62','Noida','India','F-18','Rahul Verma','$2a$10$OhluDz/56rTwOdBfL83beuTJESRjiAfDnxwOGuH9ngKttts.VGpEW','9812345678','Golf Course Road'),(8,'G-55, HSR Layout','Bengaluru','India','G-55','Sneha Rao','$2a$10$HpwwAOvPyLFmk/kkaAYQAO9fxZ3U4mXZOH.5QJoBGTD2IYiZgBR6e','9900112233','27th Main'),(9,'H-22, Alkapuri','Vadodara','India','H-22','Vikas Patel','$2a$10$XLCFYxTHazt6IUKrneDw2Oa6wyt1wQYfF9/TN4jhPZqB02NyDIrcy','9723456789','Race Course Rd'),(10,'I-30, Civil Lines','Jaipur','India','I-30','Meena Singh','$2a$10$OobjPQLozDnBT3Zm73JdZOap9k5sctFji/lhVCNoeb00JClhUFlgu','9823456712','M.I. Road'),(11,'J-77, MG Road','Pune','India','J-77','Amit Joshi','$2a$10$AkI85kJG2ZVxPvviG1u3vuStY.xMkgLunIm2cyFYUEpHRapGIrzC.','9765432109','MG Road');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `UK867x3yysb1f3jk41cv3vsoejj` (`customer_id`),
  CONSTRAINT `FKioh3c0mo0al2kswtnk5r09y7f` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `quantity` int DEFAULT NULL,
  `total_price` decimal(38,2) DEFAULT NULL,
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`cart_id`,`product_id`),
  KEY `FKqkqmvkmbtiaqn2nfqf25ymfs2` (`product_id`),
  CONSTRAINT `FK1uobyhgl1wvgt1jpccia8xxs3` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
  CONSTRAINT `FKqkqmvkmbtiaqn2nfqf25ymfs2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Beauty'),(2,'Women\'s Fashion'),(3,'Men\'s Fashion'),(4,'Phones'),(5,'Books'),(6,'Shoes'),(7,'Furniture'),(8,'Toys'),(9,'Appliances');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `house_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrfbvkrffamfql7cjmen8v976v` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,NULL,NULL,NULL,'atharv.del1610@gmail.com',NULL,'Atharv Sharma','$2a$10$DyiLVrCNfXMCs8xKZvvIluM/4Eg2V1N8dIa0dPLlgJjMK1ahyYAbW','956042009',NULL),(2,NULL,NULL,NULL,'sidtdm101@gmail.com',NULL,'Siddharth Gupta','$2a$10$sjvGW6tuIbFOCL2EarPGSurV.iG8bVvwN7IXEg9.ZXIIUbTSTSv8C','9717954118',NULL),(3,NULL,NULL,NULL,'juhee@gmail.com',NULL,'Juhee','$2a$10$gDRdVgxVL3wVcuLMd8INsOLXtdz3.fPnMbi4zGcs908C13rHiV12C','100',NULL),(4,NULL,'Delhi','India','hello@gmail.com',NULL,'Aryan','$2a$10$9ETCAtY0JpFmN0tvPkGxceWYQRLWYhC8WVPh8nedJiQeTG06wv9jy','a123124','Baker'),(5,NULL,'New Delhi','India','rajesh.kumar@gmail.com',NULL,'Juhee1','$2a$10$RTa1mt6bNoFBI6TKU0MeCO58uKx3GLsYAal8G1dy2A1LE3G7IIYmG','123-456-7890','Sheikh Sarai-1'),(6,NULL,'UDUPI','India','ficoma1560@erapk.com',NULL,'pranav','$2a$10$BOFGQ7DAhMW5XMG4Yf2Kp.nnWWuKdIHxXIHl8GhobswbbhSYnmDEW','1234556789','Sheikh Sarai-1'),(7,NULL,'Bengaluru','India','wagle3903@gmail.com',NULL,'Aryan Wagle','$2a$10$AgB4doK44sikpvhQ.z5edeqaCwVv8Bgis/mIAj8iSA.UPCwW5DGZy','7625041505','Bellary Road, Hebbal'),(8,NULL,'UDUPI','saudi arabia','kumar@gmail.com',NULL,'Pranav Kumar','$2a$10$XLOiGwNXc0vMK2FWleL6r.M038Dqu/gBVdf99qe3BynXffDHJtAIa','1234512345','Baker'),(9,NULL,'Noida','India','bhoomi@gmail.com',NULL,'Bhoomi','$2a$10$Hp.uRi6hvDVQoOOuwhWQ/.NojO9XBcEe3TXe4mV7QCx7.xnfTH75a','1234569481','Sector-135'),(10,NULL,'noida','india','anjali@gmail.com',NULL,'anjali bansal','$2a$10$1xMmPbdryWYznhdZfDg2j.6.PcN84bc9Ub1xsqRUzqXU3MCnXLOiW','9996548215','sec 137');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `quantity` int DEFAULT NULL,
  `total_price` decimal(38,2) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1299.00,25,10),(3,3897.00,26,10),(1,10999.00,27,23),(6,14994.00,28,33),(13,1300000.00,29,6),(2,21000.00,30,21),(1,999.00,31,16),(3,1497.00,31,18),(2,28598.00,31,28),(1,100000.00,32,6),(2,21998.00,33,57),(1,1299.00,34,10),(1,7999.00,34,34),(1,1799.00,35,12),(1,2599.00,35,17),(1,10500.00,35,21),(1,15999.00,35,29),(1,399.00,35,38),(1,399.00,35,41),(1,3899.00,35,49),(1,3999.00,35,50),(1,1999.00,36,27),(1,70000.00,37,7),(2,998.00,37,39),(4,640000.00,38,5),(2,2598.00,38,10),(1,3299.00,39,59);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `reduce_product_stock_after_order` AFTER INSERT ON `order_items` FOR EACH ROW BEGIN
    UPDATE products
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `amount` decimal(38,2) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1200.00,1,1,'2025-04-16 11:29:15.611212'),(2400.00,1,2,'2025-04-16 11:52:05.470886'),(3600.00,1,3,'2025-04-17 12:21:52.300033'),(1200.00,1,4,'2025-04-17 16:15:56.351369'),(54000.00,1,5,'2025-04-17 16:23:47.897584'),(54000.00,1,6,'2025-04-17 18:14:11.864781'),(40000.00,1,7,'2025-04-20 11:56:52.784494'),(40000.00,1,8,'2025-04-20 11:58:34.619715'),(18000.00,1,9,'2025-04-20 12:02:32.094683'),(40000.00,1,10,'2025-04-20 12:24:07.716719'),(40000.00,1,11,'2025-04-20 12:29:37.708771'),(40000.00,1,12,'2025-04-20 12:33:33.307077'),(18000.00,1,13,'2025-04-20 12:34:36.826487'),(40000.00,1,14,'2025-04-20 12:37:15.829697'),(40000.00,3,15,'2025-04-20 12:39:56.510678'),(40000.00,1,16,'2025-04-20 18:41:57.677647'),(200000.00,1,17,'2025-04-20 18:56:12.564806'),(40000.00,1,18,'2025-04-20 19:42:39.509984'),(18000.00,1,19,'2025-04-20 20:02:21.815611'),(18000.00,1,20,'2025-04-20 20:03:11.252796'),(18000.00,1,21,'2025-04-20 20:08:35.538368'),(40000.00,1,22,'2025-04-20 20:10:56.986848'),(40000.00,1,23,'2025-04-21 03:54:04.462712'),(98000.00,1,24,'2025-04-21 04:21:04.596861'),(1299.00,1,25,'2025-04-21 20:11:58.977687'),(3897.00,7,26,'2025-04-21 20:15:05.841901'),(10999.00,1,27,'2025-04-21 20:58:45.779747'),(14994.00,8,28,'2025-04-22 09:52:28.211502'),(1300000.00,8,29,'2025-04-22 09:52:52.255582'),(21000.00,1,30,'2025-04-22 13:37:33.820725'),(31094.00,1,31,'2025-04-22 17:22:41.965628'),(100000.00,1,32,'2025-04-22 18:27:32.639568'),(21998.00,9,33,'2025-04-22 20:22:05.078944'),(9298.00,10,34,'2025-04-23 03:46:02.065493'),(39593.00,3,35,'2025-04-23 04:13:23.729955'),(1999.00,1,36,'2025-04-23 05:32:39.985876'),(70998.00,1,37,'2025-04-23 06:09:50.398579'),(642598.00,1,38,'2025-06-07 20:11:58.718835'),(3299.00,2,39,'2025-06-07 20:16:55.709990');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `amount` decimal(38,2) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  UNIQUE KEY `UK8vo36cen604as7etdfwmyjsxt` (`order_id`),
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1200.00,1,'Credit Card'),(2400.00,2,'Cash on Delivery'),(3600.00,3,'Net Banking'),(1200.00,4,'UPI'),(54000.00,5,'Credit Card'),(54000.00,6,'Credit Card'),(40000.00,7,'UPI'),(40000.00,8,'Wallet / Gift Card'),(18000.00,9,'UPI'),(40000.00,10,'Cash on Delivery'),(40000.00,11,'Debit Card'),(40000.00,12,'Debit Card'),(18000.00,13,'Cash on Delivery'),(40000.00,14,'Net Banking'),(40000.00,15,'Wallet / Gift Card'),(40000.00,16,'Net Banking'),(200000.00,17,'Debit Card'),(40000.00,18,'Wallet / Gift Card'),(18000.00,19,'Debit Card'),(18000.00,20,'Wallet / Gift Card'),(18000.00,21,'UPI'),(40000.00,22,'Net Banking'),(40000.00,23,'Credit Card'),(98000.00,24,'Debit Card'),(1299.00,25,'UPI'),(3897.00,26,'Wallet / Gift Card'),(10999.00,27,'Cash on Delivery'),(14994.00,28,'Net Banking'),(1300000.00,29,'Net Banking'),(21000.00,30,'Net Banking'),(31094.00,31,'Credit Card'),(100000.00,32,'Credit Card'),(21998.00,33,'Credit Card'),(9298.00,34,'Wallet / Gift Card'),(39593.00,35,'Cash on Delivery'),(1999.00,36,'Cash on Delivery'),(70998.00,37,'Net Banking'),(642598.00,38,'Debit Card'),(3299.00,39,'Credit Card');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj6vpvxdkrgqdhqs0s1dlqhp6j` (`product_id`),
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,58,'https://inspireonline.in/cdn/shop/files/MacBook_Air_15_in_M3_Space_Grey_PDP_Image_Position_1__en-IN_65b3b632-c0fa-43bd-bd82-9b3d0e01fbbb.jpg?v=1730311615&width=823'),(2,59,'https://i.ebayimg.com/images/g/stQAAOSwQa5lLaXj/s-l1600.jpg');
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `price` decimal(38,2) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `stock_quantity` int DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  KEY `FK9ked8me9rhc6c5s0r489qwm85` (`seller_id`),
  CONSTRAINT `FK9ked8me9rhc6c5s0r489qwm85` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (160000.00,9,5,'Sony Bravia 4K UHD Google TV, 55 inch','Sony Bravia 55XR70','https://sony.scene7.com/is/image/sonyglobalsolutions/TVFY24_UH_PrimaryTout_0pt-image12-d?$originalDimensions$&fmt=png-alpha',96,1),(100000.00,9,6,'Samsung 4K UHD Smart LED, 55 inch','Samsung 55LS03DAUL','https://cdn.lotuselectronics.com/webpimages/655561IM.webp',100,1),(70000.00,4,7,'6.1-inch Dynamic AMOLED, Snapdragon 8 Gen 2, 128GB storage','Samsung Galaxy S23','https://rukminim2.flixcart.com/image/850/1000/xif0q/mobile/t/0/g/-original-imah4zp7fvqp8wev.jpeg?q=20&crop=false',99,1),(50000.00,9,8,'Double Door, Smart Inverter Compressor, 3 Star','LG 260L Frost-Free Refrigerator','https://www.lg.com/content/dam/channel/wcms/in/images/refrigerators/gl-t292rpzy_dpzzebn_eail_in_c/gallery/01-1600x1062.jpg',99,1),(20000.00,9,9,'Cord-free, powerful suction, laser dust detection','Dyson V12 Vacuum Cleaner','https://m.media-amazon.com/images/I/51fSrodp9yL.jpg',100,1),(1299.00,2,10,'Ankle-length sleeveless dress with floral print, ideal for summer outings','Floral Maxi Dress','https://www.vastranand.in/cdn/shop/files/1_bc4cce67-0c92-4034-b34b-644c1cd9ea66.jpg?v=1743074442',98,3),(1499.00,2,11,'Dark blue stretchable denim, perfect for casual wear','High-Waist Skinny Jeans','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQufRmZj5y_lz8piL2LpsDQHHtJ1grsKco7AA&s',100,3),(1799.00,2,12,'Oversized fleece hoodie with front pocket and drawstring','Pastel Pink Hoodie','https://giftwarewales.co.uk/cdn/shop/files/oversized-sherpa-fleece-hoodie-pink-784815_700x700.png?v=1723227102',100,3),(2299.00,2,13,'Ethnic wear set with embroidered top and dupatta','Embroidered Kurti Set','https://i.pinimg.com/originals/2b/3b/ac/2b3bacf2aad2f980cb4268244d9d06e7.jpg',100,3),(1299.00,3,14,'Cotton button-down shirt, ideal for formal or party wear','Slim Fit Black Shirt','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkWKAW5DbxeoZPBk9krpGqtre_cZr6EkPM_A&s',100,3),(1599.00,3,15,'Olive green slim-fit chinos with stretch fabric','Casual Chino Pants','https://levi.in/cdn/shop/files/A82860000_01_Style_Shot_cbf4b9a4-e788-404a-b002-0e9a840a4f9d.jpg?v=1740488437',100,3),(999.00,3,16,'White cotton tee with bold front print','Graphic Oversized T-Shirt','https://m.media-amazon.com/images/I/51unIsrxjlL._AC_UY1100_.jpg',100,3),(2599.00,3,17,'Regular fit, mid-wash denim jacket with chest pockets','Classic Blue Denim Jacket','https://assets.ajio.com/medias/sys_master/root/20240816/ZdnS/66bf5f336f60443f310f46d6/-473Wx593H-441769161-navy-MODEL4.jpg',100,3),(499.00,1,18,'Long-lasting, transfer-proof matte finish with a soft applicator','Matte Liquid Lipstick – Rose Nude','https://m.media-amazon.com/images/I/61UyCl5+vPL._AC_UF1000,1000_QL80_.jpg',100,7),(699.00,1,19,'Brightens skin tone and reduces dark spots, suitable for all skin types','Vitamin C Face Serum – 30ml','https://www.themancompany.com/cdn/shop/products/5_9db26da9-5b52-400b-964d-b4d76bed1d3d.jpg?v=1721895175',100,7),(349.00,1,20,'Enriched with aloe vera and hyaluronic acid for instant glow','Hydrating Sheet Mask Combo (Pack of 5)','https://m.media-amazon.com/images/I/51tFFOfysAL._AC_.jpg',100,7),(10500.00,1,21,'A bold, fresh fragrance with notes of bergamot, ambroxan, and vanilla. Perfect for evening wear and special occasions.','Dior Sauvage Eau de Parfum – 100ml','https://images-static.nykaa.com/media/catalog/product/d/b/db992abDIORX00000885.jpg',100,7),(11200.00,1,22,'A woody aromatic fragrance for men with notes of grapefruit, incense, and sandalwood. Elegant and timeless.','Chanel Bleu de Chanel Eau de Parfum – 100ml','https://prettycosmo.com/cdn/shop/files/vv.jpg?v=1702972176',100,7),(10999.00,6,23,'Lightweight, breathable sneakers with visible air cushioning for all-day comfort','Nike Air Max 270 – Men’s Running Shoes','https://static.nike.com/a/images/t_prod/w_1920,c_limit,f_auto,q_auto/ad80fcdc-ea4c-4d5f-9a38-8f16bbc43b0c/image.jpg',99,8),(9999.00,6,24,'Responsive Boost midsole with Primeknit upper, designed for performance and style','Adidas Ultraboost 22 – Women’s Sports Shoes','https://rukminim2.flixcart.com/image/850/1000/xif0q/shoe/2/0/i/8-gx5588-8-adidas-magmau-legprp-turbo-original-imagcnr9jtsf8esz-bb.jpeg?q=90&crop=false',100,8),(4499.00,6,25,'Rugged and durable outdoor boots with full-grain leather upper and traction sole','Woodland Leather Boots – Tan','https://assets.woodlandworldwide.app/product/images/FGF0C3046352A/TAN/FGF0C3046352A_041_1.webp',100,8),(799.00,6,26,'Casual slip-on sandals with cushioned sole and anti-slip grip','Campus Slide-in Sandals – Black & Grey','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT05KDlVpup8YZN8xgiJBBY7Wl66_NlQNNGgg&s',100,8),(1999.00,6,27,'Classic lace-up formal shoes with synthetic upper and sleek design for office wear','Bata Formal Oxford Shoes – Men’s Black','https://rukminim2.flixcart.com/image/850/1000/xif0q/shoe/z/h/h/8-8316671-8-bata-black-original-imah39rcpkmc6y69.jpeg?q=90&crop=false',99,8),(14299.00,7,28,'Minimalist center table made with sheesham wood, perfect for living rooms','Solid Wood Coffee Table – Walnut Finish','https://m.media-amazon.com/images/I/81AXa+W3-5L._AC_UF894,1000_QL80_.jpg',99,9),(15999.00,7,29,'Push-back recliner with plush cushioning and high back support','Recliner Chair – Brown Faux Leather','https://images-cdn.ubuy.co.in/634d30837d11b76bfd45c921-inzoy-leather-recliner-chair-heavy-duty.jpg',100,9),(17999.00,7,30,'Spacious almirah with shelves and hanging space','3-Door Wardrobe – Engineered Wood (Oak Finish)','https://m.media-amazon.com/images/I/81o6GAp5ynL._AC_UF1000,1000_QL80_.jpg',100,9),(4899.00,7,31,'Space-saving work desk with drawers, shelves, and cable management','Ergonomic Study Desk – Black & Oak Combo','https://m.media-amazon.com/images/I/81Thcvbc58L.jpg',100,9),(22999.00,7,32,'Premium finish table with matching chairs, ideal for modern apartments','Dining Table Set – 4 Seater, Teak Wood','https://m.media-amazon.com/images/I/81Vdhvv1caL.jpg',100,9),(2499.00,8,33,'Build a fire station with fire trucks, firemen, and rescue gear','LEGO City Fire Station Set – 60215','https://m.media-amazon.com/images/I/813muXP0DiL.jpg',100,10),(7999.00,8,34,'A beautiful 3-story mansion with multiple rooms and accessories for Barbie dolls','Barbie Dream House – 3-Story Mansion','https://m.media-amazon.com/images/I/81F1XIJm1ML._AC_UF1000,1000_QL80_.jpg',100,10),(1799.00,8,35,'Exciting loop-de-loop track set with cars for extreme stunts','Hot Wheels Track Set – Stunt Loop','https://m.media-amazon.com/images/I/8179bH20YAL.jpg',100,10),(599.00,8,36,'3D wooden puzzle to teach kids about animals and nature','Educational Wooden Puzzle – Animal Kingdom','https://m.media-amazon.com/images/I/71gre7+fn+L._AC_UF1000,1000_QL80_.jpg',100,10),(1499.00,8,37,'Sturdy off-road remote-controlled car with a long-lasting battery','Remote Control Car – 4WD Off-Road Racer','https://m.media-amazon.com/images/I/71Mszt-y9mL.jpg',100,10),(399.00,5,38,'A philosophical novel that follows the journey of Santiago, a shepherd boy, seeking his personal legend.','The Alchemist by Paulo Coelho','https://m.media-amazon.com/images/I/61HAE8zahLL.jpg',100,4),(499.00,5,39,'A practical guide to building good habits and breaking bad ones to improve every aspect of your life.','Atomic Habits by James Clear','https://m.media-amazon.com/images/I/61M6KzUbf7L._AC_UF1000,1000_QL80_.jpg',98,4),(699.00,5,40,'A captivating retelling of the story of Achilles and Patroclus, blending Greek mythology with deep emotional storytelling.','The Song of Achilles by Madeline Miller','https://target.scene7.com/is/image/Target/GUEST_ef088868-93cf-403d-8bf5-7391d5fdc3fb?qlt=65&fmt=pjpeg&hei=350&wid=350',100,4),(399.00,5,41,'The first book in the iconic Harry Potter series, where Harry discovers the magical world and his destiny.','Harry Potter and the Sorcerer\'s Stone by J.K. Rowling','https://m.media-amazon.com/images/I/819GoteowlL._UF1000,1000_QL80_.jpg',100,4),(399.00,5,42,'A spiritual guidebook that emphasizes the importance of living in the present moment and transcending mental limitations.','The Power of Now by Eckhart Tolle','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLcaaxFFNJbyE2e2Xa86h1bYdMz8KYRECg_A&s',100,4),(89990.00,4,43,'6.1-inch Super Retina XDR display, A15 Bionic chip, triple-camera system, and up to 20 hours of battery life','iPhone 14 Pro – 128GB','https://www.imagineonline.store/cdn/shop/files/iPhone_14_Pro_Space_Black_PDP_Image_Position-1a__WWEN.jpg?v=1692349757&width=1445',100,5),(124999.00,4,44,'6.8-inch Dynamic AMOLED 2X display, Snapdragon 8 Gen 2, quad-camera setup with 200MP main sensor','Samsung Galaxy S23 Ultra – 256GB','https://m.media-amazon.com/images/I/71lD7eGdW-L.jpg',100,5),(54999.00,4,45,'6.7-inch Fluid AMOLED display, Snapdragon 8 Gen 2, Hasselblad-powered cameras, 100W fast charging','OnePlus 11 – 256GB','https://oasis.opstatics.com/content/dam/oasis/page/2023/in/product/11/marble.png',100,5),(74999.00,4,46,'6.73-inch AMOLED display, Snapdragon 8 Gen 2, 50MP Leica-powered triple cameras','Xiaomi 13 Pro – 128GB','https://m.media-amazon.com/images/I/61RvCwjI7dL._AC_UF1000,1000_QL80_.jpg',100,5),(39999.00,4,47,'6.7-inch AMOLED display, Snapdragon 8 Gen 1, 50MP triple camera setup, 65W fast charging','Realme GT 2 Pro – 256GB','https://rukminim2.flixcart.com/image/850/1000/l3rmzrk0/mobile/3/o/b/-original-imagetmeqj2tndqx.jpeg?q=90&crop=false',100,5),(7899.00,1,48,'A powerful serum to reduce the appearance of wrinkles, fine lines, and skin discoloration.','Estée Lauder Advanced Night Repair Serum','https://m.media-amazon.com/images/I/511Ds5s+aCL._AC_UF1000,1000_QL80_.jpg',100,6),(3899.00,6,49,'Stylish and comfortable sneakers made with premium suede, ideal for casual outings.','Puma Suede Classic Sneakers','https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_600,h_600/global/374915/01/dt04/fnd/IND/fmt/png/Suede-Classic-XXI-Unisex-Sneakers',100,6),(3999.00,7,50,'Space-saving foldable dining table, perfect for small apartments and studios.','Foldable Dining Table – Compact Design','https://m.media-amazon.com/images/I/61nl3hDbNJL._AC_UF894,1000_QL80_.jpg',100,6),(4499.00,8,51,'A fun smartwatch for kids with games, camera, and fitness tracker.','VTech Kidizoom Smartwatch DX2','https://m.media-amazon.com/images/I/81FbghVRGbL.jpg',100,6),(799.00,5,52,'An insightful exploration of the history of human beings, from the Stone Age to the modern day.','A Brief History of Humankind by Yuval Noah Harari','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0FxCfM-_t-RbeO2sctksNPeHO2cPndNQPLg&s',100,6),(3999.00,2,53,'Stretch denim jeans with a flattering slim fit, suitable for any casual occasion.','Levi\'s Women\'s 711 Skinny Jeans','https://levi.in/cdn/shop/files/A70900003_01_Front_c7d4a69d-0caa-49fb-b094-57038fc80cde.jpg?v=1712742039',100,6),(7500.00,3,54,'Timeless, classic sunglasses that provide 100% UV protection.','Ray-Ban Aviator Sunglasses','https://india.ray-ban.com/media/catalog/product//0/r/0rbr0101s_003_gr_030a.jpg',100,6),(184999.00,4,55,'A cutting-edge foldable phone with a 7.6-inch Dynamic AMOLED 2X display, Snapdragon 8 Gen 2 chipset, and enhanced multitasking.','Samsung Galaxy Z Fold 5','https://images.samsung.com/in/smartphones/galaxy-z-fold5/images/galaxy-z-fold5-highlights-multitasking-mo.jpg?imbypass=true',100,6),(16999.00,9,56,'A powerful air fryer that cooks with little or no oil, providing crispy results with up to 90% less fat.','Philips Air Fryer XXL','https://cdn.anscommerce.com/live/image/data/philips/30mar2024/Versatile-Roast-Bake-in-the-oven-Grill-Roast-in-the-oven-Even-heat-it-up.jpg',100,6),(10999.00,4,57,'An exquisite piece of electronics that uses light to indicate unique notifications','Nothing 3a','https://in.nothing.tech/cdn/shop/files/Arc_630x1240_-_Black_750x.png?v=1740665751',100,5),(129999.00,9,58,'M3 Chip, 512GB SSD, 16GB RAM, 13\"','Macbook Air','https://inspireonline.in/cdn/shop/files/MacBook_Air_15_in_M3_Space_Grey_PDP_Image_Position_1__en-IN_65b3b632-c0fa-43bd-bd82-9b3d0e01fbbb.jpg?v=1730311615&width=823',100,1),(3299.00,9,59,'Very very very bad headphones, bad durability, bad cushion','Samson SR850','https://i.ebayimg.com/images/g/stQAAOSwQa5lLaXj/s-l1600.jpg',0,1);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `prevent_negative_price_before_insert` BEFORE INSERT ON `products` FOR EACH ROW BEGIN
    IF NEW.price < 0 THEN
        SET NEW.price = 0;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `insert_product_image` AFTER INSERT ON `products` FOR EACH ROW BEGIN
    IF NEW.image_url IS NOT NULL AND NEW.image_url <> '' THEN
        INSERT INTO product_images (product_id, url)
        VALUES (NEW.product_id, NEW.image_url);
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `prevent_negative_price_before_update` BEFORE UPDATE ON `products` FOR EACH ROW BEGIN
    IF NEW.price < 0 THEN
        SET NEW.price = 0;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratings` (
  `rating` int DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`customer_id`,`product_id`),
  KEY `FK228us4dg38ewge41gos8y761r` (`product_id`),
  CONSTRAINT `FK228us4dg38ewge41gos8y761r` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKb0aai9hp9gtd2don8c08qxn3y` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (5,1,10),(3,1,18),(4,1,21),(4,1,23),(3,2,59),(2,7,10),(4,8,6),(5,9,57);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller`
--

DROP TABLE IF EXISTS `seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seller` (
  `commission_rate` decimal(38,2) DEFAULT NULL,
  `admin_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `business_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcrgbovyy4gvgsum2yyb3fbfn7` (`email`),
  KEY `FKoaqjn25basmn8j0ghpdfwuahu` (`admin_id`),
  CONSTRAINT `FKoaqjn25basmn8j0ghpdfwuahu` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller`
--

LOCK TABLES `seller` WRITE;
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
INSERT INTO `seller` VALUES (15.00,NULL,1,'Electronics Ltd','electronics@gmail.com','$2a$10$EbrwE2NEkhz7lQdhSSAWveUjsh5378o6Du9Vn4CHbGqak//vUkTWu','1234567890'),(6.00,NULL,3,'Mehra Fashions','mehra.fashions@gmail.com','$2a$10$97axtvlEGUvkpN/abNIkVOPSsRZvBNcHW6ES7X8LGDUuIrFht5JWK','9876543212'),(4.50,NULL,4,'Kumar Books','kumar.books@gmail.com','$2a$10$/QUDpQnCT04PUSyq3flDkeZyFUVMyW9UpKyiAbbDJxZIA1pC.PLXW','9123456789'),(10.00,NULL,5,'Ghosh Mobiles','ghosh.mobiles@gmail.com','$2a$10$ALe9mGOZuWSw/r3SRHCt0..H4uaFtl8Gne2DM3HzHbX2FthEz9ioW','9001122336'),(20.00,NULL,6,'General Store','general@gmail.com','$2a$10$AotSj7pNN1U/y/y4mTeXOeLBa3PkbWzOzRTNlW45Uo5Wmzhs2Uy12','9810117301'),(12.00,NULL,7,'Glow & Grace','glow.grace@gmail.com','$2a$10$u/KnbGpKcD1FRXXoqMhDsOXyE.HzRb84Dzni/XlMRFWcPpu.rNkK6','9988776655'),(8.00,NULL,8,'SoleCraft','contact@solecraft.com','$2a$10$y9x27ruS9lxeEogZsATkhOZK05a20k7YzTPdK1XGYx1FCCvHt73Wi','9811223344'),(18.00,NULL,9,'Urban Woodworks','urbanwood@gmail.com','$2a$10$bLJcESwO71Vczhw4FGGQA.YXgNKGGENDBZmRWQRRVzEIYTfYYgXrS','8888999900'),(7.00,NULL,10,'KiddieJoy','hello@kiddiejoy.in','$2a$10$0UtbhQNVjLeKlXSoGOnUY.sFBnu4xprPHg1hnDwOFbRv4DNysDy.C','9822334455');
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-13  0:27:39
