INSERT INTO "vehicle" (vehicle_id, model, manufacturer, price, count, type)
VALUES ('e616ad0b-ed91-41d4-a743-60d08e1d46c9', 'Model-861', 'KIA', 2000, 1, 'AUTO');
INSERT INTO "auto" (vehicle_id, body_type)
VALUES ('e616ad0b-ed91-41d4-a743-60d08e1d46c9', 'coupe');

INSERT INTO "vehicle" (vehicle_id, model, manufacturer, price, count, type)
VALUES ('2f77a386-99bd-4fc2-a33c-5916039d21fd', 'Model-345', 'BMW', 1000, 1, 'MOTORBIKE');
INSERT INTO "motorbike" (vehicle_id, create_motorbike, lean_angle, currency)
VALUES ('2f77a386-99bd-4fc2-a33c-5916039d21fd', '2022-09-05T19:34:09.724144', 55.0, '$');

INSERT INTO "vehicle" (vehicle_id, model, manufacturer, price, count, type)
VALUES ('bda3b742-d0e5-4bc1-9067-2e99a304b8ef', 'Model-421', 'BMW', 3000, 2, 'AIRPLANE');
INSERT INTO "airplane" (vehicle_id, number_of_passenger_seats)
VALUES ('bda3b742-d0e5-4bc1-9067-2e99a304b8ef', 100);