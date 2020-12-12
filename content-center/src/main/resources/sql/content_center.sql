CREATE DATABASE content_center;
use content_center;

create table content_center.share
(
	id int auto_increment
		primary key,
	user_id int null,
	title varchar(128) null,
	create_time timestamp null,
	update_time timestamp null,
	is_original tinyint null,
	author varchar(20) null,
	cover varchar(128) null,
	summary varchar(128) null,
	price int null,
	download_url varchar(128) null,
	buy_count int null,
	show_flag tinyint null,
	audit_status varchar(8) null,
	reason varchar(128) null
);

create table content_center.rocketmq_transaction_log
(
	id int auto_increment
		primary key,
	transaction_id varchar(128) null,
	log varchar(64) null
);



