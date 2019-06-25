CREATE KEYSPACE interpolation WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

USE interpolation;

CREATE TABLE sample (id uuid primary key,
                     sensorName varchar,
                     variable varchar,
                     units varchar,
                     time timestamp,
                     value double);

CREATE TABLE sensor (id uuid primary key,
                     baseHeight double,
                     active boolean,
                     sensorHeight double,
                     name varchar,
                     latest timestamp,
                     type varchar,
                     longitude double,
                     latitude double);
