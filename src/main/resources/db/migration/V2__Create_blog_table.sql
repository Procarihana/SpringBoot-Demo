drop table if exists `blog`;

create table `blog`(
`id` bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
user_id bigint(20),
title varchar(100),
description varchar(100),
content TEXT,
at_index tinyint(1) UNSIGNED NOT NULL COMMENT'0-> no_at_index, 1-> at_index',
created_at datetime,
updated_at datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES user(id)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

insert into `blog`
(user_id,title,description,content,at_index,created_at)
value(1,'Hello','Say Hello','H E L L O !~',1,NOW());