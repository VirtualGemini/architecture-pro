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
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `config_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件配置ID',
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
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
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
  ('e988bb510f2047fa8155ee9e0647450a','数据库存储',1,'数据库存储文件',0,'{\"domain\": \"http://127.0.0.1:3006\"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5b2a898d6b324565b7f073c285eadd71','本地存储',10,'本地磁盘存储',1,'{\"domain\": \"http://127.0.0.1:3006\", \"basePath\": \"/tmp/uploads\"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5b57706310864da0a8955b5b25bf57ac','S3云存储',20,'S3/OSS云存储(当前仅测试阿里云，理论上 S3 类型都支持，若遇到问题请提出您宝贵的 issue，这将有利于我们完善和优化)',0,'{\"bucket\": \"bucket\", \"endpoint\": \"oss-cn-beijing.aliyuncs.com\", \"accessKey\": \"Your Access Key\", \"accessSecret\": \"Your Access Secret\", \"enablePublicAccess\": true, \"enablePathStyleAccess\": false}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('16a7a006bcf7405db1bdcb0c18ef1ccd','FTP存储',11,'FTP 文件存储示例',0,'{\"host\": \"127.0.0.1\", \"port\": 21, \"username\": \"ftp-user\", \"password\": \"ftp-password\", \"mode\": \"Passive\", \"basePath\": \"/uploads\", \"domain\": \"http://127.0.0.1:3006\"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c399bc446fa54e07b7b4267b8cca469f','SFTP存储',12,'SFTP 文件存储示例',0,'{\"host\": \"127.0.0.1\", \"port\": 22, \"username\": \"sftp-user\", \"password\": \"sftp-password\", \"basePath\": \"/uploads\", \"domain\": \"http://127.0.0.1:3006\"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_file_config` ENABLE KEYS */;

--
-- Table structure for table `sys_file_content`
--

DROP TABLE IF EXISTS `sys_file_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file_content` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `config_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件配置ID',
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
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `parent_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父级菜单ID',
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
  ('d2b1144e767b44a9b8430dc0bd7bced3',NULL,'menu','Dashboard','menus.dashboard.title','/dashboard','/index/index',NULL,'ri:pie-chart-line',NULL,1,0,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e88c1db00a7b4fcd82505c60b5d62e87','d2b1144e767b44a9b8430dc0bd7bced3','menu','Console','menus.dashboard.console','console','/dashboard/console',NULL,NULL,NULL,1,1,0,0,0,NULL,0,0,NULL,1,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('dd1779417fb047f5a0b935cdca264042',NULL,'menu','System','menus.system.title','/system','/index/index',NULL,'ri:user-3-line',NULL,1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('2548bf85e40843d59e27ef9f510becfe','dd1779417fb047f5a0b935cdca264042','menu','User','menus.system.user','user','/system/user',NULL,NULL,NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('6d9252ed10c6478a8f820a7176a01262','dd1779417fb047f5a0b935cdca264042','menu','Role','menus.system.role','role','/system/role',NULL,NULL,NULL,1,2,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('51690b5840df4085b239f080b2066860','dd1779417fb047f5a0b935cdca264042','menu','UserCenter','menus.system.userCenter','user-center','/system/user-center',NULL,NULL,NULL,1,3,1,1,1,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('9ea6bb64c49e44e2861b83a75b06208d','dd1779417fb047f5a0b935cdca264042','menu','Menus','menus.system.menu','menu','/system/menu',NULL,NULL,NULL,1,4,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('df8911e52adc459ebeea91ef4d14863a','9ea6bb64c49e44e2861b83a75b06208d','button','MenusCreate','新增',NULL,NULL,NULL,NULL,'system:menu:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5bb2c5fa3ed54404879ae73cfb096d68','9ea6bb64c49e44e2861b83a75b06208d','button','MenusUpdate','编辑',NULL,NULL,NULL,NULL,'system:menu:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c2c3fb4089fc426bb058d481b43b3966','9ea6bb64c49e44e2861b83a75b06208d','button','MenusDelete','删除',NULL,NULL,NULL,NULL,'system:menu:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('9c045fa1a23547b888c7918ca5eaafee',NULL,'menu','Result','menus.result.title','/result','/index/index',NULL,'ri:checkbox-circle-line',NULL,1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('ad50d9a466104ca5b476f91f258abeb6','9c045fa1a23547b888c7918ca5eaafee','menu','ResultSuccess','menus.result.success','success','/result/success',NULL,'ri:checkbox-circle-line',NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5355a5aa8ccd4634ac59fe646e4a6aa3','9c045fa1a23547b888c7918ca5eaafee','menu','ResultFail','menus.result.fail','fail','/result/fail',NULL,'ri:close-circle-line',NULL,1,2,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('ddba3eeb12154bdc9574618865eb7a9f',NULL,'menu','Exception','menus.exception.title','/exception','/index/index',NULL,'ri:error-warning-line',NULL,1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c01e95e1b54f4f86a2829b6f9308a40a','ddba3eeb12154bdc9574618865eb7a9f','menu','Exception403','menus.exception.forbidden','403','/exception/403',NULL,NULL,NULL,1,1,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('dd0913de39664f8091dba9e730313d89','ddba3eeb12154bdc9574618865eb7a9f','menu','Exception404','menus.exception.notFound','404','/exception/404',NULL,NULL,NULL,1,2,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('d800a9261b064f94bf239c705800d845','ddba3eeb12154bdc9574618865eb7a9f','menu','Exception500','menus.exception.serverError','500','/exception/500',NULL,NULL,NULL,1,3,1,0,1,NULL,0,0,NULL,0,NULL,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('2f8a9e0fc49b470abfdc506bf5cd49d4','2548bf85e40843d59e27ef9f510becfe','button','UserCreate','新增',NULL,NULL,NULL,NULL,'system:user:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('99742991c244409abbf485f4108d84f1','2548bf85e40843d59e27ef9f510becfe','button','UserUpdate','编辑',NULL,NULL,NULL,NULL,'system:user:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('3f3a83ef8c484afd8385864becbd0c0d','2548bf85e40843d59e27ef9f510becfe','button','UserDelete','删除',NULL,NULL,NULL,NULL,'system:user:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0c9b55a015a14b689d346f81d44d9dac','6d9252ed10c6478a8f820a7176a01262','button','RoleCreate','新增',NULL,NULL,NULL,NULL,'system:role:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('b9d06d56a04244d68ff6f6c0fe823ad3','6d9252ed10c6478a8f820a7176a01262','button','RoleUpdate','编辑',NULL,NULL,NULL,NULL,'system:role:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('eeff1b1c873b4116bbbba069d2020ae4','6d9252ed10c6478a8f820a7176a01262','button','RoleDelete','删除',NULL,NULL,NULL,NULL,'system:role:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('833a44bec17e4ba2890cd532bf5cedb7','6d9252ed10c6478a8f820a7176a01262','button','RolePermission','菜单权限',NULL,NULL,NULL,NULL,'system:role:permission',1,5,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0f6f8f31b87d45eb81af46e22452c9b1','2548bf85e40843d59e27ef9f510becfe','button','UserQuery','查询',NULL,NULL,NULL,NULL,'system:user:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e46025c1357a4fc6b9ef82b8fd3c1ca2','6d9252ed10c6478a8f820a7176a01262','button','RoleQuery','查询',NULL,NULL,NULL,NULL,'system:role:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e464d665546c4079a059da00a9f96456','9ea6bb64c49e44e2861b83a75b06208d','button','MenusQuery','查询',NULL,NULL,NULL,NULL,'system:menu:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('3bc8911ee1e14a1d97ac6e0953428579',NULL,'menu','Config','menus.config.title','/config','/index/index',NULL,'ri:settings-3-line',NULL,1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0b819c2e55fe4a2e8a21ae7ca32a2873','3bc8911ee1e14a1d97ac6e0953428579','menu','FileConfig','menus.config.fileConfig','file-config','/config/file-config',NULL,'',NULL,1,1,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('9bac1634f09a42008d73b5474829a506','0b819c2e55fe4a2e8a21ae7ca32a2873','button','FileConfigQuery','查询',NULL,NULL,NULL,NULL,'system:file-config:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('592f7901e34546fd99bd133cdd93005b','0b819c2e55fe4a2e8a21ae7ca32a2873','button','FileConfigCreate','新增',NULL,NULL,NULL,NULL,'system:file-config:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('cbbdd66f582d48e2ac961aa58889ba86','0b819c2e55fe4a2e8a21ae7ca32a2873','button','FileConfigUpdate','编辑',NULL,NULL,NULL,NULL,'system:file-config:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('67ec0ef6d2aa4bb980062dc58a7eac2a','0b819c2e55fe4a2e8a21ae7ca32a2873','button','FileConfigDelete','删除',NULL,NULL,NULL,NULL,'system:file-config:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('56c4f2ec6fa747daab0360656c65f035','dd1779417fb047f5a0b935cdca264042','menu','FileManage','menus.system.fileManage','file-manage','/system/file-manage',NULL,'',NULL,1,5,1,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('1058647de21647d2a391b4440fe9ee99','56c4f2ec6fa747daab0360656c65f035','button','FileQuery','查询',NULL,NULL,NULL,NULL,'system:file:query',1,1,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('a8d2fe6b3ca04aed80e043b1d775a5d5','56c4f2ec6fa747daab0360656c65f035','button','FileCreate','新增',NULL,NULL,NULL,NULL,'system:file:create',1,2,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('fce350f15d3f4756a72aca561d99007d','56c4f2ec6fa747daab0360656c65f035','button','FileUpdate','编辑',NULL,NULL,NULL,NULL,'system:file:update',1,3,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c1c8363e790549c3a82b83d8801265f2','56c4f2ec6fa747daab0360656c65f035','button','FileDelete','删除',NULL,NULL,NULL,NULL,'system:file:delete',1,4,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('de9bbc5bdca94b1fac8d269f7a6ecc33','56c4f2ec6fa747daab0360656c65f035','button','FileDownload','下载',NULL,NULL,NULL,NULL,'system:file:download',1,5,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0f4c932b5cfb4ee588dda70b293acaa7','56c4f2ec6fa747daab0360656c65f035','button','FileUpload','上传',NULL,NULL,NULL,NULL,'system:file:upload',1,6,0,0,0,NULL,0,0,NULL,0,NULL,0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

--
-- Table structure for table `sys_profile`
--

DROP TABLE IF EXISTS `sys_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_profile` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
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
  ('ca8b921307ac47a4902222f7144b25e6','39f40e1c95cc4988a9ad71722c46c97f','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Super',2,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('8ee9f35da1ab445ab227bec289fcf17d','e64f4fbf77e1419ab7e9800405e4a4f1','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Admin',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('1c2a11d7af47455696633a9eae5d4695','77e520e10da24c17986a7e5da387124f','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=User',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','[\"可爱\", \"很可爱\", \"非常可爱\"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_profile` ENABLE KEYS */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
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
  ('34f874655a7843859e766476692ddaac','超级管理员','R_SUPER','拥有系统全部权限',0,3,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c4b9ae7b6a0a4014b7b48f54d2f82281','管理员','R_ADMIN','拥有系统管理权限',0,2,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('47c0e8e634004d5bbec4931ff111c339','普通用户','R_USER','拥有系统普通权限',0,1,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

--
-- Table structure for table `sys_role_menu_permission`
--

DROP TABLE IF EXISTS `sys_role_menu_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu_permission` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `role_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单/按钮ID',
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
  ('81304ac166824eb6828df9b50d1874ea','34f874655a7843859e766476692ddaac','d2b1144e767b44a9b8430dc0bd7bced3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('473e380dc4f44485a955c9686e2897b6','34f874655a7843859e766476692ddaac','e88c1db00a7b4fcd82505c60b5d62e87','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c15a7418ff6f44759aedc8dee1ce978e','34f874655a7843859e766476692ddaac','dd1779417fb047f5a0b935cdca264042','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('666380d4b6b745659ecba4196ddd545d','34f874655a7843859e766476692ddaac','2548bf85e40843d59e27ef9f510becfe','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e72328b689a54dd89199948af146ec7b','34f874655a7843859e766476692ddaac','6d9252ed10c6478a8f820a7176a01262','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0eebe3a313a4444ba905d9a48dd18933','34f874655a7843859e766476692ddaac','51690b5840df4085b239f080b2066860','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('538777561e264be3bf48905965cb7b75','34f874655a7843859e766476692ddaac','9ea6bb64c49e44e2861b83a75b06208d','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c665d303266a417f9b2fbc66bd02c038','34f874655a7843859e766476692ddaac','df8911e52adc459ebeea91ef4d14863a','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('6d3785b7b7b0458686392326f297015c','34f874655a7843859e766476692ddaac','5bb2c5fa3ed54404879ae73cfb096d68','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('719530463b004eac86c375dd99c202e8','34f874655a7843859e766476692ddaac','c2c3fb4089fc426bb058d481b43b3966','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('cfa3d89c822047aaba73b8b526db925b','34f874655a7843859e766476692ddaac','9c045fa1a23547b888c7918ca5eaafee','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('d6a92a537dce459d8badd033695c6a36','34f874655a7843859e766476692ddaac','ad50d9a466104ca5b476f91f258abeb6','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('b774ddf5649642a7a86044e222a9d69d','34f874655a7843859e766476692ddaac','5355a5aa8ccd4634ac59fe646e4a6aa3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('260be65eb9da4edb9f1480633a38ecae','34f874655a7843859e766476692ddaac','ddba3eeb12154bdc9574618865eb7a9f','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('3b12507a82a14dc5a4c64af117281818','34f874655a7843859e766476692ddaac','c01e95e1b54f4f86a2829b6f9308a40a','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('795b1768efb9460c8fa944a7be5efe62','34f874655a7843859e766476692ddaac','dd0913de39664f8091dba9e730313d89','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('4917f1a240c64b6c9298e55696c39543','34f874655a7843859e766476692ddaac','d800a9261b064f94bf239c705800d845','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('de850c2b581146249a980d7146271309','34f874655a7843859e766476692ddaac','2f8a9e0fc49b470abfdc506bf5cd49d4','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('110ce30e3bc5441090bfb142a8a94f7c','34f874655a7843859e766476692ddaac','99742991c244409abbf485f4108d84f1','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('52a068b3f6ee4552a7fe8d6555502701','34f874655a7843859e766476692ddaac','3f3a83ef8c484afd8385864becbd0c0d','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('2ba447ec662c49338b930f176ed71578','34f874655a7843859e766476692ddaac','0c9b55a015a14b689d346f81d44d9dac','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('2bc2bbec7d304b7189f9ad2ee6efff01','34f874655a7843859e766476692ddaac','b9d06d56a04244d68ff6f6c0fe823ad3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('4f906d77bef8460b97cc3bcf2e4fdf3a','34f874655a7843859e766476692ddaac','eeff1b1c873b4116bbbba069d2020ae4','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('7a63e4bc9a494b14a07cef8537fb675e','34f874655a7843859e766476692ddaac','833a44bec17e4ba2890cd532bf5cedb7','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0af9d4ef20944b1ba5ff143306dc333a','34f874655a7843859e766476692ddaac','0f6f8f31b87d45eb81af46e22452c9b1','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('8b45743a097042d1b8dcef0f0ebf5ce4','34f874655a7843859e766476692ddaac','e46025c1357a4fc6b9ef82b8fd3c1ca2','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('91e293c67dba415b9c29a15c9be03c14','34f874655a7843859e766476692ddaac','e464d665546c4079a059da00a9f96456','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('3aebe350f10d4a26a07b9e88f850c4c4','c4b9ae7b6a0a4014b7b48f54d2f82281','d2b1144e767b44a9b8430dc0bd7bced3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('4a494758304a4159bb4e0a5c209afbe0','c4b9ae7b6a0a4014b7b48f54d2f82281','e88c1db00a7b4fcd82505c60b5d62e87','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('329c14e0b5e94d448a776913c50a48a6','c4b9ae7b6a0a4014b7b48f54d2f82281','dd1779417fb047f5a0b935cdca264042','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('2299a8381e0642b780e829b676ec2dc1','c4b9ae7b6a0a4014b7b48f54d2f82281','2548bf85e40843d59e27ef9f510becfe','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('d82eec1c6aff402fa3f16502805f563d','c4b9ae7b6a0a4014b7b48f54d2f82281','2f8a9e0fc49b470abfdc506bf5cd49d4','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('dc2be3a3499e48c4ad6f028e7a55376d','c4b9ae7b6a0a4014b7b48f54d2f82281','99742991c244409abbf485f4108d84f1','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('7e8b7849e53a4ad2bfe7a6bd30c70267','c4b9ae7b6a0a4014b7b48f54d2f82281','3f3a83ef8c484afd8385864becbd0c0d','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e9ed2797d0d74990b37c886e97647ea7','c4b9ae7b6a0a4014b7b48f54d2f82281','0f6f8f31b87d45eb81af46e22452c9b1','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c236b564f1634bd5ba7f1e423b53a171','c4b9ae7b6a0a4014b7b48f54d2f82281','51690b5840df4085b239f080b2066860','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('eee90789cd6249afab34f60ee9ec8cef','c4b9ae7b6a0a4014b7b48f54d2f82281','9c045fa1a23547b888c7918ca5eaafee','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('315558941e54409ca4516836cf49c87f','c4b9ae7b6a0a4014b7b48f54d2f82281','ad50d9a466104ca5b476f91f258abeb6','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('10fa256f6a38484ebcb991c2b7ef8c0b','c4b9ae7b6a0a4014b7b48f54d2f82281','5355a5aa8ccd4634ac59fe646e4a6aa3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('b251ce7c0306464fb1d6b81f01991623','c4b9ae7b6a0a4014b7b48f54d2f82281','ddba3eeb12154bdc9574618865eb7a9f','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('18c93c2faf074a1ca96869548147085f','c4b9ae7b6a0a4014b7b48f54d2f82281','c01e95e1b54f4f86a2829b6f9308a40a','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('cb615f3b189e4485910c06a193e10bf2','c4b9ae7b6a0a4014b7b48f54d2f82281','dd0913de39664f8091dba9e730313d89','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('33b4530f98e04a1e98981fdbedb262c4','c4b9ae7b6a0a4014b7b48f54d2f82281','d800a9261b064f94bf239c705800d845','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('11124476eda14c8dac3007c83572e14e','47c0e8e634004d5bbec4931ff111c339','51690b5840df4085b239f080b2066860','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c94f039c02ba4ab689e52ad9959da070','47c0e8e634004d5bbec4931ff111c339','9c045fa1a23547b888c7918ca5eaafee','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('1ab69965a5254e7cbe762a8f8a564f41','47c0e8e634004d5bbec4931ff111c339','ad50d9a466104ca5b476f91f258abeb6','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('a91d93c0ac024ea7bdb56434aaa56cbc','47c0e8e634004d5bbec4931ff111c339','5355a5aa8ccd4634ac59fe646e4a6aa3','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('0e28356eccd74ecfaae62dd1ae672404','47c0e8e634004d5bbec4931ff111c339','ddba3eeb12154bdc9574618865eb7a9f','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('ec7ed485b9de453fb53c501f78a130be','47c0e8e634004d5bbec4931ff111c339','c01e95e1b54f4f86a2829b6f9308a40a','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('fb9e026c1689499c83be6987cc553b5f','47c0e8e634004d5bbec4931ff111c339','dd0913de39664f8091dba9e730313d89','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e0c0140836d54cce89da3231e43ff50d','47c0e8e634004d5bbec4931ff111c339','d800a9261b064f94bf239c705800d845','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('9a4383d41c39458dbc045aa207b71329','34f874655a7843859e766476692ddaac','3bc8911ee1e14a1d97ac6e0953428579','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('957e5a0621e645caaa3fdc53622a0777','34f874655a7843859e766476692ddaac','0b819c2e55fe4a2e8a21ae7ca32a2873','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('87daeccb077b40c4824f34ea7abe81d4','34f874655a7843859e766476692ddaac','9bac1634f09a42008d73b5474829a506','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('29ca526332c34553a1842f91efc02575','34f874655a7843859e766476692ddaac','592f7901e34546fd99bd133cdd93005b','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('bd46b2befbdf4f169bf46f4ec17797bb','34f874655a7843859e766476692ddaac','cbbdd66f582d48e2ac961aa58889ba86','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('de1fb7b692ab412d83fcdfa6eaa76acd','34f874655a7843859e766476692ddaac','67ec0ef6d2aa4bb980062dc58a7eac2a','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('aaaec8ae1e634e86a729c120d748a7dd','34f874655a7843859e766476692ddaac','56c4f2ec6fa747daab0360656c65f035','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('7da2f857997e4c9b95988d69a8d410ce','34f874655a7843859e766476692ddaac','1058647de21647d2a391b4440fe9ee99','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('041c9463f85042e1abd6a08f4b12b67e','34f874655a7843859e766476692ddaac','a8d2fe6b3ca04aed80e043b1d775a5d5','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('38e71ea958b842f39a8e40a141be3ad8','34f874655a7843859e766476692ddaac','fce350f15d3f4756a72aca561d99007d','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('1c0e44806e544b86a57d403720771a90','34f874655a7843859e766476692ddaac','c1c8363e790549c3a82b83d8801265f2','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('370f5af7e8d247dbb359903be670ffd9','34f874655a7843859e766476692ddaac','de9bbc5bdca94b1fac8d269f7a6ecc33','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('839197c3846e4775a0ff0915b19d2861','34f874655a7843859e766476692ddaac','0f4c932b5cfb4ee588dda70b293acaa7','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_role_menu_permission` ENABLE KEYS */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
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
  ('39f40e1c95cc4988a9ad71722c46c97f','Super','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','admin@architecture.pro','15800158001',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e64f4fbf77e1419ab7e9800405e4a4f1','Admin','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','admin@architecture.pro','15800158002',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('77e520e10da24c17986a7e5da387124f','User','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','user@architecture.pro','15800158003',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `role_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
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
  ('31ada5f4f0e84503b58e7c86cf64c0e1','39f40e1c95cc4988a9ad71722c46c97f','34f874655a7843859e766476692ddaac','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('198c0ae4b0744fe88b8626cca98704f9','e64f4fbf77e1419ab7e9800405e4a4f1','c4b9ae7b6a0a4014b7b48f54d2f82281','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('35330b308a8a45a8870001cd39ec2c10','77e520e10da24c17986a7e5da387124f','47c0e8e634004d5bbec4931ff111c339','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
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
