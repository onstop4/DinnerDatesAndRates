
  
  CREATE TABLE `sns`.`user_friend` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sourceId` BIGINT NOT NULL,
  `targetId` BIGINT NOT NULL,
  `type` SMALLINT NOT NULL DEFAULT 0,
  `status` SMALLINT NOT NULL DEFAULT 0,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL DEFAULT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_friend_source` (`sourceId` ASC),
  CONSTRAINT `fk_friend_source`
    FOREIGN KEY (`sourceId`)
    REFERENCES `sns`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `sns`.`user_friend` 
ADD INDEX `idx_friend_target` (`targetId` ASC);
ALTER TABLE `sns`.`user_friend` 
ADD CONSTRAINT `fk_friend_target`
  FOREIGN KEY (`targetId`)
  REFERENCES `sns`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `sns`.`user_friend` ADD UNIQUE `uq_friend`(`sourceId`, `targetId`);

CREATE TABLE `sns`.`user_follower` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sourceId` BIGINT NOT NULL,
  `targetId` BIGINT NOT NULL,
  `type` SMALLINT NOT NULL DEFAULT 0,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_ufollower_source` (`sourceId` ASC),
  CONSTRAINT `fk_ufollower_source`
    FOREIGN KEY (`sourceId`)
    REFERENCES `sns`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `sns`.`user_follower` 
ADD INDEX `idx_ufollower_target` (`targetId` ASC);
ALTER TABLE `sns`.`user_follower` 
ADD CONSTRAINT `fk_ufollower_target`
  FOREIGN KEY (`targetId`)
  REFERENCES `sns`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `sns`.`user_follower` ADD UNIQUE `uq_ufollower`(`sourceId`, `targetId`, `type`);

CREATE TABLE `sns`.`user_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sourceId` BIGINT NOT NULL,
  `targetId` BIGINT NOT NULL,
  `message` TINYTEXT NULL DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_umessage_source` (`sourceId` ASC),
  CONSTRAINT `fk_umessage_source`
    FOREIGN KEY (`sourceId`)
    REFERENCES `sns`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `sns`.`user_message` 
ADD INDEX `idx_umessage_target` (`targetId` ASC);
ALTER TABLE `sns`.`user_message` 
ADD CONSTRAINT `fk_umessage_target`
  FOREIGN KEY (`targetId`)
  REFERENCES `sns`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  CREATE TABLE `sns`.`user_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `userId` BIGINT NOT NULL,
  `senderId` BIGINT NOT NULL,
  `message` TINYTEXT NULL DEFAULT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_upost_user` (`userId` ASC),
  CONSTRAINT `fk_upost_user`
    FOREIGN KEY (`userId`)
    REFERENCES `sns`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `sns`.`user_post` 
ADD INDEX `idx_upost_sender` (`senderId` ASC);
ALTER TABLE `sns`.`user_post` 
ADD CONSTRAINT `fk_upost_sender`
  FOREIGN KEY (`senderId`)
  REFERENCES `sns`.`user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;