
CREATE TABLE user  ( 
	user_id    	bigint(20) AUTO_INCREMENT COMMENT '用户id'  NOT NULL,
	name       	varchar(120) COMMENT '用户姓名'  NOT NULL,
	password   	varchar(30) NULL,
	email      	varchar(30) NULL,
	code       	varchar(100) COMMENT '激活邮箱代码'  NULL,
	status     	int(11) COMMENT '用户状态'  NOT NULL,
	create_time	timestamp COMMENT '邮箱创建时间'  NOT NULL DEFAULT CURRENT_TIMESTAMP,
	user_pic   	varchar(100) NULL,
	login_type 	varchar(1) COMMENT '登录类型 1：邮箱登录 2：微博登录 3：qq登录'  NULL DEFAULT '1',
	temp_token 	varchar(100) NULL,
	PRIMARY KEY(user_id)
)
COMMENT = '用户注册表' 
GO

CREATE TABLE user_detail_info  ( 
	project_detail_id  	bigint(20) AUTO_INCREMENT COMMENT '分项目id'  NOT NULL,
	project_id         	bigint(20) COMMENT '分项目所属总项目id'  NOT NULL,
	project_detail_name	varchar(120) COMMENT '项目名称'  NOT NULL,
	detail_start_time  	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	detail_end_time    	timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
	detail_modify      	int(11) NULL DEFAULT '0',
	done               	int(11) COMMENT '是否完成任务，0没有，1完成'  NULL DEFAULT '0',
	PRIMARY KEY(project_detail_id)
)
COMMENT = '用户分项目表' 
GO
ALTER TABLE user_detail_info
	ADD CONSTRAINT fk_tb_aid
	FOREIGN KEY(project_id)
	REFERENCES user_info(project_id)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT 
GO

CREATE TABLE user_info  ( 
	project_id  	bigint(20) AUTO_INCREMENT COMMENT '总项目id'  NOT NULL,
	email       	varchar(30) COMMENT '用户邮箱'  NOT NULL,
	project_name	varchar(120) COMMENT '项目名称'  NOT NULL,
	start_time  	timestamp COMMENT '项目开始时间'  NOT NULL DEFAULT CURRENT_TIMESTAMP,
	end_time    	timestamp COMMENT '项目结束时间'  NOT NULL DEFAULT '0000-00-00 00:00:00',
	modify      	int(11) COMMENT '项目修改次数'  NULL DEFAULT '0',
	PRIMARY KEY(project_id)
)
COMMENT = '用户总项目表' 
GO

CREATE TABLE photo_demo  ( 
	id   	varchar(25) NULL,
	photo	mediumblob NULL,
	str  	longtext NULL 
	)
GO

