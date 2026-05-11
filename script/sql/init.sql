-- MySQL dump 10.13  Distrib 9.6.0, for macos15.7 (arm64)
--
-- Host: 127.0.0.1    Database: architecture_pro
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Database: `architecture_pro`
--

DROP DATABASE IF EXISTS `architecture_pro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE DATABASE IF NOT EXISTS `architecture_pro` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Use the database `architecture_pro`
--

USE `architecture_pro`;

--
-- Character set and collation for the database
--

SET NAMES utf8mb4;

--
-- Table structure for table `sys_file`
--

DROP TABLE IF EXISTS `sys_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `config_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件配置ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `path` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件URL',
  `type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件类型',
  `size` bigint DEFAULT '0' COMMENT '文件大小(字节)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_sys_file_config_id` (`config_id`),
  KEY `idx_sys_file_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_file_config`
--

DROP TABLE IF EXISTS `sys_file_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file_config` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置名称',
  `storage` int NOT NULL COMMENT '存储器(1-数据库 10-本地磁盘 11-FTP 12-SFTP 20-S3云存储)',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `master` tinyint DEFAULT '0' COMMENT '是否主配置(0-否 1-是)',
  `config` json DEFAULT NULL COMMENT '存储器配置(JSON)',
  `enabled` tinyint DEFAULT '1' COMMENT '启用状态(0-禁用 1-启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_sys_file_config_storage` (`storage`),
  KEY `idx_sys_file_config_master` (`master`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_file_config`
--

/*!40000 ALTER TABLE `sys_file_config` DISABLE KEYS */;
INSERT INTO `sys_file_config` VALUES
  ('C0000001','数据库存储',1,'数据库存储文件',0,'{\"domain\": \"http://127.0.0.1:3006\"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('C0000002','本地存储',10,'本地磁盘存储',1,'{\"domain\": \"http://127.0.0.1:3006\", \"basePath\": \"/tmp/uploads\"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('C0000003','S3云存储',20,'S3/OSS云存储(当前仅测试阿里云，理论上 S3 类型都支持，若遇到问题请提出您宝贵的 issue，这将有利于我们完善和优化)',0,'{\"bucket\": \"bucket\", \"endpoint\": \"oss-cn-beijing.aliyuncs.com\", \"accessKey\": \"Your Access Key\", \"accessSecret\": \"Your Access Secret\", \"enablePublicAccess\": true, \"enablePathStyleAccess\": false}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('C0000004','FTP存储',11,'FTP 文件存储示例',0,'{\"host\": \"127.0.0.1\", \"port\": 21, \"username\": \"ftp-user\", \"password\": \"ftp-password\", \"mode\": \"Passive\", \"basePath\": \"/uploads\", \"domain\": \"http://127.0.0.1:3006\"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('C0000005','SFTP存储',12,'SFTP 文件存储示例',0,'{\"host\": \"127.0.0.1\", \"port\": 22, \"username\": \"sftp-user\", \"password\": \"sftp-password\", \"basePath\": \"/uploads\", \"domain\": \"http://127.0.0.1:3006\"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_file_config` ENABLE KEYS */;

--
-- Table structure for table `sys_file_content`
--

DROP TABLE IF EXISTS `sys_file_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file_content` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `config_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件配置ID',
  `path` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `content` mediumblob NOT NULL COMMENT '文件内容(二进制)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_sys_file_content_config_id` (`config_id`),
  KEY `idx_sys_file_content_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件内容表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_id_sequence`
--

DROP TABLE IF EXISTS `sys_id_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_id_sequence` (
  `biz_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务编码',
  `current_value` int NOT NULL COMMENT '当前序号',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`biz_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务ID序列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_id_sequence`
--

/*!40000 ALTER TABLE `sys_id_sequence` DISABLE KEYS */;
INSERT INTO `sys_id_sequence` VALUES
  ('file',3,'2026-05-10 12:00:00'),
  ('file_config',5,'2026-05-10 12:00:00'),
  ('file_content',3,'2026-05-10 12:00:00'),
  ('menu',40,'2026-05-10 12:00:00'),
  ('profile',4,'2026-05-10 12:00:00'),
  ('role',3,'2026-05-10 12:00:00'),
  ('role_menu_permission',64,'2026-05-10 12:00:00'),
  ('user',4,'2026-05-10 12:00:00'),
  ('user_role',4,'2026-05-10 12:00:00');
/*!40000 ALTER TABLE `sys_id_sequence` ENABLE KEYS */;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `parent_id` char(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父级菜单ID',
  `menu_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单类型(menu/button)',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路由名称或权限名称',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单标题或按钮标题',
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件路径',
  `redirect` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '重定向地址',
  `icon` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标',
  `auth_mark` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `is_enable` tinyint DEFAULT '1' COMMENT '是否启用',
  `sort` int DEFAULT '1' COMMENT '排序',
  `keep_alive` tinyint DEFAULT '0' COMMENT '是否缓存',
  `is_hide` tinyint DEFAULT '0' COMMENT '是否隐藏菜单',
  `is_hide_tab` tinyint DEFAULT '0' COMMENT '是否隐藏标签页',
  `link` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '外部链接',
  `is_iframe` tinyint DEFAULT '0' COMMENT '是否内嵌',
  `show_badge` tinyint DEFAULT '0' COMMENT '是否显示徽章',
  `show_text_badge` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文本徽章',
  `fixed_tab` tinyint DEFAULT '0' COMMENT '是否固定标签页',
  `active_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '激活菜单路径',
  `is_full_page` tinyint DEFAULT '0' COMMENT '是否全屏页面',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  `active_auth_mark` varchar(100) COLLATE utf8mb4_unicode_ci GENERATED ALWAYS AS ((case when (`deleted` = 0) then `auth_mark` else NULL end)) STORED COMMENT '未删除权限标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_auth_mark` (`active_auth_mark`),
  KEY `idx_menu_parent_id` (`parent_id`),
  KEY `idx_menu_auth_mark` (`auth_mark`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_type`, `name`, `title`, `path`, `component`, `redirect`, `icon`, `auth_mark`, `is_enable`, `sort`, `keep_alive`, `is_hide`, `is_hide_tab`, `link`, `is_iframe`, `show_badge`, `show_text_badge`, `fixed_tab`, `active_path`, `is_full_page`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
  ('M0000001',NULL,'menu','Dashboard','menus.dashboard.title','/dashboard','/index/index',NULL,'ri:pie-chart-line',NULL,1,0,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000002','M0000001','menu','Console','menus.dashboard.console','console','/dashboard/console',NULL,NULL,NULL,1,1,0,0,0,NULL,0,0,NULL,1,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000003',NULL,'menu','System','menus.system.title','/system','/index/index',NULL,'ri:user-3-line',NULL,1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000004','M0000003','menu','User','menus.system.user','user','/system/user',NULL,NULL,NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000005','M0000003','menu','Role','menus.system.role','role','/system/role',NULL,NULL,NULL,1,2,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000006','M0000003','menu','UserCenter','menus.system.userCenter','user-center','/system/user-center',NULL,NULL,NULL,1,3,1,1,1,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000007','M0000003','menu','Menus','menus.system.menu','menu','/system/menu',NULL,NULL,NULL,1,4,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000008','M0000007','button','MenusCreate','新增',NULL,NULL,NULL,NULL,'system:menu:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000009','M0000007','button','MenusUpdate','编辑',NULL,NULL,NULL,NULL,'system:menu:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000010','M0000007','button','MenusDelete','删除',NULL,NULL,NULL,NULL,'system:menu:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000011',NULL,'menu','Result','menus.result.title','/result','/index/index',NULL,'ri:checkbox-circle-line',NULL,1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000012','M0000011','menu','ResultSuccess','menus.result.success','success','/result/success',NULL,'ri:checkbox-circle-line',NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000013','M0000011','menu','ResultFail','menus.result.fail','fail','/result/fail',NULL,'ri:close-circle-line',NULL,1,2,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000014',NULL,'menu','Exception','menus.exception.title','/exception','/index/index',NULL,'ri:error-warning-line',NULL,1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000015','M0000014','menu','Exception403','menus.exception.forbidden','403','/exception/403',NULL,NULL,NULL,1,1,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000016','M0000014','menu','Exception404','menus.exception.notFound','404','/exception/404',NULL,NULL,NULL,1,2,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000017','M0000014','menu','Exception500','menus.exception.serverError','500','/exception/500',NULL,NULL,NULL,1,3,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000018','M0000004','button','UserCreate','新增',NULL,NULL,NULL,NULL,'system:user:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000019','M0000004','button','UserUpdate','编辑',NULL,NULL,NULL,NULL,'system:user:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000020','M0000004','button','UserDelete','删除',NULL,NULL,NULL,NULL,'system:user:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000021','M0000005','button','RoleCreate','新增',NULL,NULL,NULL,NULL,'system:role:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000022','M0000005','button','RoleUpdate','编辑',NULL,NULL,NULL,NULL,'system:role:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000023','M0000005','button','RoleDelete','删除',NULL,NULL,NULL,NULL,'system:role:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000024','M0000005','button','RolePermission','菜单权限',NULL,NULL,NULL,NULL,'system:role:permission',1,5,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000025','M0000004','button','UserQuery','查询',NULL,NULL,NULL,NULL,'system:user:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000026','M0000005','button','RoleQuery','查询',NULL,NULL,NULL,NULL,'system:role:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000027','M0000007','button','MenusQuery','查询',NULL,NULL,NULL,NULL,'system:menu:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000028',NULL,'menu','Config','menus.config.title','/config','/index/index',NULL,'ri:settings-3-line',NULL,1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000029','M0000028','menu','FileConfig','menus.config.fileConfig','file-config','/config/file-config',NULL,'',NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000030','M0000029','button','FileConfigQuery','查询',NULL,NULL,NULL,NULL,'system:file-config:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000031','M0000029','button','FileConfigCreate','新增',NULL,NULL,NULL,NULL,'system:file-config:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000032','M0000029','button','FileConfigUpdate','编辑',NULL,NULL,NULL,NULL,'system:file-config:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000033','M0000029','button','FileConfigDelete','删除',NULL,NULL,NULL,NULL,'system:file-config:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000034','M0000003','menu','FileManage','menus.system.fileManage','file-manage','/system/file-manage',NULL,'',NULL,1,5,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000035','M0000034','button','FileQuery','查询',NULL,NULL,NULL,NULL,'system:file:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000036','M0000034','button','FileCreate','新增',NULL,NULL,NULL,NULL,'system:file:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000037','M0000034','button','FileUpdate','编辑',NULL,NULL,NULL,NULL,'system:file:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000038','M0000034','button','FileDelete','删除',NULL,NULL,NULL,NULL,'system:file:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000039','M0000034','button','FileDownload','下载',NULL,NULL,NULL,NULL,'system:file:download',1,5,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('M0000040','M0000034','button','FileUpload','上传',NULL,NULL,NULL,NULL,'system:file:upload',1,6,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

--
-- Table structure for table `sys_profile`
--

DROP TABLE IF EXISTS `sys_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_profile` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `user_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT '0' COMMENT '性别(0-未知 1-男 2-女 3-其它)',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '个人介绍',
  `signature` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个人签名',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位',
  `company` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '在职企业',
  `tags` json DEFAULT NULL COMMENT '标签',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  CONSTRAINT `fk_sys_profile_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户资料表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_profile`
--

/*!40000 ALTER TABLE `sys_profile` DISABLE KEYS */;
INSERT INTO `sys_profile` VALUES
  ('F0000001','U0000001','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Super',2,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('F0000002','U0000002','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Admin',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('F0000003','U0000003','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=User',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_profile` ENABLE KEYS */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `role_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色描述',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '角色类型：0系统角色，1自定义角色',
  `role_level` int NOT NULL DEFAULT '0' COMMENT '角色等级，值越大权限越高',
  `enabled` tinyint DEFAULT '1' COMMENT '启用状态(0-禁用 1-启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  `active_role_code` varchar(50) COLLATE utf8mb4_unicode_ci GENERATED ALWAYS AS ((case when (`deleted` = 0) then `role_code` else NULL end)) STORED COMMENT '未删除角色编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`active_role_code`),
  KEY `idx_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `type`, `role_level`, `enabled`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
  ('R0000001','超级管理员','R_SUPER','拥有系统全部权限',0,3,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('R0000002','管理员','R_ADMIN','拥有系统管理权限',0,2,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('R0000003','普通用户','R_USER','拥有系统普通权限',0,1,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

--
-- Table structure for table `sys_role_menu_permission`
--

DROP TABLE IF EXISTS `sys_role_menu_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu_permission` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `role_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `menu_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单/按钮ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  `active_role_menu_permission` tinyint GENERATED ALWAYS AS ((case when (`deleted` = 0) then 1 else NULL end)) STORED COMMENT '未删除关联标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu_permission` (`role_id`,`menu_id`,`active_role_menu_permission`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`),
  CONSTRAINT `fk_sys_role_menu_permission_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`),
  CONSTRAINT `fk_sys_role_menu_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu_permission`
--

/*!40000 ALTER TABLE `sys_role_menu_permission` DISABLE KEYS */;
INSERT INTO `sys_role_menu_permission` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
  ('P0000001','R0000001','M0000001','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000002','R0000001','M0000002','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000003','R0000001','M0000003','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000004','R0000001','M0000004','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000005','R0000001','M0000005','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000006','R0000001','M0000006','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000007','R0000001','M0000007','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000008','R0000001','M0000008','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000009','R0000001','M0000009','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000010','R0000001','M0000010','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000011','R0000001','M0000011','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000012','R0000001','M0000012','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000013','R0000001','M0000013','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000014','R0000001','M0000014','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000015','R0000001','M0000015','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000016','R0000001','M0000016','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000017','R0000001','M0000017','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000018','R0000001','M0000018','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000019','R0000001','M0000019','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000020','R0000001','M0000020','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000021','R0000001','M0000021','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000022','R0000001','M0000022','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000023','R0000001','M0000023','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000024','R0000001','M0000024','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000025','R0000001','M0000025','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000026','R0000001','M0000026','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000027','R0000001','M0000027','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000028','R0000002','M0000001','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000029','R0000002','M0000002','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000030','R0000002','M0000003','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000031','R0000002','M0000004','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000032','R0000002','M0000018','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000033','R0000002','M0000019','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000034','R0000002','M0000020','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000035','R0000002','M0000025','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000036','R0000002','M0000006','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000037','R0000002','M0000011','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000038','R0000002','M0000012','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000039','R0000002','M0000013','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000040','R0000002','M0000014','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000041','R0000002','M0000015','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000042','R0000002','M0000016','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000043','R0000002','M0000017','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000044','R0000003','M0000006','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000045','R0000003','M0000011','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000046','R0000003','M0000012','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000047','R0000003','M0000013','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000048','R0000003','M0000014','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000049','R0000003','M0000015','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000050','R0000003','M0000016','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000051','R0000003','M0000017','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000052','R0000001','M0000028','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000053','R0000001','M0000029','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000054','R0000001','M0000030','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000055','R0000001','M0000031','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000056','R0000001','M0000032','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000057','R0000001','M0000033','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000058','R0000001','M0000034','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000059','R0000001','M0000035','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000060','R0000001','M0000036','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000061','R0000001','M0000037','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000062','R0000001','M0000038','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000063','R0000001','M0000039','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('P0000064','R0000001','M0000040','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_role_menu_permission` ENABLE KEYS */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态(1-在线 2-离线 3-异常 4-注销)',
  `login_fail_count` int DEFAULT '0' COMMENT '登录失败次数',
  `login_fail_time` datetime DEFAULT NULL COMMENT '登录失败时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  `active_username` varchar(50) COLLATE utf8mb4_unicode_ci GENERATED ALWAYS AS ((case when (`deleted` = 0) then `username` else NULL end)) STORED COMMENT '未删除用户名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`active_username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `username`, `password`, `email`, `phone`, `status`, `login_fail_count`, `login_fail_time`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
  ('U0000001','Super','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','admin@architecture.pro','15800158001',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('U0000002','Admin','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','admin@architecture.pro','15800158002',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('U0000003','User','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','user@architecture.pro','15800158003',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID(字母前缀 + 7位数字)',
  `user_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `role_id` char(9) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除(0-未删除 1-已删除)',
  `active_user_role` tinyint GENERATED ALWAYS AS ((case when (`deleted` = 0) then 1 else NULL end)) STORED COMMENT '未删除关联标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`,`active_user_role`),
  KEY `idx_user_role_user_id` (`user_id`),
  KEY `idx_user_role_role_id` (`role_id`),
  CONSTRAINT `fk_sys_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `fk_sys_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
  ('L0000001','U0000001','R0000001','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('L0000002','U0000002','R0000002','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('L0000003','U0000003','R0000003','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

--
-- Dumping routines for database 'architecture_pro'
--
--
-- WARNING: can't read the INFORMATION_SCHEMA.libraries table. It's most probably an old server 8.0.45.
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-10 14:30:14
