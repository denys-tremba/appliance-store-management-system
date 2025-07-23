# Appliance Order Management System

## Use Cases

1. **End Order**: Client finds appliances of interest via catalog, enters appliance id (implicitly), quantity. System records order line item and presents appliance description, price, running total. Client ends order.

2. **Process Order**: Employee picks completed order among pending ones, either approves or disapproves it.

3. **Appliance Management**: Employee creates / edits / deletes appliance record.

4. **Manufacturer Management**

5. **Register/Login**: User (Client or Employee) enters credentials to register/login himself in the system.

6. **User’s Rights Management**: Admin locks/unlocks User’s account.

---

## Domain Rules

1. Client can have at most a single pending order at a time.
2. Client cannot enter line items into ended order.
3. Client cannot complete empty order.
4. Client can revoke only completed order.
5. Employee can approve/disapprove only ended order.
6. Orders are assigned to employees acc. deterministic policy (round robbin by default)

---

## Additional Features

1. Semantic search for appliances.
2. LLM-based chat support: appliance choice suggestions, order population.
3. Flexible filtering, sorting of appliances.
4. Stateless JWT-based:
   - Authentication (access & refresh token, claims & expiration validation)
   - Shopping cart tracking
5. Enforcing strong password creation during registration / edit.
6. Brute force login attack prevention (limiting failed attempts).
7. Email notifications on business logic events.
8. Optimistic locking (preventing lost updates) of appliance details editing.
9. Primitive statistics visualization: charts (e.g. order count by Employee).
10. Account deletion (“right to be forgotten”).
11. UI internationalization.

---

## Technical Details

- Spring Boot
- Thymeleaf
- Bootstrap
- JPA (Hibernate)
- H2
- Spring Security
- Spring AI
- Lombok
- Mapstruct


## Task description

In this exercise, you will implement an "Appliances shop" using Spring Boot.
Your application has to imitate work of an online shop.

Employees should be able to do the following:
1. show every table;
2. add, correct, and delete `Employee`, `Client`, `Appliance`, `Manufacturer`;
3. approve order.

Clients and employees should be able to do the following:
1. have navigation and provide access for permitted elements front-end;
2. add, correct, and delete order.

Users must be able to work in two languages(native and English). 

The class diagram of the Domain model is shown in the figure below:
![img.png](img.png)

Your application must implement next layers:
* Repository - work with necessary entities (JPA);
* Service - contains business logic your application;
* Controller - is front end your application. 

### Requirements:
* Use a skeleton application;
* Use H2 database to realisation storage;
* Use annotations to describe DB entities. List of entities:
  * `User`;
  * `Client`;
  * `Employee`;
  * `Manufacturer`;
  * `Appliance`;
  * `Order`;
  * `OrderRow`.
* Use Spring JPA technology;
* Use Spring Security;
* Implement internationalization and localization in controllers;
* Implement validation for the Domain model and controllers.

### Would be nice
* to use logging;
* to implement pagination and sorting in controllers;
* to explore other Spring technologies;
* to write tests for controllers and services.

### Recommendations
* Use `Lombok`
* Implement In-Memory Authentication(Spring Security).
* You have files with initial data, but you can use other data.
  To load initial data, use
  `spring.sql.init.data-locations=classpath:manufacturer.sql, classpath:client.sql, classpath:employee.sql, classpath:appliance.sql`
  in `application.properties`.
* Files with initial data located `resources`. The list of files includes `manufacturer.sql`, `client.sql`, `employee.sql`, `appliance.sql`.
* Additionally, you may to using files to frond-end from `resources\templates\**`, or use yourself front-end.  
* If you do not have enough time to implement all the requirements, you can choose to implement only part of the functionality. For example, you can focus on add, delete, and update for employees and not implement them for clients.

Good luck. You have only 15 hours