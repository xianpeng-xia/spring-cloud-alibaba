CREATE DATABASE user_center;
use user_center;
create table user_center.user
(
	id int auto_increment
		primary key,
	username varchar(32) null comment '用户名',
	email varchar(32) null comment '邮箱',
	create_time timestamp null comment '创建时间',
	update_time timestamp null comment '更新时间'
);


create table user_center.bonus_event_log
(
	id int auto_increment
		primary key,
	user_id int null,
	value int null,
	event varchar(128) null,
	create_time int null,
	description varchar(512) null
);


