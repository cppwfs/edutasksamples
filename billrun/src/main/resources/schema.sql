CREATE TABLE IF NOT EXISTS BILL_STATEMENTS
(
   id int,
   first_name varchar(50),
   last_name varchar(50),
   minutes int,
   data_usage int,
   bill_amount double
);
CREATE TABLE IF NOT EXISTS BILL_USAGE
(
   id int,
   first_name varchar(50),
   last_name varchar(50),
   minutes int,
   data_usage int
);