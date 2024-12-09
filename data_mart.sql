-- Tạo cơ sở dữ liệu data_mart
CREATE DATABASE IF NOT EXISTS data_mart /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE data_mart;

-- Tạo bảng raw_tivi_data trong data_mart
CREATE TABLE IF NOT EXISTS data_mart.raw_tivi_data LIKE data_warehouse.data_tivi;

-- Sao chép dữ liệu từ data_warehouse.data_tivi sang data_mart.raw_tivi_data
INSERT INTO data_mart.raw_tivi_data
SELECT * FROM data_warehouse.data_tivi;

-- Tạo bảng date_dimension trong data_mart
CREATE TABLE IF NOT EXISTS data_mart.date_dimension LIKE data_warehouse.date_dimension;

-- Sao chép dữ liệu từ data_warehouse.data_tivi sang data_mart.raw_tivi_data
INSERT INTO data_mart.date_dimension
SELECT * FROM data_warehouse.date_dimension;

-- =======================================
-- Bảng Dimension: Dim_Manufacturer (Thương hiệu sản phẩm)
-- =======================================
CREATE TABLE IF NOT EXISTS dim_manufacturer (
  manufacturer_id INT AUTO_INCREMENT PRIMARY KEY,
  manufacturer_name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Chèn dữ liệu vào bảng dim_manufacturer từ raw_tivi_data
INSERT INTO dim_manufacturer (manufacturer_name)
SELECT DISTINCT manufacturer FROM raw_tivi_data;

-- =======================================
-- Bảng Dimension: Dim_Tech_Image (Công nghệ hình ảnh)
-- =======================================
CREATE TABLE IF NOT EXISTS dim_tech_image (
  tech_image_id INT AUTO_INCREMENT PRIMARY KEY,
  image_technology_name VARCHAR(512) NOT NULL  -- Đã tăng kích thước cột lên 512 ký tự
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Chèn dữ liệu vào bảng dim_tech_image từ raw_tivi_data
INSERT INTO dim_tech_image (image_technology_name)
SELECT DISTINCT imageTechnology FROM raw_tivi_data;

-- =======================================
-- Bảng Dimension: Dim_Tech_Audio (Công nghệ âm thanh)
-- =======================================
CREATE TABLE IF NOT EXISTS dim_tech_audio (
  tech_audio_id INT AUTO_INCREMENT PRIMARY KEY,
  audio_technology_name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
             

-- Chèn dữ liệu vào bảng dim_tech_audio từ raw_tivi_data
INSERT INTO dim_tech_audio (audio_technology_name)
SELECT DISTINCT soundTechnology FROM raw_tivi_data;

-- =======================================
-- Bảng Dim: Dim_Name (Danh mục tên sản phẩm)
-- =======================================
CREATE TABLE IF NOT EXISTS dim_name (
  name_id INT AUTO_INCREMENT PRIMARY KEY,  -- Khóa chính tự động tăng
  name_value VARCHAR(512) NOT NULL         -- Tên sản phẩm
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Chèn dữ liệu vào bảng dim_name từ bảng raw_tivi_data
INSERT INTO dim_name (name_value)
SELECT name FROM raw_tivi_data;

CREATE TABLE IF NOT EXISTS fact_product (
  name_id INT DEFAULT NULL,  -- Đưa cột name_id lên đầu tiên
  product_id BIGINT NOT NULL,
  manufacturer_id INT NOT NULL,
  tech_image_id INT NOT NULL,
  tech_audio_id INT NOT NULL,
  price DECIMAL(18,2) NOT NULL,  -- Tăng độ chính xác của price
  oldPrice DECIMAL(18,2) DEFAULT 0.00,  -- Tăng độ chính xác của oldPrice
  discountPercent INT DEFAULT 0,
  screenSize VARCHAR(512) DEFAULT '',  -- Tăng độ dài của screenSize
  resolution VARCHAR(512) DEFAULT '',  -- Tăng độ dài của resolution
  operatingSystem VARCHAR(512) DEFAULT '',  -- Tăng độ dài của operatingSystem
  processor VARCHAR(512) DEFAULT '',  -- Tăng độ dài của processor
  refreshRate VARCHAR(512) DEFAULT '',  -- Tăng độ dài của refreshRate
  speakerPower VARCHAR(512) DEFAULT '',  -- Tăng độ dài của speakerPower
  internetConnection VARCHAR(512) DEFAULT '',  -- Tăng độ dài của internetConnection
  wirelessConnectivity VARCHAR(512) DEFAULT '',  -- Tăng độ dài của wirelessConnectivity
  usbPorts VARCHAR(512) DEFAULT '',  -- Tăng độ dài của usbPorts
  videoAudioInputPorts VARCHAR(512) DEFAULT '',  -- Tăng độ dài của videoAudioInputPorts
  releaseYear INT DEFAULT NULL,
  warrantyPeriod VARCHAR(512) DEFAULT '',  -- Tăng độ dài của warrantyPeriod
  itemGift VARCHAR(512) DEFAULT '',  -- Tăng độ dài của itemGift
  crawlDate INT,  -- Cột crawlDate lưu trữ ID từ bảng date_dimension
  PRIMARY KEY (product_id),
  FOREIGN KEY (manufacturer_id) REFERENCES dim_manufacturer(manufacturer_id),
  FOREIGN KEY (tech_image_id) REFERENCES dim_tech_image(tech_image_id),
  FOREIGN KEY (tech_audio_id) REFERENCES dim_tech_audio(tech_audio_id),
  FOREIGN KEY (name_id) REFERENCES dim_name(name_id),  -- Khóa ngoại tới dim_name
  FOREIGN KEY (crawlDate) REFERENCES date_dimension(id)  -- Khóa ngoại tới date_dimension (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO fact_product (
    name_id, product_id, manufacturer_id, tech_image_id, tech_audio_id, price, oldPrice, discountPercent, 
    screenSize, resolution, operatingSystem, processor, refreshRate, speakerPower, internetConnection, 
    wirelessConnectivity, usbPorts, videoAudioInputPorts, releaseYear, warrantyPeriod, itemGift, crawlDate
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


-- =======================================
-- Bảng Thống kê Sản phẩm theo Hãng
-- =======================================
CREATE TABLE IF NOT EXISTS product_count_by_manufacturer (
  manufacturer_id INT PRIMARY KEY,
  manufacturer_name VARCHAR(255) NOT NULL,
  product_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cập nhật bảng product_count_by_manufacturer từ fact_product
INSERT INTO product_count_by_manufacturer (manufacturer_id, manufacturer_name, product_count)
SELECT f.manufacturer_id, m.manufacturer_name, COUNT(*) AS product_count
FROM fact_product f
JOIN dim_manufacturer m ON f.manufacturer_id = m.manufacturer_id
GROUP BY f.manufacturer_id;

-- =======================================
-- Bảng Thống kê Giá Cao Nhất, Thấp Nhất và Tên Sản Phẩm
-- =======================================
CREATE TABLE IF NOT EXISTS product_price_summary (
  price_summary_id INT AUTO_INCREMENT PRIMARY KEY,
  highest_price DECIMAL(15,2) DEFAULT 0.00,
  lowest_price DECIMAL(15,2) DEFAULT 0.00,
  highest_price_product_name VARCHAR(255) NOT NULL,
  lowest_price_product_name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Bước 1: Lấy giá trị cao nhất và thấp nhất của sản phẩm
SELECT 
  MAX(price) AS highest_price, 
  MIN(price) AS lowest_price
FROM raw_tivi_data;

-- Bước 2: Cập nhật bảng `product_price_summary` với tên sản phẩm tương ứng
-- Cập nhật giá trị cao nhất và thấp nhất vào bảng `product_price_summary`
INSERT INTO product_price_summary (highest_price, lowest_price, highest_price_product_name, lowest_price_product_name)
SELECT 
  highest_price,  -- Giá trị cao nhất
  lowest_price,   -- Giá trị thấp nhất
  (SELECT name FROM raw_tivi_data WHERE price = highest_price LIMIT 1) AS highest_price_product_name,  -- Tên sản phẩm có giá cao nhất
  (SELECT name FROM raw_tivi_data WHERE price = lowest_price LIMIT 1) AS lowest_price_product_name  -- Tên sản phẩm có giá thấp nhất
FROM (
  SELECT 
    MAX(price) AS highest_price, 
    MIN(price) AS lowest_price
  FROM raw_tivi_data
) AS price_summary;



-- =======================================
-- Bảng Thống kê Giá Trung Bình
-- =======================================
CREATE TABLE IF NOT EXISTS product_avg_price (
  avg_price_id INT AUTO_INCREMENT PRIMARY KEY,
  average_price DECIMAL(15,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cập nhật bảng product_avg_price
INSERT INTO product_avg_price (average_price)
SELECT AVG(price) AS average_price
FROM raw_tivi_data;

-- =======================================
-- Bảng Thống kê Sản phẩm theo Công Nghệ Hình Ảnh
-- =======================================
CREATE TABLE IF NOT EXISTS product_image_technology_summary (
  tech_image_id INT PRIMARY KEY,
  tech_image_name VARCHAR(512) NOT NULL,
  product_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cập nhật bảng product_image_technology_summary
INSERT INTO product_image_technology_summary (tech_image_id, tech_image_name, product_count)
SELECT ti.tech_image_id, ti.image_technology_name, COUNT(*) AS product_count
FROM fact_product fp
JOIN dim_tech_image ti ON fp.tech_image_id = ti.tech_image_id
GROUP BY fp.tech_image_id;

-- =======================================
-- Bảng Thống kê Sản phẩm theo Công Nghệ Âm Thanh
-- =======================================
CREATE TABLE IF NOT EXISTS product_audio_technology_summary (
  tech_audio_id INT PRIMARY KEY,
  tech_audio_name VARCHAR(255) NOT NULL,
  product_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cập nhật bảng product_audio_technology_summary
INSERT INTO product_audio_technology_summary (tech_audio_id, tech_audio_name, product_count)
SELECT ta.tech_audio_id, ta.audio_technology_name, COUNT(*) AS product_count
FROM fact_product fp
JOIN dim_tech_audio ta ON fp.tech_audio_id = ta.tech_audio_id
GROUP BY fp.tech_audio_id;

-- =======================================
-- Bảng Thống Kê Tổng Số Lượng Sản Phẩm
-- =======================================
CREATE TABLE IF NOT EXISTS product_total_count (
  total_count_id INT AUTO_INCREMENT PRIMARY KEY,
  total_product_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cập nhật tổng số lượng sản phẩm vào bảng product_total_count
INSERT INTO product_total_count (total_product_count)
SELECT COUNT(*) AS total_product_count
FROM fact_product;

-- Cập nhật lại tổng số lượng sản phẩm mỗi khi cần
UPDATE product_total_count
SET total_product_count = (SELECT COUNT(*) FROM fact_product)
WHERE total_count_id = 1;



