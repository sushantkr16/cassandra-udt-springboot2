## Overview / Getting started : Cassandra user defined data type (UDT) with Spring boot 2

  # Table of contents
  * [Cassandra user defined data type (UDT)](#cassandra-user-defined-data-type-udt)
  * [Tools and Technologies](#tools-and-technologies)
  * [Prerequisites](#prerequisites)
  * [Setting up in local](#setting-up-in-local)
  * [Database](#database)
  * [Spring boot 2 dependency](#spring-boot-2-dependency)
  * [Steps to follow to create an UDT](#steps-to-follow-to-create-an-udt)
  * [Api Reference](#api-reference)
  
  ### Cassandra user defined data type (UDT): 
  
  User-defined types (UDTs) can attach multiple data fields, each named and typed, to a single column. 
  The fields used to create a UDT may be any valid data type, including collections and other existing UDTs. 
  Once created, UDTs may be used to define a column in a table.
  
  Link : https://docs.datastax.com/en/cql-oss/3.3/cql/cql_using/useCreateUDT.html
   
   * Spring version : 2.2.6.RELEASE (should be above than : 1.5.0.RELEASE)
   * Datastax java driver version used for this project : 3.9.0
  
  ### Tools and Technologies 
  
   1. Gradle (5 and above) : https://gradle.org/install/
   2. IntelliJ or any editor : https://www.jetbrains.com/idea/download/
   3. Spring boot 2 : https://start.spring.io/
   4. Datastax Cassandra 3.x : https://downloads.datastax.com/#enterprise
   5. OR Apache Cassandra : https://cassandra.apache.org/
  
  Can be downloaded from the links.
  
  ### Prerequisites
  
  1. Gradle : Gradle should be installed and working. We should be able to build with gradle.
  2. IntelliJ or any editor : Intellij or any editor to see the code.
  3. Datastax Cassandra 3.x OR Apache Cassandra
  
  Either datastax  cassandra or apache cassandra should be running.
  
  ### Setting up in local
  
  Steps to follow to set up in local :
  
  ```shell
  
  git clone https://github.com/sushantkr16/cassandra-udt-springboot2.git
  cd your-project/
  gradle clean build
  use the cql file (provided in the resources directory) to create keyspace and table
  start the application by running CassandraUDTSpringBoot2Application main class
  
  ```
  ### Database
  
  Database used is Cassandra. Cassandra dependency : https://docs.datastax.com/en/developer/java-driver/3.9/
  
  ```shell
  compile group: 'com.datastax.cassandra', name: 'cassandra-driver-core', version: '3.9.0'
  compile group: 'com.datastax.cassandra', name: 'cassandra-driver-mapping', version: '3.9.0'
  compile group: 'com.datastax.cassandra', name: 'cassandra-driver-extras', version: '3.9.0'
   ```
 Keyspace and table details are present in the invitation.cql file which is in resources directory. Please create keyspace and table before procceding.
 
 ### Spring boot 2 dependency
 
 ```shell
 implementation 'org.springframework.boot:spring-boot-starter-actuator'
 implementation 'org.springframework.boot:spring-boot-starter-web'
 implementation ("org.springframework.data:spring-data-cassandra") {
 	exclude group: 'com.datastax.cassandra', module: 'cassandra-driver-core'
 }
 ```
 Removed the sparing data cassandra dependency as we need to use the datastax java driver.
 
 ### Steps to follow to create an UDT

1. We need to use the latest jar of Spring data Cassandra (2.2.6.RELEASE). Any version after 1.5.0.RELEASE is OK.

        group: 'org.springframework.data', name: 'spring-data-cassandra', version: '2.2.6.RELEASE'

2. Make sure it uses below versions of the jar :

   Use datastax java driver : 
   
       compile group: 'com.datastax.cassandra', name: 'cassandra-driver-core', version: '3.9.0'
       compile group: 'com.datastax.cassandra', name: 'cassandra-driver-mapping', version: '3.9.0'
       compile group: 'com.datastax.cassandra', name: 'cassandra-driver-extras', version: '3.9.0' 
   
3. Create user defined type in Cassandra. The type name should be same as defined in the POJO class or Vice versa. 
   
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

4. Create domain class for the user defined type : We need to make sure that column name in the user defined type 
   schema has to be same as field name in the domain class.

        @UserDefinedType("address_type")
        public class Address {
        
           @CassandraType(type = DataType.Name.TEXT)
           @Column("first_name")
           private String firstName;
           
        }   
    
5. Create column-family with one of the columns as UDT in Cassandra. 

        Syntax : address frozen<cassandra_udt.address_type>,

        Table :
        
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

5. In the domain class, define the field with annotation -CassandraType and DataType should be UDT:

        @Table("invitation") 
        public class Invitation { 
            -- other fields-- 
            
            @CassandraType(type = DataType.Name.UDT, userTypeName = "address_type")
            @Column("address")
            private Address address; 
        }

7. In the Cassandra Config, Change this :

        @Bean public CassandraMappingContext mappingContext() throws Exception { 
            BasicCassandraMappingContext mappingContext = new BasicCassandraMappingContext(); 
            mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), cassandraKeyspace)); 
            return mappingContext; 
        }

8. User defined type should have the same name across everywhere. for e.g

        @UserDefinedType("address_type")
        @CassandraType(type = DataType.Name.UDT, userTypeName = "address_type")
        CREATE TYPE address_type

9. It will be stored in the table like below. If you see, address has been stored in one column.

       2020-07-26 | ksushant | 3d022412-31db-419e-b329-63bbbc418970 | 
 
       {first_name: 'Test', middle_initial: null, last_name: 'address', street_name: '1314 Marquette Ave', additional_address: 'apt#1208', city: 'MINNEAPOLIS', state: 'MN', zip: '55403', country: 'US', phone: '6124454533'}
 
       | sk | Are you coming to the party? | Birthday

 
  ### Api Reference
  
    * Http Method : GET
    * URL : http://localhost:9090/sk/invitation?invitationDate=2020-07-26

    * Http Method : POST
    * URL : http://localhost:9090/sk/invitation
    * Request Body :

        {
            "invitationDate":"2020-07-26",
            "invitationTo":"ksushant",
            "invitationType":"Birthday",
            "invitationMessage":"Please come to the party",
            "invitationFrom":"sk",
            "venueAddress": {
                "streetName": "1314 Marquette Ave",
                "additional_address": "apt#1208",
                "city": "MINNEAPOLIS",
                "state": "MN",
                "country": "US",
                "phone": "6124454533",
                "zip": "55403",
                "firstName": "Test",
                "lastName": "address"
            }
        
        }

    * Http Method : PUT
    * URL : http://localhost:9090/sk/invitation/{invitationId}

        {
            "invitationDate":"2020-07-26",
            "invitationTo":"ksushant",
            "invitationType":"Birthday",
            "invitationMessage":"Are you coming to the party?",
            "invitationFrom":"sk",
            "venueAddress": {
                "streetName": "1314 Marquette Ave",
                "additional_address": "apt#1208",
                "city": "MINNEAPOLIS",
                "state": "MN",
                "country": "US",
                "phone": "6124454533",
                "zip": "55403",
                "firstName": "Test",
                "lastName": "address"
            }
        
        }