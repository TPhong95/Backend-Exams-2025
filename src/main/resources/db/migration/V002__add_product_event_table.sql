create table product_event
(
    id INTEGER primary key,
    stock_status varchar(25),
    timestamp timestamp,
    product_id INTEGER
);

create sequence product_event_seq increment by 1 start with 1;