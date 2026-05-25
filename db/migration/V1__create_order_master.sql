CREATE TABLE order_item (

    order_item_id BIGSERIAL PRIMARY KEY,

    order_id BIGINT
        REFERENCES order_master(order_id),

    product_id VARCHAR(50),

    product_name VARCHAR(255),

    quantity INT,

    unit_price NUMERIC(12,2),

    total_price NUMERIC(12,2)
);