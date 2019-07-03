CREATE TABLE system_resource (
  id BIGINT NOT NULL AUTO_INCREMENT,
  parent_resource_id BIGINT,
  resource_code VARCHAR (255),
  resource_icon VARCHAR (255),
  resource_name VARCHAR (255),
  resource_url VARCHAR (255),
  is_out_link INTEGER,
  resource_type VARCHAR (255),
  resource_index INTEGER,
  PRIMARY KEY (id)
) ;

CREATE TABLE system_role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_time DATETIME,
  remark VARCHAR (255),
  role_name VARCHAR (255),
  PRIMARY KEY (id)
) ;

CREATE TABLE system_role_resource_assign (
  id BIGINT NOT NULL AUTO_INCREMENT,
  resource_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (id)
) ;

CREATE TABLE system_user_role_assign (
  id BIGINT NOT NULL AUTO_INCREMENT,
  role_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id)
) ;

CREATE TABLE sytem_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_time DATETIME,
  email VARCHAR (255),
  last_change_password_time DATETIME,
  mobile VARCHAR (255),
  operator_id BIGINT,
  PASSWORD VARCHAR (255),
  realname VARCHAR (255),
  remark VARCHAR (255),
  user_state INTEGER,
  username VARCHAR (255),
  PRIMARY KEY (id)
) ;

