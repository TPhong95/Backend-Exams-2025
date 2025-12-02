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

create table purchase(
    id INTEGER primary key,
    shipping_charge INTEGER,
    total_price INTEGER,
    customer_address_id INTEGER,
    address_id INTEGER
);

create sequence purchase_seq increment by 1 start with 1;

create table customer (
    id INTEGER primary key,
    name varchar(255)
);

create sequence customer_seq increment by 1 start with 1;

create table customer_address (
    id INTEGER primary key,
    street_name varchar(255),
    phone varchar(25),
    email varchar(25),
    customer_id INTEGER
);

create sequence address_seq increment by 1 start with 1;

create table purchase_event(
    id INTEGER primary key,
    shipped_status varchar(25),
    purchase_date timestamp,
    purchase_id INTEGER
);

create sequence purchase_event_seq increment by 1 start with 1;