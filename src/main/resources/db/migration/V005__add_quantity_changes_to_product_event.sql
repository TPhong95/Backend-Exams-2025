alter table product_event
    add column previous_quantity INTEGER,
    add column incoming_quantity INTEGER,
    add column new_quantity INTEGER;
