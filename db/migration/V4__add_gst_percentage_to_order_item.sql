ALTER TABLE order_item
ADD COLUMN gst_percentage NUMERIC(5,2);

ALTER TABLE order_item
ADD COLUMN gst_amount NUMERIC(12,2);