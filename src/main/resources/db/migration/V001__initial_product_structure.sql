create table product(
id INTEGER primary key,
breed varchar(25),
description varchar(255),
price INTEGER,
stock_status varchar(25),
quantity INTEGER
);
create sequence product_seq increment by 1 start with 1;


create table product_batch(
    id INTEGER primary key,
    quantity INTEGER,
    total_price INTEGER,
    purchase_id INTEGER,
    product_id INTEGER
);

create sequence product_batch_seq increment by 1 start with 1;
