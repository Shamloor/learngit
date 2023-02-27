create database if not exists Management;
use Management;

create table `User`(
`email` varchar(100) NOT NULL,
`username` varchar(100) NOT NULL,
`password` varchar(100) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

use Management;
insert into User(`email`,`username`,`password`)values(`130`,`sa`,`123456`);

select * from User;

