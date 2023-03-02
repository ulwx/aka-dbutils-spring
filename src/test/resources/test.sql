/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.31-log : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
`address_id` int NOT NULL,
`name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
`custormer_id` bigint DEFAULT NULL,
PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `address` */

insert  into `address`(`name`,`address_id`,`custormer_id`) values 
('1',1,10),
('2',2,11),
('3',3,12),
('4',4,13);

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `customer` */

insert  into `customer`(`id`,`first_name`,`dob`) values 
(10,'Albert','1904-05-13'),
(11,'Albert','1904-05-13'),
(12,'Albert','1904-05-13'),
(13,'Albert','1904-05-13'),
(14,'Hans Albert','1904-05-13');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
