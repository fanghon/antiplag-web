/*
Navicat MySQL Data Transfer

Source Server         : mysql8
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : jplag

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2025-04-28 09:10:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `name` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('fh', '123');
