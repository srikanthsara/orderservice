CREATE TABLE cart_item (

    cart_item_id BIGSERIAL PRIMARY KEY,

    cart_id BIGINT
        REFERENCES cart_master(cart_id),

    product_id VARCHAR(50),

    product_name VARCHAR(255),

    category VARCHAR(100),

    brand VARCHAR(100),

    quantity INT,

    unit_price NUMERIC(12,2),

    total_price NUMERIC(12,2)
);