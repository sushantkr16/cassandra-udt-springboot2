##User-defined types (UDTs)

Cassandra being a NO-SQL database, de-normalization and duplication of data is
allowed and considered as normal. Cassandra supports many pre-defined data types 
and one of them is User-Defined data types (UDTs). 

### So what exactly an User-defined types (UDTs) is?

User-defined types (UDTs) are a kind of custom data type which can be completely 
based on technical requirements and can have one or more data fields. These data fields 
can have their own individual valid data types including collection ( SET, LIST, 
MAP) and all of these fields are mapped to a single column. 

### The benefit of this custom data type is : 
It allows you to group multiple fields and store nested objects(POJO's/Domain) in the 
same table rather than in an other separate table. 

### Didn't understand? Fine let's take a scenario:

In a real world, we face a lot of scenarios where we want to have a table which 
can store a nested POJO kind of object. 

For e.g Lets say we have a marathon planned. We want to make sure, we have all 
the information related to a marathon like name, venue, distance, date etc.. 
are there and also the basic information of the person who will win the particular 
marathon. Now to have this kind of data stored in DB, 

1. Either we need to have 2 different tables : Might not be feasible
2. Or all the columns individually stored : Which makes columns cluttered in the row.

### Ok.. understood the problem but how does sUDT helps?

If we use UDT, we can have a table with all the marathon details and can have an UDT
to store the winner details. Now this UDT can be stored  as a single column in the 
table. So when you see the way it is stored in DB and also pull the data, it is very 
easy to read, figure out the data stored. For e.g. 

{
    "marathonDate":"2020-07-26",
    "marathonName":"LA marathon",
    "distance":"50m",
    "venue":"LA",
    "winner": {
        "firstName": "Jhon",
        "lastName": "Doe",
        "marathonParticipantNumber": "2345"
    }
}

### It's about Flexibility and not performance:

User defined types offers flexibility in storing the user required details in easily
readable formats. Its doesn't improves the performance in a great way. Datastax provides
drivers for various languages which serializes and deserializes the data to POJO's/Domain
objects.