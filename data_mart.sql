-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               10.4.28-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for data_mart
CREATE DATABASE IF NOT EXISTS `data_mart` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `data_mart`;

-- Dumping structure for table data_mart.date_dimension
CREATE TABLE IF NOT EXISTS `date_dimension` (
  `id` int(11) NOT NULL,
  `full_date` date DEFAULT NULL,
  `day` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.dim_manufacturer
CREATE TABLE IF NOT EXISTS `dim_manufacturer` (
  `manufacturer_id` int(11) NOT NULL AUTO_INCREMENT,
  `manufacturer_name` varchar(255) NOT NULL,
  PRIMARY KEY (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.dim_name
CREATE TABLE IF NOT EXISTS `dim_name` (
  `name_id` int(11) NOT NULL AUTO_INCREMENT,
  `name_value` varchar(512) NOT NULL,
  PRIMARY KEY (`name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1535 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.dim_tech_audio
CREATE TABLE IF NOT EXISTS `dim_tech_audio` (
  `tech_audio_id` int(11) NOT NULL AUTO_INCREMENT,
  `audio_technology_name` varchar(255) NOT NULL,
  PRIMARY KEY (`tech_audio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.dim_tech_image
CREATE TABLE IF NOT EXISTS `dim_tech_image` (
  `tech_image_id` int(11) NOT NULL AUTO_INCREMENT,
  `image_technology_name` varchar(512) NOT NULL,
  PRIMARY KEY (`tech_image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=511 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.fact_product
CREATE TABLE IF NOT EXISTS `fact_product` (
  `name_id` int(11) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `manufacturer_id` int(11) NOT NULL,
  `tech_image_id` int(11) NOT NULL,
  `tech_audio_id` int(11) NOT NULL,
  `price` decimal(18,2) NOT NULL,
  `oldPrice` decimal(18,2) DEFAULT 0.00,
  `discountPercent` int(11) DEFAULT 0,
  `screenSize` varchar(512) DEFAULT '',
  `resolution` varchar(512) DEFAULT '',
  `operatingSystem` varchar(512) DEFAULT '',
  `processor` varchar(512) DEFAULT '',
  `refreshRate` varchar(512) DEFAULT '',
  `speakerPower` varchar(512) DEFAULT '',
  `internetConnection` varchar(512) DEFAULT '',
  `wirelessConnectivity` varchar(512) DEFAULT '',
  `usbPorts` varchar(512) DEFAULT '',
  `videoAudioInputPorts` varchar(512) DEFAULT '',
  `releaseYear` int(11) DEFAULT NULL,
  `warrantyPeriod` varchar(512) DEFAULT '',
  `itemGift` varchar(512) DEFAULT '',
  `crawlDate` int(11) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `manufacturer_id` (`manufacturer_id`),
  KEY `tech_image_id` (`tech_image_id`),
  KEY `tech_audio_id` (`tech_audio_id`),
  KEY `name_id` (`name_id`),
  KEY `crawlDate` (`crawlDate`),
  CONSTRAINT `fact_product_ibfk_1` FOREIGN KEY (`manufacturer_id`) REFERENCES `dim_manufacturer` (`manufacturer_id`),
  CONSTRAINT `fact_product_ibfk_2` FOREIGN KEY (`tech_image_id`) REFERENCES `dim_tech_image` (`tech_image_id`),
  CONSTRAINT `fact_product_ibfk_3` FOREIGN KEY (`tech_audio_id`) REFERENCES `dim_tech_audio` (`tech_audio_id`),
  CONSTRAINT `fact_product_ibfk_4` FOREIGN KEY (`name_id`) REFERENCES `dim_name` (`name_id`),
  CONSTRAINT `fact_product_ibfk_5` FOREIGN KEY (`crawlDate`) REFERENCES `date_dimension` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_audio_technology_summary
CREATE TABLE IF NOT EXISTS `product_audio_technology_summary` (
  `tech_audio_id` int(11) NOT NULL,
  `tech_audio_name` varchar(255) NOT NULL,
  `product_count` int(11) DEFAULT 0,
  PRIMARY KEY (`tech_audio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_avg_price
CREATE TABLE IF NOT EXISTS `product_avg_price` (
  `avg_price_id` int(11) NOT NULL AUTO_INCREMENT,
  `average_price` decimal(15,2) DEFAULT 0.00,
  PRIMARY KEY (`avg_price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_count_by_manufacturer
CREATE TABLE IF NOT EXISTS `product_count_by_manufacturer` (
  `manufacturer_id` int(11) NOT NULL,
  `manufacturer_name` varchar(255) NOT NULL,
  `product_count` int(11) DEFAULT 0,
  PRIMARY KEY (`manufacturer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_image_technology_summary
CREATE TABLE IF NOT EXISTS `product_image_technology_summary` (
  `tech_image_id` int(11) NOT NULL,
  `tech_image_name` varchar(512) NOT NULL,
  `product_count` int(11) DEFAULT 0,
  PRIMARY KEY (`tech_image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_price_summary
CREATE TABLE IF NOT EXISTS `product_price_summary` (
  `price_summary_id` int(11) NOT NULL AUTO_INCREMENT,
  `highest_price` decimal(15,2) DEFAULT 0.00,
  `lowest_price` decimal(15,2) DEFAULT 0.00,
  `highest_price_product_name` varchar(255) NOT NULL,
  `lowest_price_product_name` varchar(255) NOT NULL,
  PRIMARY KEY (`price_summary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.product_total_count
CREATE TABLE IF NOT EXISTS `product_total_count` (
  `total_count_id` int(11) NOT NULL AUTO_INCREMENT,
  `total_product_count` int(11) DEFAULT 0,
  PRIMARY KEY (`total_count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table data_mart.raw_tivi_data
CREATE TABLE IF NOT EXISTS `raw_tivi_data` (
  `sk` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_id` bigint(20) NOT NULL DEFAULT 0,
  `product_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT '',
  `price` decimal(15,2) DEFAULT 0.00,
  `oldPrice` decimal(15,2) DEFAULT 0.00,
  `imgLink` varchar(255) DEFAULT '',
  `discountPercent` int(11) DEFAULT 0,
  `productLink` varchar(255) DEFAULT '',
  `screenSize` varchar(255) DEFAULT '',
  `resolution` varchar(255) DEFAULT '',
  `operatingSystem` varchar(255) DEFAULT '',
  `imageTechnology` text DEFAULT '',
  `processor` varchar(255) DEFAULT '',
  `refreshRate` varchar(255) DEFAULT '',
  `speakerPower` varchar(255) DEFAULT '',
  `internetConnection` varchar(255) DEFAULT '',
  `wirelessConnectivity` varchar(255) DEFAULT '',
  `usbPorts` varchar(255) DEFAULT '',
  `videoAudioInputPorts` varchar(255) DEFAULT '',
  `manufacturer` varchar(255) DEFAULT '',
  `manufacturedIn` varchar(255) DEFAULT '',
  `releaseYear` int(4) DEFAULT NULL,
  `screenType` varchar(255) DEFAULT '',
  `audioOutputPorts` varchar(255) DEFAULT '',
  `warrantyPeriod` varchar(255) DEFAULT '',
  `itemGift` varchar(255) DEFAULT '',
  `tiviType` varchar(255) DEFAULT '',
  `hdr` varchar(255) DEFAULT '',
  `soundTechnology` varchar(255) DEFAULT '',
  `memory` varchar(255) DEFAULT '',
  `voiceSearch` varchar(255) DEFAULT '',
  `crawlDate` int(11) DEFAULT NULL,
  `expired` date DEFAULT '9999-12-30',
  `isDelete` int(11) DEFAULT 0,
  `deleteTime` datetime DEFAULT NULL,
  `changeTime` datetime DEFAULT NULL,
  PRIMARY KEY (`sk`),
  KEY `FK_data_tivi_file_config` (`source_id`),
  KEY `FK_data_tivi_date_dimension` (`crawlDate`)
) ENGINE=InnoDB AUTO_INCREMENT=685 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for procedure data_mart.update_dim
DELIMITER //
CREATE PROCEDURE `update_dim`()
BEGIN
	-- 4.5.1: Cập nhật dim_name
	INSERT INTO dim_name (name_value)
   SELECT DISTINCT name
   FROM raw_tivi_data
   ON DUPLICATE KEY UPDATE
        name_value = VALUES(name_value);
	-- 4.5.2: Cập nhật dim_tech_audio
	INSERT INTO dim_tech_audio (audio_technology_name)
   SELECT DISTINCT soundTechnology
   FROM raw_tivi_data
   ON DUPLICATE KEY UPDATE
        audio_technology_name = VALUES(audio_technology_name);
   
   -- 4.5.3: Cập nhật dim_tech_image
   INSERT INTO dim_tech_image (image_technology_name)
   SELECT DISTINCT imageTechnology
   FROM raw_tivi_data
   ON DUPLICATE KEY UPDATE
        image_technology_name = VALUES(image_technology_name);
        
   -- 4.5.4: Cập nhật dim_manufacturer
    INSERT INTO dim_manufacturer (manufacturer_name)
    SELECT DISTINCT manufacturer
    FROM raw_tivi_data
    ON DUPLICATE KEY UPDATE
        manufacturer_name = VALUES(manufacturer_name);
END//
DELIMITER ;

-- Dumping structure for procedure data_mart.update_fact_product
DELIMITER //
CREATE PROCEDURE `update_fact_product`()
BEGIN
    -- Chèn dữ liệu từ bảng raw_tivi_data vào bảng fact_product, sử dụng ON DUPLICATE KEY UPDATE
    INSERT INTO fact_product (
        name_id, product_id, manufacturer_id, tech_image_id, tech_audio_id, price, oldPrice, 
        discountPercent, screenSize, resolution, operatingSystem, processor, refreshRate, 
        speakerPower, internetConnection, wirelessConnectivity, usbPorts, videoAudioInputPorts, 
        releaseYear, warrantyPeriod, itemGift, crawlDate
    )
    SELECT DISTINCT
        n.name_id,
        p.product_id,
        m.manufacturer_id,
        ti.tech_image_id,
        ta.tech_audio_id,
        p.price,
        p.oldPrice,
        p.discountPercent,
        p.screenSize,
        p.resolution,
        p.operatingSystem,
        p.processor,
        p.refreshRate,
        p.speakerPower,
        p.internetConnection,
        p.wirelessConnectivity,
        p.usbPorts,
        p.videoAudioInputPorts,
        p.releaseYear,
        p.warrantyPeriod,
        p.itemGift,
        p.crawlDate  -- Chỉ lấy crawlDate từ raw_tivi_data
    FROM raw_tivi_data p
    JOIN dim_manufacturer m ON p.manufacturer = m.manufacturer_name
    JOIN dim_tech_image ti ON p.imageTechnology = ti.image_technology_name
    JOIN dim_tech_audio ta ON p.soundTechnology = ta.audio_technology_name
    JOIN dim_name n ON p.name = n.name_value
    JOIN date_dimension dd ON p.crawlDate = dd.id
    ON DUPLICATE KEY UPDATE
        price = VALUES(price),
        oldPrice = VALUES(oldPrice),
        discountPercent = VALUES(discountPercent),
        screenSize = VALUES(screenSize),
        resolution = VALUES(resolution),
        operatingSystem = VALUES(operatingSystem),
        processor = VALUES(processor),
        refreshRate = VALUES(refreshRate),
        speakerPower = VALUES(speakerPower),
        internetConnection = VALUES(internetConnection),
        wirelessConnectivity = VALUES(wirelessConnectivity),
        usbPorts = VALUES(usbPorts),
        videoAudioInputPorts = VALUES(videoAudioInputPorts),
        releaseYear = VALUES(releaseYear),
        warrantyPeriod = VALUES(warrantyPeriod),
        itemGift = VALUES(itemGift),
        crawlDate = VALUES(crawlDate);
END//
DELIMITER ;

-- Dumping structure for procedure data_mart.update_raw_tivi_data
DELIMITER //
CREATE PROCEDURE `update_raw_tivi_data`()
BEGIN
    INSERT INTO raw_tivi_data (
        product_id, manufacturer, imageTechnology, soundTechnology, name, price, oldPrice, 
        discountPercent, screenSize, resolution, operatingSystem, processor, refreshRate, 
        speakerPower, internetConnection, wirelessConnectivity, usbPorts, videoAudioInputPorts, 
        releaseYear, warrantyPeriod, itemGift, crawlDate
    )
    SELECT 
        product_id, manufacturer, imageTechnology, soundTechnology, name, price, oldPrice, 
        discountPercent, screenSize, resolution, operatingSystem, processor, refreshRate, 
        speakerPower, internetConnection, wirelessConnectivity, usbPorts, videoAudioInputPorts, 
        releaseYear, warrantyPeriod, itemGift, crawlDate
    FROM data_warehouse.data_tivi
    ON DUPLICATE KEY UPDATE 
        manufacturer = VALUES(manufacturer),
        imageTechnology = VALUES(imageTechnology),
        soundTechnology = VALUES(soundTechnology),
        name = VALUES(name),
        price = VALUES(price),
        oldPrice = VALUES(oldPrice),
        discountPercent = VALUES(discountPercent),
        screenSize = VALUES(screenSize),
        resolution = VALUES(resolution),
        operatingSystem = VALUES(operatingSystem),
        processor = VALUES(processor),
        refreshRate = VALUES(refreshRate),
        speakerPower = VALUES(speakerPower),
        internetConnection = VALUES(internetConnection),
        wirelessConnectivity = VALUES(wirelessConnectivity),
        usbPorts = VALUES(usbPorts),
        videoAudioInputPorts = VALUES(videoAudioInputPorts),
        releaseYear = VALUES(releaseYear),
        warrantyPeriod = VALUES(warrantyPeriod),
        itemGift = VALUES(itemGift),
        crawlDate = VALUES(crawlDate);
END//
DELIMITER ;

-- Dumping structure for procedure data_mart.update_statistical
DELIMITER //
CREATE PROCEDURE `update_statistical`()
BEGIN
    -- Variable declarations
    DECLARE avg_price DECIMAL(15,2);
    DECLARE highest_price DECIMAL(15,2);
    DECLARE lowest_price DECIMAL(15,2);
    DECLARE highest_price_product_name VARCHAR(255);
    DECLARE lowest_price_product_name VARCHAR(255);

    -- 4.6.1: Cập nhật product_audio_technology_summary
    INSERT INTO product_audio_technology_summary (tech_audio_id, tech_audio_name, product_count)
    SELECT ta.tech_audio_id, ta.audio_technology_name, COUNT(*) AS product_count
    FROM fact_product fp
    JOIN dim_tech_audio ta ON fp.tech_audio_id = ta.tech_audio_id
    GROUP BY fp.tech_audio_id
    ON DUPLICATE KEY UPDATE product_count = VALUES(product_count), tech_audio_name = VALUES(tech_audio_name);

    -- 4.6.2: Lấy giá trị trung bình
    SELECT AVG(price) INTO avg_price
    FROM raw_tivi_data;

    -- Cập nhật vào bảng `product_avg_price`
    INSERT INTO product_avg_price (average_price)
    VALUES (avg_price)
    ON DUPLICATE KEY UPDATE average_price = VALUES(average_price);
   
    -- 4.6.3: Cập nhật product_count_by_manufacturer
    INSERT INTO product_count_by_manufacturer (manufacturer_id, manufacturer_name, product_count)
    SELECT f.manufacturer_id, m.manufacturer_name, COUNT(*) AS product_count
    FROM fact_product f
    JOIN dim_manufacturer m ON f.manufacturer_id = m.manufacturer_id
    GROUP BY f.manufacturer_id
    ON DUPLICATE KEY UPDATE product_count = VALUES(product_count);
        
    -- 4.6.4: Cập nhật product_image_technology_summary
    INSERT INTO product_image_technology_summary (tech_image_id, tech_image_name, product_count)
    SELECT ti.tech_image_id, ti.image_technology_name, COUNT(*) AS product_count
    FROM fact_product fp
    JOIN dim_tech_image ti ON fp.tech_image_id = ti.tech_image_id
    GROUP BY fp.tech_image_id
    ON DUPLICATE KEY UPDATE product_count = VALUES(product_count);
    
    -- 4.6.5: Lấy giá trị cao nhất và thấp nhất
    SELECT MAX(price), MIN(price) INTO highest_price, lowest_price
    FROM raw_tivi_data;

    -- Lấy tên sản phẩm có giá cao nhất và thấp nhất
    SELECT name INTO highest_price_product_name
    FROM raw_tivi_data
    WHERE price = highest_price
    LIMIT 1;

    SELECT name INTO lowest_price_product_name
    FROM raw_tivi_data
    WHERE price = lowest_price
    LIMIT 1;

    -- Cập nhật vào bảng `product_price_summary`
    INSERT INTO product_price_summary (highest_price, lowest_price, highest_price_product_name, lowest_price_product_name)
    VALUES (highest_price, lowest_price, highest_price_product_name, lowest_price_product_name)
    ON DUPLICATE KEY UPDATE
        highest_price = VALUES(highest_price),
        lowest_price = VALUES(lowest_price),
        highest_price_product_name = VALUES(highest_price_product_name),
        lowest_price_product_name = VALUES(lowest_price_product_name);
        
    -- 4.6.6: Cập nhật product_total_count
    INSERT INTO product_total_count (total_product_count)
    SELECT COUNT(*) AS total_product_count
    FROM fact_product
    ON DUPLICATE KEY UPDATE total_product_count = VALUES(total_product_count);
END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
