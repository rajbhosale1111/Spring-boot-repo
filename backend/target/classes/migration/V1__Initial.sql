
CREATE TABLE `user` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NULL,
  `role` ENUM('USER', 'ADMIN') NULL,
  `active` BIT(1) NOT NULL,
  `created_by` INT NULL,
  `created_at` datetime NULL,
  `updated_by` INT NULL,
  `updated_at` datetime NULL,
  `deleted_at` datetime NULL,
  CONSTRAINT `pk_user` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `user` ADD CONSTRAINT `uc_user_email` UNIQUE (`email`);


INSERT INTO `user` (`name`,`email`,`password`,`role`,`active`)
VALUES ('MDTI Admin','admin@at.com','$2a$10$rCSm8epHdCFXEIjkV1a2GuDyJ1rfI6rpvQFf6iacdfGsJaVB5VVom','ADMIN',1);
