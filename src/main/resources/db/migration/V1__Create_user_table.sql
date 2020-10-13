drop table if exists `user`;

create table user(
  id bigint primary key auto_increment ,
  username varchar(10) unique,
  encrypted_password varchar(100),
  avatar varchar(100),
  created_at datetime,
  updated_at datetime);

insert into user(username,encrypted_password, avatar,created_at,updated_at) values('testuser','testpassword','testavatar',now(),now());