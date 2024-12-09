-- --------------------------------------------------------
-- Host:                         192.168.56.1
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


-- Dumping database structure for control
CREATE DATABASE IF NOT EXISTS `control` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `control`;

-- Dumping structure for table control.file_config
CREATE TABLE IF NOT EXISTS `file_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  `file_location` varchar(255) NOT NULL DEFAULT '',
  `staging_table` varchar(255) NOT NULL DEFAULT '',
  `dw_procedure` varchar(255) DEFAULT '',
  `file_name_format` varchar(255) NOT NULL DEFAULT '',
  `createdAt` date NOT NULL DEFAULT curdate(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table control.file_config: ~2 rows (approximately)
INSERT INTO `file_config` (`id`, `name`, `address`, `file_location`, `staging_table`, `dw_procedure`, `file_name_format`, `createdAt`) VALUES
	(1, 'dmx_tivi', 'https://www.dienmayxanh.com', 'Staging/data/DMX', 'stg_dmx_data', 'loadDMXData()', '%s/DMXdata_%s.csv', '2024-10-29'),
	(2, 'nk_tivi', 'https://www.nguyenkim.com', 'Staging/data/NK', 'stg_nk_data', 'loadNKData()', '%s/NKdata_%s.csv', '2024-10-29');

-- Dumping structure for table control.file_log
CREATE TABLE IF NOT EXISTS `file_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_id` bigint(20) NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT '0',
  `createdAt` date NOT NULL DEFAULT curdate(),
  `file_data` varchar(255) NOT NULL DEFAULT 'crawling',
  `updatedAt` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `reloadedAt` timestamp NULL DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  `file_size_kb` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FK__file_config` (`config_id`),
  CONSTRAINT `FK__file_config` FOREIGN KEY (`config_id`) REFERENCES `file_config` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table control.file_log: ~6 rows (approximately)
INSERT INTO `file_log` (`id`, `config_id`, `status`, `createdAt`, `file_data`, `updatedAt`, `reloadedAt`, `count`, `file_size_kb`) VALUES
	(13, 1, 'LMS', '2024-11-05', 'DMXdata_2024-11-05.csv', '2024-12-09 06:20:17', NULL, 209, 201),
	(14, 2, 'LMS', '2024-11-05', 'NKdata_2024-11-05.csv', '2024-12-09 06:18:52', NULL, 222, 181),
	(17, 1, 'LMS', '2024-12-01', 'DMXdata_2024-12-01.csv', '2024-12-09 06:19:45', NULL, 218, 189),
	(18, 2, 'LMS', '2024-12-01', 'NKdata_2024-12-01.csv', '2024-12-09 06:18:51', NULL, 222, 181),
	(32, 1, 'LDS', '2024-12-09', 'DMXdata_2024-12-09.csv', '2024-12-09 09:07:12', NULL, 211, 181),
	(33, 2, 'ER', '2024-12-09', 'crawling', '2024-12-09 09:07:12', NULL, 0, 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
