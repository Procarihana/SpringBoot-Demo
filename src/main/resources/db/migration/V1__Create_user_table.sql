drop table if exists `user`;

create table user(id bigint primary key auto_increment ,
  username varchar(10) unique,
  encrypted_password varchar(100),
  avatar varchar(100),
  created_at datetime,
  updated_at datetime);