-- phpMyAdmin SQL Dump
-- version 2.10.1
-- http://www.phpmyadmin.net
-- 
-- ����: localhost
-- ��������: 2009 �� 07 �� 15 �� 08:58
-- �������汾: 5.0.37
-- PHP �汾: 5.2.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- ���ݿ�: `reyom`
-- 

-- --------------------------------------------------------

-- 
-- ��Ľṹ `authorities`
-- 

CREATE TABLE `authorities` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(20) NOT NULL,
  `DISPLAY_NAME` varchar(20) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- 
-- �������е����� `authorities`
-- 

INSERT INTO `authorities` (`ID`, `NAME`, `DISPLAY_NAME`) VALUES 
(1, 'A_VIEW_USER', '�鿴�û�'),
(2, 'A_MODIFY_USER', '�����û�'),
(3, 'A_VIEW_ROLE', '�鿴��ɫ'),
(4, 'A_MODIFY_ROLE', '�����ɫ');

-- --------------------------------------------------------

-- 
-- ��Ľṹ `resources`
-- 

CREATE TABLE `resources` (
  `ID` int(11) NOT NULL auto_increment,
  `RESOURCE_TYPE` varchar(20) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  `ORDER_NUM` float NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- 
-- �������е����� `resources`
-- 

INSERT INTO `resources` (`ID`, `RESOURCE_TYPE`, `VALUE`, `ORDER_NUM`) VALUES 
(1, 'url', '/security/user!save*', 1),
(2, 'url', '/security/user!delete*', 2),
(3, 'url', '/security/user*', 3),
(4, 'url', '/security/role!save*', 4),
(5, 'url', '/security/role!delete*', 5),
(6, 'url', '/security/role*', 6);

-- --------------------------------------------------------

-- 
-- ��Ľṹ `resources_authorities`
-- 

CREATE TABLE `resources_authorities` (
  `AUTHORITY_ID` int(11) NOT NULL,
  `RESOURCE_ID` int(11) NOT NULL,
  KEY `AUTHORITY_ID` (`AUTHORITY_ID`),
  KEY `RESOURCE_ID` (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 
-- �������е����� `resources_authorities`
-- 

INSERT INTO `resources_authorities` (`AUTHORITY_ID`, `RESOURCE_ID`) VALUES 
(2, 1),
(2, 2),
(1, 3),
(4, 4),
(4, 5),
(3, 6);

-- --------------------------------------------------------

-- 
-- ��Ľṹ `roles`
-- 

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `description` varchar(64) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- 
-- �������е����� `roles`
-- 

INSERT INTO `roles` (`id`, `name`, `description`) VALUES 
(-2, 'ROLE_USER', 'Default role for all Users'),
(-1, 'ROLE_ADMIN', 'Administrator role (can edit Users)');

-- --------------------------------------------------------

-- 
-- ��Ľṹ `roles_authorities`
-- 

CREATE TABLE `roles_authorities` (
  `ROLE_ID` bigint(20) NOT NULL,
  `AUTHORITY_ID` int(11) NOT NULL,
  KEY `ROLE_ID` (`ROLE_ID`),
  KEY `AUTHORITY_ID` (`AUTHORITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 
-- �������е����� `roles_authorities`
-- 

INSERT INTO `roles_authorities` (`ROLE_ID`, `AUTHORITY_ID`) VALUES 
(-1, 1),
(-1, 2),
(-1, 3),
(-1, 4),
(-2, 1),
(-2, 3);

-- --------------------------------------------------------

-- 
-- ��Ľṹ `users`
-- 

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL auto_increment,
  `address` varchar(150) default NULL,
  `country` varchar(100) default NULL,
  `city` varchar(50) NOT NULL,
  `province` varchar(100) default NULL,
  `postal_code` varchar(15) NOT NULL,
  `version` int(11) default NULL,
  `account_enabled` bit(1) default NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hint` varchar(255) default NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_number` varchar(255) default NULL,
  `website` varchar(255) default NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `credentials_expired` bit(1) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- 
-- �������е����� `users`
-- 

INSERT INTO `users` (`id`, `address`, `country`, `city`, `province`, `postal_code`, `version`, `account_enabled`, `username`, `password`, `email`, `password_hint`, `first_name`, `last_name`, `phone_number`, `website`, `account_expired`, `account_locked`, `credentials_expired`) VALUES 
(-2, '', 'US', 'Denver', 'CO', '80210', 1, '', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'matt@raibledesigns.com', 'Not a female kitty.', 'Matt', 'Raible', '', 'http://raibledesigns.com', '\0', '\0', '\0'),
(-1, '', 'US', 'Denver', 'CO', '80210', 1, '', 'user', '12dea96fec20593566ab75692c9949596833adc9', 'matt_raible@yahoo.com', 'A male kitty.', 'Tomcat', 'User', '', 'http://tomcat.apache.org', '\0', '\0', '\0');

-- --------------------------------------------------------

-- 
-- ��Ľṹ `users_roles`
-- 

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`role_id`),
  KEY `FKF6CCD9C6B1F15A` (`role_id`),
  KEY `FKF6CCD9C6A5DCB53A` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 
-- �������е����� `users_roles`
-- 

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES 
(-1, -2),
(-2, -1);

-- 
-- ���Ƶ����ı�
-- 

-- 
-- ���Ʊ� `resources_authorities`
-- 
ALTER TABLE `resources_authorities`
  ADD CONSTRAINT `resources_authorities_ibfk_1` FOREIGN KEY (`AUTHORITY_ID`) REFERENCES `authorities` (`ID`),
  ADD CONSTRAINT `resources_authorities_ibfk_2` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `resources` (`ID`);

-- 
-- ���Ʊ� `roles_authorities`
-- 
ALTER TABLE `roles_authorities`
  ADD CONSTRAINT `roles_authorities_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `roles_authorities_ibfk_2` FOREIGN KEY (`AUTHORITY_ID`) REFERENCES `authorities` (`ID`);

-- 
-- ���Ʊ� `users_roles`
-- 
ALTER TABLE `users_roles`
  ADD CONSTRAINT `FKF6CCD9C6A5DCB53A` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKF6CCD9C6B1F15A` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
