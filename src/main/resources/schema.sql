--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

-- CREATES A TABLE NAMED `users` IF A TABLE WITH THE SAME NAME DOESN'T EXIST
-- WILL BE USED TO STORE ADMIN ACCOUNTS
CREATE TABLE IF NOT EXISTS `users` (
	`uuid` INT(11) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`password` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`uuid`) USING BTREE,
	UNIQUE INDEX `UNIQUE` (`username`) USING BTREE
)
COMMENT='authorization/authentication'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

-- CREATES A TABLE NAMED `groups` IF A TABLE WITH THE SAME NAME DOESN'T EXIST
-- WILL BE USED TO STORE GROUP AND PERMISSIONS
CREATE TABLE IF NOT EXISTS `groups` (
	`username` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`user_group` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`permissions` VARCHAR(15) NOT NULL COLLATE 'latin1_swedish_ci',
	UNIQUE INDEX `UNIQUE` (`username`) USING BTREE
)
COMMENT='authorization/authentication'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

-- CREATES A TABLE NAMED `products` IF A TABLE WITH THE SAME NAME DOESN'T EXIST
-- WILL BE USED TO STORE PRODUCT ENTRIES
CREATE TABLE IF NOT EXISTS `products` (
	`prod_id` INT(11) NOT NULL AUTO_INCREMENT,
	`prod_name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`prod_desc` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`prod_price` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`prod_id`) USING BTREE
)
COMMENT='catalog'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;