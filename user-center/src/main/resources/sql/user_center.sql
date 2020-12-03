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



