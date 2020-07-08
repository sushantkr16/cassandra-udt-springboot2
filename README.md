# Cassandra user defined data type (UDT)
User-defined types (UDTs) can attach multiple data fields, each named and typed, to a single column. 
The fields used to create a UDT may be any valid data type, including collections and other existing UDTs. 
Once created, UDTs may be used to define a column in a table.
 
 * Spring data cassandra, version should be above than : 1.5.0.RELEASE
 * Datastax java driver version used for this project : 1.8.1

### API reference

Rest service : 

* Http Method : GET
* URL : http://localhost:9090/sk/invitation?invitationDate=2020-07-22

* Http Method : POST
* URL : http://localhost:9090/sk/invitation
* Request Body :

        {
            "invitationDate":"2020-07-22",
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
            "invitationDate":"2020-07-22",
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


### Steps to follow 

1. We need to use the latest jar of Spring data Cassandra (2.2.6.RELEASE). Any version after 1.5.0.RELEASE is OK.

        group: 'org.springframework.data', name: 'spring-data-cassandra', version: '2.2.6.RELEASE'

2. Make sure it uses below versions of the jar :

   Use datastax java driver : 
   
       compile group: 'com.datastax.dse', name: 'dse-java-driver-core', version: '1.8.1' 
   
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
  
  
  TAGS : ksushant, sushant, spring boot 2, cassandra java driver, UDT, user defined data type