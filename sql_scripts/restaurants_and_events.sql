-- Create and use dinnerdates

CREATE DATABASE dinnerdates;
USE dinnerdates;

-- dinnerdates.Event definition

CREATE TABLE `Event` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `event_date` date NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.Restaurant definition

CREATE TABLE `Restaurant` (
  `restaurant_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `time_open` varchar(500) NOT NULL,
  PRIMARY KEY (`restaurant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.`User` definition

CREATE TABLE `User` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `User_UN` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.MenuItem definition

CREATE TABLE `MenuItem` (
  `menu_item_id` int NOT NULL AUTO_INCREMENT,
  `restaurant_id` int NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`menu_item_id`),
  KEY `MenuItem_ibfk_1` (`restaurant_id`),
  CONSTRAINT `MenuItem_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `Restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.Student definition

CREATE TABLE `Student` (
  `user_id` int NOT NULL,
  `year` int NOT NULL,
  `major` varchar(100) NOT NULL,
  `interest` varchar(100) NOT NULL,
  `favorite_restaurant` int DEFAULT NULL,
  `preferred_food` varchar(200) DEFAULT NULL,
  `availability` varchar(200) DEFAULT NULL,
  `favorite_eating_time` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `Student_ibfk_2` (`favorite_restaurant`),
  CONSTRAINT `Student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `Student_ibfk_2` FOREIGN KEY (`favorite_restaurant`) REFERENCES `Restaurant` (`restaurant_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.EventAttendant definition

CREATE TABLE `EventAttendant` (
  `attendant_id` int NOT NULL,
  `event_id` int NOT NULL,
  PRIMARY KEY (`attendant_id`,`event_id`),
  KEY `EventAttendant_ibfk_2` (`event_id`),
  CONSTRAINT `EventAttendant_ibfk_1` FOREIGN KEY (`attendant_id`) REFERENCES `Student` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `EventAttendant_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `Event` (`event_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- dinnerdates.Review definition

CREATE TABLE `Review` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `menu_item_id` int NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `date_submitted` date NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `Review_ibfk_1` (`student_id`),
  KEY `Review_ibfk_2` (`menu_item_id`),
  CONSTRAINT `Review_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `Student` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `Review_ibfk_2` FOREIGN KEY (`menu_item_id`) REFERENCES `MenuItem` (`menu_item_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

