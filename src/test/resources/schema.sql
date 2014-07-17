create table master (
  id bigint primary key,
  name varchar(255)
);

create table slave (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255),
  master_id bigint null REFERENCES master(id)
);

create table person (
  id BIGINT PRIMARY KEY,
  name varchar(255),
  streetName varchar(255),
  houseNumber varchar(255)
)