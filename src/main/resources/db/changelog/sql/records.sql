CREATE EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO STOCKS_DB (uuid, name, price, amount, category, vendor_code, description, update_date, creation_date)
SELECT gen_random_uuid(), 'test', 1.0, 10, 'DRESS', gen_random_uuid(), CONCAT('test_description', CAST(i AS VARCHAR)), NOW(), NOW()
FROM generate_series(1, 1000000) AS t(i);