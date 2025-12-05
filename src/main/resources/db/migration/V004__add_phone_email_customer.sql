alter table customer
add column primary_phone varchar(50) not null default 'no phone_number',
add column primary_email varchar(255) not null default 'email';