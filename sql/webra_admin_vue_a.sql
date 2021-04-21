/*
 Navicat Premium Data Transfer

 Source Server         : lohost
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : webra_admin_vue_a

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 21/04/2021 14:10:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wra_auth
-- ----------------------------
DROP TABLE IF EXISTS `wra_auth`;
CREATE TABLE `wra_auth`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限名',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限字符',
  `super_id` int(20) NOT NULL DEFAULT 0 COMMENT '上级权限id，默认0',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '前端动态路由',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
  `whether` int(2) NULL DEFAULT 0 COMMENT '是否存在下级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_auth
-- ----------------------------
INSERT INTO `wra_auth` VALUES (2, '站点管理', 'site', 0, '/system/site/inform', 'system/site/Inform', 'el-icon-location', 1);
INSERT INTO `wra_auth` VALUES (3, '部门管理', 'department_admin', 0, '/system/department', 'system/department/Department', 'el-icon-s-management', 1);
INSERT INTO `wra_auth` VALUES (4, '用户管理', 'user_admin', 0, '/system/user', 'system/user/User', 'el-icon-user-solid', 1);
INSERT INTO `wra_auth` VALUES (5, '系统监控', 'monitoring_admin', 0, '/system/monitoring', 'system/monitoring/Monitoring', 'el-icon-s-platform', 1);
INSERT INTO `wra_auth` VALUES (6, '本站相关', 'about', 0, '/system/about', 'system/About', 'el-icon-s-flag', 0);
INSERT INTO `wra_auth` VALUES (7, '通知公告', 'inform', 2, '/system/site/inform', 'system/site/Inform', NULL, 0);
INSERT INTO `wra_auth` VALUES (8, '日志详情', 'log', 2, '/system/site/log', 'system/site/Log', NULL, 0);
INSERT INTO `wra_auth` VALUES (9, '部门管理', 'department', 3, '/system/department', 'system/department/Department', NULL, 1);
INSERT INTO `wra_auth` VALUES (10, '岗位管理', 'post', 3, '/system/department/post', 'system/department/Post', NULL, 0);
INSERT INTO `wra_auth` VALUES (11, '用户管理', 'user', 4, '/system/user', 'system/user/User', NULL, 1);
INSERT INTO `wra_auth` VALUES (12, '权限管理', 'auth', 4, '/system/user/auth', 'system/user/Auth', NULL, 0);
INSERT INTO `wra_auth` VALUES (13, '系统状态', 'monitoring', 5, '/system/monitoring', 'system/monitoring/Monitoring', NULL, 0);
INSERT INTO `wra_auth` VALUES (14, '在线用户', 'active', 5, '/system/monitoring/user', 'system/monitoring/ActiveUser', NULL, 0);
INSERT INTO `wra_auth` VALUES (15, '全部部门', 'all_department', 9, NULL, NULL, NULL, 0);
INSERT INTO `wra_auth` VALUES (16, '负责部门', 'responsible_department', 9, NULL, NULL, NULL, 0);
INSERT INTO `wra_auth` VALUES (17, '全部用户', 'all_user', 11, NULL, NULL, NULL, 0);
INSERT INTO `wra_auth` VALUES (18, '部门用户', 'responsible_user', 11, NULL, NULL, NULL, 0);
INSERT INTO `wra_auth` VALUES (19, 'Swagger', 'swagger', 5, '/system/monitoring/swagger', 'system/monitoring/Swagger', NULL, 0);

-- ----------------------------
-- Table structure for wra_department
-- ----------------------------
DROP TABLE IF EXISTS `wra_department`;
CREATE TABLE `wra_department`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门名',
  `super_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级部门id',
  `user_id` int(20) NULL DEFAULT NULL COMMENT '用户id',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '状态，默认0,0关闭，1开启',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  `whether` int(255) NOT NULL DEFAULT 0 COMMENT '是否存在下级0否/1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_department_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_department_user` FOREIGN KEY (`user_id`) REFERENCES `wra_user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_department
-- ----------------------------
INSERT INTO `wra_department` VALUES (1, '北京部门', 0, NULL, 0, '2021-03-20 12:04:02', '2021-04-07 10:41:05', 1);
INSERT INTO `wra_department` VALUES (2, '第一队', 1, NULL, 0, '2021-03-20 12:04:27', '2021-03-20 12:04:27', 1);
INSERT INTO `wra_department` VALUES (3, '上海部门', 0, NULL, 0, '2021-03-23 19:02:20', '2021-03-23 19:02:20', 0);
INSERT INTO `wra_department` VALUES (4, '广东部门', 0, NULL, 0, '2021-03-23 19:02:34', '2021-03-23 19:02:34', 0);
INSERT INTO `wra_department` VALUES (5, '第二队', 1, NULL, 0, '2021-04-06 17:59:21', '2021-04-06 17:59:21', 0);
INSERT INTO `wra_department` VALUES (6, '第一小队', 2, NULL, 0, '2021-04-06 17:59:32', '2021-04-06 17:59:32', 0);
INSERT INTO `wra_department` VALUES (7, '东厂', 0, NULL, 0, '2021-04-07 10:19:06', '2021-04-07 10:19:06', 0);

-- ----------------------------
-- Table structure for wra_inform
-- ----------------------------
DROP TABLE IF EXISTS `wra_inform`;
CREATE TABLE `wra_inform`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公告标题',
  `user_id` int(20) NOT NULL COMMENT '用户id',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '公告类型',
  `text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公告内容',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_inform_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_inform_user` FOREIGN KEY (`user_id`) REFERENCES `wra_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_inform
-- ----------------------------

-- ----------------------------
-- Table structure for wra_log
-- ----------------------------
DROP TABLE IF EXISTS `wra_log`;
CREATE TABLE `wra_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日志名',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `text` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日志详情',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_log
-- ----------------------------

-- ----------------------------
-- Table structure for wra_post
-- ----------------------------
DROP TABLE IF EXISTS `wra_post`;
CREATE TABLE `wra_post`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '岗位名',
  `serial` int(20) NOT NULL DEFAULT 0 COMMENT '排序，默认0，值小靠前',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '状态，0/1,默认0，0开启，1关闭',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_post
-- ----------------------------

-- ----------------------------
-- Table structure for wra_role
-- ----------------------------
DROP TABLE IF EXISTS `wra_role`;
CREATE TABLE `wra_role`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色字符',
  `serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '排序，默认0，值小在前',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '状态，默认0，0关闭，1开启',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  `auth_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限字符串',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_role
-- ----------------------------
INSERT INTO `wra_role` VALUES (1, '超级管理员', 'admin', '0', 0, '默认，不可更改/删除', '2021-03-20 11:38:04', '2021-03-20 11:38:08', '2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19');
INSERT INTO `wra_role` VALUES (2, '普通用户', 'user', '0', 0, '默认，不可更改/删除', '2021-03-20 11:38:46', '2021-03-21 10:42:11', '6');

-- ----------------------------
-- Table structure for wra_update_log
-- ----------------------------
DROP TABLE IF EXISTS `wra_update_log`;
CREATE TABLE `wra_update_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '更新版本',
  `text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '更新内容',
  `create_date` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_update_log
-- ----------------------------

-- ----------------------------
-- Table structure for wra_user
-- ----------------------------
DROP TABLE IF EXISTS `wra_user`;
CREATE TABLE `wra_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名，登录，禁止修改',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '昵称，显示，可修改',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '/assets/images/touxiang.gif' COMMENT '用户头像',
  `department_id` int(20) NULL DEFAULT NULL COMMENT '部门id',
  `post_id` int(20) NULL DEFAULT NULL COMMENT '岗位id',
  `role_id` int(20) NOT NULL DEFAULT 2 COMMENT '角色id',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱号',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '状态，默认0,0关闭，1开启',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_department`(`department_id`) USING BTREE,
  INDEX `fk_user_post`(`post_id`) USING BTREE,
  INDEX `fk_user_role`(`role_id`) USING BTREE,
  CONSTRAINT `fk_user_department` FOREIGN KEY (`department_id`) REFERENCES `wra_department` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_post` FOREIGN KEY (`post_id`) REFERENCES `wra_post` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `wra_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wra_user
-- ----------------------------
INSERT INTO `wra_user` VALUES (1, 'admin', 'webra', '82956b56f769234e5a646a14966c0c164f4ce46f5727886c', '/touxiang.gif', NULL, NULL, 1, '18231200000', '\r\nli_zhm@qq.com', 0, NULL, '2021-04-20 00:07:42', '2021-04-20 00:07:45');

SET FOREIGN_KEY_CHECKS = 1;
