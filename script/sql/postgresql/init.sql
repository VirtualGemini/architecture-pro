DROP TABLE IF EXISTS sys_role_menu_permission CASCADE;
DROP TABLE IF EXISTS sys_user_role CASCADE;
DROP TABLE IF EXISTS sys_profile CASCADE;
DROP TABLE IF EXISTS sys_menu CASCADE;
DROP TABLE IF EXISTS sys_role CASCADE;
DROP TABLE IF EXISTS sys_user CASCADE;
DROP TABLE IF EXISTS sys_file_content CASCADE;
DROP TABLE IF EXISTS sys_file CASCADE;
DROP TABLE IF EXISTS sys_file_config CASCADE;

CREATE TABLE sys_file_config (
  id varchar(36) PRIMARY KEY,
  name varchar(100) NOT NULL,
  storage integer NOT NULL,
  remark varchar(500),
  master boolean DEFAULT false,
  config text,
  enabled smallint DEFAULT 1,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_sys_file_config_storage ON sys_file_config (storage);
CREATE INDEX idx_sys_file_config_master ON sys_file_config (master);

INSERT INTO sys_file_config (id, name, storage, remark, master, config, enabled, create_time, update_time, create_by, update_by, deleted) VALUES
  ('e988bb510f2047fa8155ee9e0647450a','数据库存储',1,'数据库存储文件',false,'{"domain": "http://127.0.0.1:3006"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5b2a898d6b324565b7f073c285eadd71','本地存储',10,'本地磁盘存储',true,'{"domain": "http://127.0.0.1:3006", "basePath": "/tmp/uploads"}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('5b57706310864da0a8955b5b25bf57ac','S3云存储',20,'S3/OSS云存储(当前仅测试阿里云，理论上 S3 类型都支持，若遇到问题请提出您宝贵的 issue，这将有利于我们完善和优化)',false,'{"bucket": "bucket", "endpoint": "oss-cn-beijing.aliyuncs.com", "accessKey": "Your Access Key", "accessSecret": "Your Access Secret", "enablePublicAccess": true, "enablePathStyleAccess": false}',1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('16a7a006bcf7405db1bdcb0c18ef1ccd','FTP存储',11,'FTP 文件存储示例',false,'{"host": "127.0.0.1", "port": 21, "username": "ftp-user", "password": "ftp-password", "mode": "Passive", "basePath": "/uploads", "domain": "http://127.0.0.1:3006"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c399bc446fa54e07b7b4267b8cca469f','SFTP存储',12,'SFTP 文件存储示例',false,'{"host": "127.0.0.1", "port": 22, "username": "sftp-user", "password": "sftp-password", "basePath": "/uploads", "domain": "http://127.0.0.1:3006"}',0,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);

CREATE TABLE sys_file (
  id varchar(36) PRIMARY KEY,
  config_id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  path varchar(500) NOT NULL,
  url varchar(500),
  type varchar(100),
  size bigint DEFAULT 0,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_sys_file_config_id ON sys_file (config_id);
CREATE INDEX idx_sys_file_create_by ON sys_file (create_by);

CREATE TABLE sys_file_content (
  id varchar(36) PRIMARY KEY,
  config_id varchar(36) NOT NULL,
  path varchar(500) NOT NULL,
  content bytea NOT NULL,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_sys_file_content_config_id ON sys_file_content (config_id);
CREATE INDEX idx_sys_file_content_path ON sys_file_content (path);

CREATE TABLE sys_menu (
  id varchar(36) PRIMARY KEY,
  parent_id varchar(36),
  menu_type varchar(20) NOT NULL,
  name varchar(100) NOT NULL,
  title varchar(100) NOT NULL,
  path varchar(255),
  component varchar(255),
  redirect varchar(255),
  icon varchar(100),
  auth_mark varchar(100),
  is_enable smallint DEFAULT 1,
  sort integer DEFAULT 1,
  keep_alive smallint DEFAULT 0,
  is_hide smallint DEFAULT 0,
  is_hide_tab smallint DEFAULT 0,
  link varchar(255),
  is_iframe smallint DEFAULT 0,
  show_badge smallint DEFAULT 0,
  show_text_badge varchar(50),
  fixed_tab smallint DEFAULT 0,
  active_path varchar(255),
  is_full_page smallint DEFAULT 0,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_menu_parent_id ON sys_menu (parent_id);
CREATE INDEX idx_menu_auth_mark ON sys_menu (auth_mark);
CREATE UNIQUE INDEX uk_menu_auth_mark_active ON sys_menu (auth_mark) WHERE deleted = 0 AND auth_mark IS NOT NULL;

INSERT INTO sys_menu (id, parent_id, menu_type, name, title, path, component, redirect, icon, auth_mark, is_enable, sort, keep_alive, is_hide, is_hide_tab, link, is_iframe, show_badge, show_text_badge, fixed_tab, active_path, is_full_page, create_time, update_time, create_by, update_by, deleted) VALUES
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

CREATE TABLE sys_role (
  id varchar(36) PRIMARY KEY,
  role_name varchar(50) NOT NULL,
  role_code varchar(50) NOT NULL,
  description varchar(255),
  type smallint NOT NULL DEFAULT 1,
  role_level integer NOT NULL DEFAULT 0,
  enabled smallint DEFAULT 1,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_role_code ON sys_role (role_code);
CREATE UNIQUE INDEX uk_role_code_active ON sys_role (role_code) WHERE deleted = 0;

INSERT INTO sys_role (id, role_name, role_code, description, type, role_level, enabled, create_time, update_time, create_by, update_by, deleted) VALUES
  ('34f874655a7843859e766476692ddaac','超级管理员','R_SUPER','拥有系统全部权限',0,3,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('c4b9ae7b6a0a4014b7b48f54d2f82281','管理员','R_ADMIN','拥有系统管理权限',0,2,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('47c0e8e634004d5bbec4931ff111c339','普通用户','R_USER','拥有系统普通权限',0,1,1,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);

CREATE TABLE sys_user (
  id varchar(36) PRIMARY KEY,
  username varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  email varchar(100),
  phone varchar(20),
  status smallint DEFAULT 1,
  login_fail_count integer DEFAULT 0,
  login_fail_time timestamp,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_username ON sys_user (username);
CREATE UNIQUE INDEX uk_username_active ON sys_user (username) WHERE deleted = 0;

INSERT INTO sys_user (id, username, password, email, phone, status, login_fail_count, login_fail_time, create_time, update_time, create_by, update_by, deleted) VALUES
  ('39f40e1c95cc4988a9ad71722c46c97f','Super','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','super@architecture.pro','15800158001',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('e64f4fbf77e1419ab7e9800405e4a4f1','Admin','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','admin@architecture.pro','15800158002',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('77e520e10da24c17986a7e5da387124f','User','{bcrypt}$2a$12$w9oNXS.wlWOfoHg6vMVKKuRHIASL5uTI2BtybrocyvQCMmeSIhshu','user@architecture.pro','15800158003',1,0,NULL,'2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);

CREATE TABLE sys_profile (
  id varchar(36) PRIMARY KEY,
  user_id varchar(36) NOT NULL REFERENCES sys_user (id),
  nickname varchar(50),
  avatar varchar(255),
  gender smallint DEFAULT 0,
  real_name varchar(50),
  address varchar(255),
  introduction text,
  signature varchar(255),
  position varchar(50),
  company varchar(100),
  tags text,
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE UNIQUE INDEX uk_user_id ON sys_profile (user_id);

INSERT INTO sys_profile (id, user_id, nickname, avatar, gender, real_name, address, introduction, signature, position, company, tags, create_time, update_time, create_by, update_by, deleted) VALUES
  ('ca8b921307ac47a4902222f7144b25e6','39f40e1c95cc4988a9ad71722c46c97f','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Super',2,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','["可爱", "很可爱", "非常可爱"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('8ee9f35da1ab445ab227bec289fcf17d','e64f4fbf77e1419ab7e9800405e4a4f1','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=Admin',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','["可爱", "很可爱", "非常可爱"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('1c2a11d7af47455696633a9eae5d4695','77e520e10da24c17986a7e5da387124f','虚拟双子','https://api.dicebear.com/7.x/avataaars/svg?seed=User',1,'不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','不愿透露','["可爱", "很可爱", "非常可爱"]','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);

CREATE TABLE sys_role_menu_permission (
  id varchar(36) PRIMARY KEY,
  role_id varchar(36) NOT NULL REFERENCES sys_role (id),
  menu_id varchar(36) NOT NULL REFERENCES sys_menu (id),
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_role_id ON sys_role_menu_permission (role_id);
CREATE INDEX idx_menu_id ON sys_role_menu_permission (menu_id);
CREATE UNIQUE INDEX uk_role_menu_permission_active ON sys_role_menu_permission (role_id, menu_id) WHERE deleted = 0;

INSERT INTO sys_role_menu_permission (id, role_id, menu_id, create_time, update_time, create_by, update_by, deleted) VALUES
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
  ('11124476eda14c8dac3007c83572e14e','47c0e8e634004d5bbec4931ff111c339','51690b5840df4085b239f080b2066860','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
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

CREATE TABLE sys_user_role (
  id varchar(36) PRIMARY KEY,
  user_id varchar(36) NOT NULL REFERENCES sys_user (id),
  role_id varchar(36) NOT NULL REFERENCES sys_role (id),
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  create_by varchar(50),
  update_by varchar(50),
  deleted smallint DEFAULT 0
);

CREATE INDEX idx_user_role_user_id ON sys_user_role (user_id);
CREATE INDEX idx_user_role_role_id ON sys_user_role (role_id);
CREATE UNIQUE INDEX uk_user_role_active ON sys_user_role (user_id, role_id) WHERE deleted = 0;

INSERT INTO sys_user_role (id, user_id, role_id, create_time, update_time, create_by, update_by, deleted) VALUES
  ('31ada5f4f0e84503b58e7c86cf64c0e1','39f40e1c95cc4988a9ad71722c46c97f','34f874655a7843859e766476692ddaac','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('198c0ae4b0744fe88b8626cca98704f9','e64f4fbf77e1419ab7e9800405e4a4f1','c4b9ae7b6a0a4014b7b48f54d2f82281','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0),
  ('35330b308a8a45a8870001cd39ec2c10','77e520e10da24c17986a7e5da387124f','47c0e8e634004d5bbec4931ff111c339','2026-05-10 12:00:00','2026-05-10 12:00:00','system','system',0);
