INSERT INTO STOCKS (id, name, price, amount, category, vendor_code, description, update_date, creation_date)
SELECT RANDOM_UUID(), 'test', 100.05, 10, 'DRESS', RANDOM_UUID(), CONCAT('test_description', CAST(i AS VARCHAR)), NOW(), NOW()
FROM generate_series(1, 1000000) AS t(i);