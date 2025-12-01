create table product(
id INTEGER primary key,
breed varchar(25),
description varchar(255),
price INTEGER,
stock_status varchar(25),
quantity INTEGER
);
create sequence product_seq increment by 1 start with 1;
