CREATE KEYSPACE cassandra_udt WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TYPE cassandra_udt.address_type (
first_name text,
middle_initial text,
last_name text,
street_name text,
additional_address text,
city text,
state text,
zip text,
country text,
phone text,
);


CREATE TABLE IF NOT EXISTS cassandra_udt.invitation (
invitation_date date,
invitation_to text,
invitation_id uuid,
invitation_type text,
invitation_message text,
invitation_from text,
address frozen<cassandra_udt.address_type>,
primary key(invitation_date, invitation_to, invitation_id)
) with comment='Cassandra UDT Table';