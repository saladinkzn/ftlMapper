insert into master
(id, name)
VALUES
  (1, 'abc'),
  (2, 'def');

insert into slave
  (id, name, master_id)
values
  (1, 'abc_slave_1', 1),
  (2, 'abc_slave_2', 1),
  (3, 'def_slave_1', 2),
  (4, 'def_slave_2', 2);

insert into person
(id, name, streetName, houseNumber)
VALUES
  (1, 'John Doe', 'Lenin street', '1A'),
  (2, 'Jane Doe', 'Trozki street', '2B');