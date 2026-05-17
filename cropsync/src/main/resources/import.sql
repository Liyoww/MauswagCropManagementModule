-- Burahin ang luma
DELETE FROM TASK;
DELETE FROM CROP_TEMPLATE;

-- Insert nang walang manual ID (hayaang ang H2 ang mag-generate)
INSERT INTO CROP_TEMPLATE (CROP_TYPE, DAY_NUMBER, TASK_NAME) VALUES ('Eggplant', 1, 'Paghahanda ng Lupa');
INSERT INTO CROP_TEMPLATE (CROP_TYPE, DAY_NUMBER, TASK_NAME) VALUES ('Eggplant', 7, 'Paglilipat-Tanim');
INSERT INTO CROP_TEMPLATE (CROP_TYPE, DAY_NUMBER, TASK_NAME) VALUES ('Tomato', 1, 'Paghahanda ng Seedbed'); 