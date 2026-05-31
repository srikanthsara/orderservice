ALTER TABLE order_item

ALTER COLUMN unit_price
TYPE NUMERIC(19,2)

USING unit_price::numeric;

ALTER TABLE order_item

ALTER COLUMN total_price
TYPE NUMERIC(19,2)

USING total_price::numeric;

ALTER TABLE order_master

ALTER COLUMN sub_total
TYPE NUMERIC(19,2)

USING sub_total::numeric;

ALTER TABLE order_master

ALTER COLUMN gst_amount
TYPE NUMERIC(19,2)

USING gst_amount::numeric;

ALTER TABLE order_master

ALTER COLUMN shipping_charges
TYPE NUMERIC(19,2)

USING shipping_charges::numeric;

ALTER TABLE order_master

ALTER COLUMN discount_amount
TYPE NUMERIC(19,2)

USING discount_amount::numeric;

ALTER TABLE order_master

ALTER COLUMN total_amount
TYPE NUMERIC(19,2)

USING total_amount::numeric;