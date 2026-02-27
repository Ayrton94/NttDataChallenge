-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: bank_accounts
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Current Database: `bank_accounts`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bank_accounts` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bank_accounts`;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `account_number` varchar(30) NOT NULL,
  `type` varchar(20) NOT NULL,
  `balance` decimal(19,2) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_number` (`account_number`),
  KEY `idx_accounts_customer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,16,'ACC-0001','SAVINGS',900.50,1,'2026-02-26 01:11:41'),(7,16,'DDD-0001','SAVINGS',990.50,1,'2026-02-26 08:49:40'),(8,16,'EDD-0001','SAVINGS',600.50,1,'2026-02-26 08:55:54'),(9,16,'FDD-0001','SAVINGS',100.50,1,'2026-02-26 09:09:57'),(10,16,'GDD-0001','SAVINGS',150.50,1,'2026-02-26 13:15:13'),(11,16,'HDD-0001','SAVINGS',650.50,1,'2026-02-26 13:20:25'),(12,17,'ODD-0001','SAVINGS',650.50,1,'2026-02-26 13:20:48'),(13,20,'FFD-0001','SAVINGS',50.50,1,'2026-02-26 13:21:30'),(14,21,'ZZZ-0001','SAVINGS',50.50,1,'2026-02-26 13:41:12'),(15,22,'rrrr-0001','SAVINGS',50.50,1,'2026-02-26 13:42:41'),(16,23,'TTTT-0001','SAVINGS',50.50,1,'2026-02-26 13:43:33'),(17,24,'UUUU-0001','SAVINGS',50.50,1,'2026-02-26 14:46:48'),(18,16,'CTA-0001','SAVINGS',570.50,1,'2026-02-26 23:40:15');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','init','SQL','V1__init.sql',1751293967,'root','2026-02-26 06:07:58',52,1),(2,'2','create movements','SQL','V2__create_movements.sql',1674594176,'root','2026-02-27 02:46:04',158,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movements`
--

DROP TABLE IF EXISTS `movements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `movement_date` timestamp NOT NULL,
  `type` varchar(20) NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `balance` decimal(19,2) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_mov_account_date` (`account_id`,`movement_date`),
  KEY `idx_mov_customer_date` (`customer_id`,`movement_date`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movements`
--

LOCK TABLES `movements` WRITE;
/*!40000 ALTER TABLE `movements` DISABLE KEYS */;
INSERT INTO `movements` VALUES (1,18,16,'2026-02-26 21:49:24','CREDIT',50.00,200.50,1,'2026-02-27 02:49:23'),(2,18,16,'2026-02-26 21:54:19','DEBIT',30.00,170.50,1,'2026-02-27 02:54:19'),(3,18,16,'2026-02-26 23:36:22','CREDIT',100.00,270.50,1,'2026-02-27 04:36:22'),(4,18,16,'2026-02-26 23:37:01','CREDIT',100.00,370.50,1,'2026-02-27 04:37:01'),(5,18,16,'2026-02-26 23:37:21','CREDIT',100.00,470.50,1,'2026-02-27 04:37:20'),(6,18,16,'2026-02-26 23:40:15','CREDIT',100.00,570.50,1,'2026-02-27 04:40:15');
/*!40000 ALTER TABLE `movements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `bank_customers`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bank_customers` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bank_customers`;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `identification` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlg23guupmqfqjs6j1005fomvw` (`identification`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Esmeraldas','MALE','0102030405','Ayrton Test','$2a$10$z4GW1aS1VDKEpPRAyhwBouF1YKKq42FdJh/a06xwp.FVNIOUdw6zW','0999999999',_binary ''),(17,'Esmeraldas','MALE','942512','Ayrton Adame Bernal Final Test ACTUALIZADO','$2a$10$45diXfuV6blIgs42b30CBejcw1wAyJP/3MFi1TbylIiZ1m23TQ6Ii','0999999999',_binary ''),(18,'Quito','MALE','564932','Carlos Test','$2a$10$o9IYG7CGDF5nkGFIK3CaZ.0ua7vpTTusrHPiW.rCAfX5euSJE5eQq','0999999999',_binary '');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-27  5:38:14
