# Property Management System

This project implements a CRUD (Create, Read, Update, Delete) system for managing real estate properties. It uses a Spring Boot backend and a frontend built with HTML, CSS, and JavaScript, along with a MySQL database. The project is deployed on AWS, with security features such as HTTPS via Apache for the frontend, and TLS for secure communication between the backend and frontend.

![AWS](https://github.com/alexandrac1420/Seguridad_Nube/blob/master/Pictures/Funcionamiento%20AWS.gif)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine and on AWS for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java (version 17)**
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "17.0.7"
    OpenJDK Runtime Environment (build 17.0.7+7-LTS)
    OpenJDK 64-Bit Server VM (build 17.0.7+7-LTS, mixed mode, sharing)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Docker**(For local deployment)
    - Install Docker by following the instructions [here](https://docs.docker.com/get-docker/).
    - Verify the installation:
    ```sh
    docker --version
    ```

4. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.31.1
    ```
---
## How to Use the Property Management System

The Property Management System allows users to manage real estate properties by providing functionalities such as creating new properties, updating existing properties, deleting properties, and searching for properties by address. Additionally, the system supports pagination for easier navigation through the property listings.

### Features:
1. **Add New Properties**: Create new property entries by providing address, price, size, and description.
2. **Update Existing Properties**: Modify the details of a property, such as its price or description.
3. **Delete Properties**: Remove properties that are no longer needed.
4. **Search by Address**: Easily search for properties based on their address.
5. **Pagination**: Navigate through property listings with paginated results.
6. **User Feedback**: The system displays success, error, and validation messages to provide feedback during property management operations.

---

## Secure Login with Hashing

In this system, user passwords are securely stored using hashing techniques to prevent storing plain text passwords. We utilize the `BCrypt` algorithm, which is a strong and widely-used password hashing function that incorporates a salt to protect against rainbow table attacks.

When a user registers or updates their password, the system applies the `BCrypt` hashing algorithm before storing the password in the database. This ensures that even if the database is compromised, the actual passwords cannot be easily retrieved or decoded.

Additionally, during login, the provided password is hashed and compared with the stored hash to authenticate users securely.

By using `BCrypt`, the system ensures that password storage adheres to modern security standards.



## Running the Project Locally (with Docker)

### Steps to Run Locally:

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Seguridad_Nube.git
    cd Seguridad_Nube
    ```

2. Start the application with Docker Compose:
    ```sh
    docker-compose up -d
    ```

    This will set up both the Spring Boot backend and a MySQL container locally. The backend will be available at `http://localhost:8443`, and the MySQL instance will run inside a Docker container.

3. Build the project using Maven:
    ```sh
    mvn package
    ```

4. Run the application:
    ```sh
    java -jar target/Patrones-0.0.1-SNAPSHOT.jar
    ```

5. Access the application:
    ```sh
    http://localhost:8443
    ```

---
## Running the Project on AWS

This project uses two **EC2 instances**: one for running **MySQL** and another for running the **Spring Boot backend**. Below are the detailed steps for setting up both instances, along with the Apache server for the frontend.

---

### 1. **Setting Up MySQL on EC2**

1. **Create an EC2 instance** for the MySQL database using Amazon Linux 2 as the operating system.

2. **Connect to the EC2 instance**:
    ```bash
    ssh -i your-key.pem ec2-user@<mysql-ec2-instance-ip>
    ```

3. **Install MySQL**:
    ```bash
    sudo yum update -y
    sudo yum install -y https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
    sudo yum install -y mysql-community-server
    ```

4. **Start MySQL** and enable it to run at boot:
    ```bash
    sudo systemctl start mysqld
    sudo systemctl enable mysqld
    ```

5. **Get the MySQL temporary password**:
    ```bash
    sudo grep 'temporary password' /var/log/mysqld.log
    ```

6. **Secure the MySQL installation**:
    ```bash
    sudo mysql_secure_installation
    ```

7. **Create the MySQL database and user**:
    ```sql
    CREATE DATABASE mydatabase;
    CREATE USER 'myuser'@'%' IDENTIFIED BY 'myP@ssw0rd123!';
    GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'%';
    FLUSH PRIVILEGES;
    ```

8. **Allow external connections** to MySQL:
    Edit the MySQL config file:
    ```bash
    sudo nano /etc/my.cnf
    ```
    Add the following line under `[mysqld]`:
    ```bash
    bind-address = 0.0.0.0
    ```

9. **Restart MySQL**:
    ```bash
    sudo systemctl restart mysqld
    ```

10. **Verify MySQL is running on port 3306**:
    ```bash
    sudo netstat -tuln | grep 3306
    ```

11. **Update AWS Security Groups**:
    Ensure port `3306` is open for the MySQL EC2 instance to allow external connections. Similarly, ensure port `8080` is open for the Spring Boot backend to enable external access.

---

### 2. **Setting Up Spring Boot Backend on Another EC2 Instance**

1. **Create another EC2 instance** for the backend and connect via SSH:
    ```bash
    ssh -i your-key.pem ec2-user@<backend-ec2-instance-ip>
    ```

2. **Install Java and Maven**:
    ```bash
    sudo yum install java-17-amazon-corretto -y
    sudo yum install maven -y
    ```

3. **Transfer the JAR file** to the EC2 instance using SFTP:
   
   Connect using SFTP with your private key:
   ```bash
   sftp -i your-key.pem ec2-user@<backend-ec2-instance-ip>
   ```
   Use the `put` command to upload the JAR file:
   ```bash
   put target/Patrones-0.0.1-SNAPSHOT.jar
   ```

4. **Run the Spring Boot application**:
    ```bash
    java -jar Patrones-0.0.1-SNAPSHOT.jar
    ```

---

### 3. **Setting Up Apache Server for the Frontend**

1. **Create an EC2 instance** for the frontend and install Apache:
    ```bash
    sudo yum install httpd -y
    sudo systemctl start httpd
    sudo systemctl enable httpd
    ```

2. **Set up HTTPS with Let's Encrypt**:
    Install Certbot and configure Apache to serve content over HTTPS:
    ```bash
    sudo yum install certbot python3-certbot-apache -y
    sudo certbot --apache -d serverfront.duckdns.org
    ```
    This will automatically configure Apache to use the Let's Encrypt certificate.

3. **Deploy frontend files**:
    Copy your frontend files (HTML, CSS, JS) to `/var/www/html/`:
    ```bash
    sudo cp -r /path/to/your/frontend/* /var/www/html/
    ```

4. **Ensure the firewall allows HTTPS traffic**:
    Ensure that port `443` is open for the Apache server in the AWS Security Group.

---

### 4. **Configuring SSL for the Spring Boot Backend**

1. **Generate SSL certificate using Let's Encrypt on the backend instance**:
    Install Certbot and obtain the certificate:
    ```bash
    sudo yum install certbot -y
    sudo certbot certonly --manual --preferred-challenges dns -d serverspring.duckdns.org
    ```

2. **Convert the SSL certificate to PKCS12 for Spring Boot**:
    ```bash
    sudo openssl pkcs12 -export -in /etc/letsencrypt/live/serverspring.duckdns.org/fullchain.pem     -inkey /etc/letsencrypt/live/serverspring.duckdns.org/privkey.pem     -out keystore.p12 -name springboot     -CAfile /etc/letsencrypt/live/serverspring.duckdns.org/chain.pem -caname root
    ```

3. **Configure Spring Boot to use the SSL certificate**:
    In your `application.properties` file, add the following configuration:
    ```properties
    server.port=8443
    server.ssl.key-store=classpath:keystore.p12
    server.ssl.key-store-password=your_password
    server.ssl.key-store-type=PKCS12
    server.ssl.key-alias=springboot
    ```

4. **Open port 8443**:
    Ensure that port `8443` is open in the AWS Security Group for the backend EC2 instance to allow HTTPS traffic.

   ![image](https://github.com/user-attachments/assets/8e3a380c-f72f-42e1-a794-10ba898f1831)


---
## 5. **Using DuckDNS for Domain Setup**

1. **Go to [DuckDNS](https://www.duckdns.org/domains)** and register for a free account.

2. **Create a subdomain** for each of your EC2 instances, one for the frontend (e.g., `serverfront.duckdns.org`) and another for the backend (e.g., `serverspring.duckdns.org`).

3. **Update your DNS records** on DuckDNS with the **public IP addresses** of your EC2 instances.

4. Ensure the domains (`serverfront.duckdns.org` and `serverspring.duckdns.org`) are correctly mapped to the respective public IPs for accessing the frontend and backend via HTTPS.
   ![Domain back](https://github.com/alexandrac1420/Seguridad_Nube/blob/master/Pictures/serverSpring.png)
   ![Domain front](https://github.com/alexandrac1420/Seguridad_Nube/blob/master/Pictures/serverFront.png)
---

### 6. **Configuring VirtualHost for Apache**

1. **Edit the Apache VirtualHost configuration**:
    ```bash
    sudo nano /etc/httpd/conf.d/serverfront.conf
    ```

2. **Add the following configuration**:
    ```apache
    <VirtualHost *:80>
        ServerName serverfront.duckdns.org
        DocumentRoot /var/www/html

        ErrorLog /var/log/httpd/serverfront_error.log
        CustomLog /var/log/httpd/serverfront_access.log combined
    </VirtualHost>

    <VirtualHost *:443>
        ServerName serverfront.duckdns.org
        DocumentRoot /var/www/html

        SSLEngine on
        SSLCertificateFile /etc/letsencrypt/live/serverfront.duckdns.org/fullchain.pem
        SSLCertificateKeyFile /etc/letsencrypt/live/serverfront.duckdns.org/privkey.pem

        ErrorLog /var/log/httpd/serverfront_error_ssl.log
        CustomLog /var/log/httpd/serverfront_access_ssl.log combined
    </VirtualHost>
    ```

3. **Restart Apache** to apply changes:
    ```bash
    sudo systemctl restart httpd
    ```

---


## Additional Changes for AWS Deployment

During the deployment to AWS, certain configurations were updated to ensure seamless communication between the frontend and backend. The most significant changes involved updating the domains from localhost to the public DuckDNS domains for each EC2 instance, configuring CORS, and modifying the security settings. Below is the step-by-step breakdown of these updates.

### 1. Modifications to application.properties in the Spring Boot Backend

In the Spring Boot backend, the `application.properties` file was updated to point to the MySQL database running on the MySQL EC2 instance, using the DuckDNS domain:

```properties
spring.datasource.url=jdbc:mysql://serverfront.duckdns.org:3306/mydatabase
spring.datasource.username=myuser
spring.datasource.password=myP@ssw0rd123!
```

This ensures the backend connects to the remote MySQL EC2 instance via its DuckDNS domain rather than trying to connect to a local database.

### 2. CORS Configuration in `UserController` and `PropertyController`

Since the frontend and backend are hosted on different domains (the frontend on `serverfront.duckdns.org` and the backend on `serverspring.duckdns.org`), Cross-Origin Resource Sharing (CORS) needed to be configured.

In the Spring Boot controllers, the `@CrossOrigin` annotation was applied to allow requests from the frontend to the backend:

```java
@CrossOrigin(origins = "https://serverfront.duckdns.org", allowCredentials = "true")
@RestController
@RequestMapping("/users")
public class UserController {
    // User registration and authentication endpoints
}

@CrossOrigin(origins = "https://serverfront.duckdns.org", allowCredentials = "true")
@RestController
@RequestMapping("/properties")
public class PropertyController {
    // Property management endpoints
}
```

This configuration ensures that the frontend (served by Apache) can send authenticated requests to the backend, allowing credentials such as session cookies to be included in the requests.

### 3. CORS Configuration in SecurityConfig

The CORS configuration also needed to be handled at the security level in the `SecurityConfig` class. In Spring Security, the following adjustments were made to enable CORS and disable CSRF for simplicity in this environment:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow cookies and credentials
        config.setAllowedOrigins(Arrays.asList("https://serverfront.duckdns.org")); // Allow frontend domain
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Allowed headers
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

This ensures that:

- CORS is enabled across the entire backend, allowing requests from the frontend (`serverfront.duckdns.org`).
- Credentials such as cookies or authentication tokens are allowed in cross-origin requests.
- CSRF protection is disabled for simplicity, which is common in microservices that use JWT or similar tokens.

### 4. Modifications to the Frontend

The frontend, which is served by Apache on the frontend EC2 instance, had its API calls updated to point to the backend's DuckDNS domain (`serverspring.duckdns.org`). The following updates were made in the JavaScript files:

```javascript
const apiBaseUrl = 'https://serverspring.duckdns.org:8443';

fetch(`${apiBaseUrl}/users/register`, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        username: 'testuser',
        password: 'password123'
    }),
    credentials: 'include' // Ensure credentials (cookies) are included
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

This ensures that the frontend communicates with the backend over HTTPS, using the correct domain.

    
## Architecture
![Architecture Diagram](https://github.com/alexandrac1420/Securidad_Nube/blob/master/Pictures/Arquitectura.png)
### Overview

The Property Management System is designed using a client-server architecture that consists of three main components: the frontend, the Spring Boot backend, and the MySQL database. The system supports secure communication, using HTTPS for data transmission between the frontend and backend, as well as secure database interactions through TLS connections. The architecture is designed to ensure scalability, security, and separation of concerns, with each component having a clearly defined role.

### Components

#### 1. **Frontend (HTML/JavaScript/CSS)**
   - **Role**: The frontend is responsible for providing the user interface (UI) that allows users to interact with the system to manage properties. It is a static web interface built using HTML, JavaScript, and CSS.
   - **Responsibilities**:
     - Display property listings in a tabular format.
     - Provide forms to create new properties and update existing ones.
     - Facilitate search functionality for properties by address.
     - Show feedback and validation messages for user actions (e.g., success or error messages).
     - Communicate with the Spring Boot backend via HTTP requests using JavaScript's `fetch` API.
     - Secure the communication channel using HTTPS, ensuring the integrity and confidentiality of data sent between the user’s browser and the server.

#### 2. **Spring Boot Backend (API)**
   - **Role**: The backend is the core component that provides the RESTful API for property management. It implements the business logic for handling CRUD operations (Create, Read, Update, Delete) on properties and interacts with the MySQL database for data persistence.
   - **Responsibilities**:
     - Serve as the intermediary between the frontend and the MySQL database.
     - Expose RESTful APIs for property-related operations, such as:
       - **Creating** new properties.
       - **Retrieving** a list of properties (with pagination support).
       - **Updating** existing property details.
       - **Deleting** properties by their unique identifier.
       - **Searching** properties by address with pagination.
     - Use the `PropertyController` class to handle HTTP requests and delegate business logic to the `PropertyService` layer.
     - Implement security by securing API endpoints with HTTPS and enabling cross-origin resource sharing (CORS) for the frontend.
     - Handle data validation and ensure that only valid data is persisted into the MySQL database.
     - Implement paginated results to manage large property datasets efficiently and prevent performance bottlenecks.

#### 3. **MySQL Database**
   - **Role**: The MySQL database is responsible for storing and managing all property data. It persists records of properties, including details like address, price, size, and description.
   - **Responsibilities**:
     - Store property information in a structured format within the `properties` table.
     - Ensure data persistence for the entire system, allowing CRUD operations to be performed.
     - Handle queries from the backend efficiently, including searching for properties by address and paginating results.
     - Secure the database connection by enforcing TLS encryption, ensuring secure data transmission between the backend and the database.
     - Support relational database operations such as JOINs and transactions, ensuring data integrity during updates or deletions.

### Interaction Flow

1. **User Interaction with the Frontend**:  
   Users interact with the web-based UI through forms to create, update, search, or delete properties. The user interface is intuitive and responsive, ensuring ease of use for property management.

2. **HTTP Requests to the Backend (Spring Boot)**:  
   The frontend sends HTTP requests (GET, POST, PUT, DELETE) to the backend. These requests are handled by the `PropertyController`, which processes the incoming data and applies the necessary business logic. For example, when a user submits a form to add a new property, the frontend sends a POST request containing the property data.

3. **Backend Data Processing and Validation**:  
   Upon receiving requests, the backend validates the data (e.g., checking for valid property prices or sizes). The backend then interacts with the database using the `PropertyService` and `PropertyRepository` to perform CRUD operations.

4. **Database Interaction (MySQL)**:  
   The backend performs queries on the MySQL database to either retrieve data (e.g., searching for properties) or modify data (e.g., adding or updating properties). The MySQL database ensures that all data is stored securely and efficiently.

5. **JSON Responses to the Frontend**:  
   The backend sends JSON-formatted responses to the frontend. These responses contain the data requested by the user (e.g., a list of properties) or confirmation messages for successful operations (e.g., a property was successfully created).

6. **Frontend Updates**:  
   The frontend updates the user interface dynamically based on the JSON response received from the backend. For instance, when a property is successfully added, the new property is displayed in the property listing without requiring a page reload.

### Security

- **HTTPS (TLS) for Frontend-Backend Communication**:  
   The system enforces HTTPS for all communication between the frontend and the Spring Boot backend. This ensures that data transmitted over the network is encrypted and secure, protecting against eavesdropping or man-in-the-middle attacks.
   
- **Cross-Origin Resource Sharing (CORS)**:  
   The backend enables CORS to allow the frontend, which may be hosted on a different domain or port, to securely interact with the backend APIs. This is crucial for preventing security issues related to browser restrictions on cross-origin requests.

- **Database Security**:  
   The MySQL database enforces secure connections using TLS, ensuring that all data transmitted between the backend and the database remains confidential and tamper-proof.

### Scalability and Future Enhancements

- The system is designed to be scalable by deploying the frontend, backend, and database on separate servers (EC2 instances). This separation of concerns allows each component to be scaled independently based on demand. For example:
   - The frontend can be served from a CDN or a load-balanced group of Apache servers.
   - The backend API can be scaled horizontally by adding more instances behind a load balancer.
   - The database can be replicated and distributed for high availability and scalability.
   
- Future enhancements could include the implementation of user authentication and role-based access control, allowing only authorized users to manage properties.


## Class Diagram

![Class Diagram](https://github.com/alexandrac1420/Securidad_Nube/blob/master/Pictures/DiagramaClases.png)


### Overview

The class diagram provides a detailed view of the backend structure of the Property Management System. It illustrates the relationships between the main classes, such as `PropertyController`, `PropertyService`, and `PropertyRepository`, as well as their roles in the system. The backend follows a typical layered architecture, where each layer (Controller, Service, Repository) has specific responsibilities. The diagram also includes the `Property` and `User` entities, which are mapped to tables in the MySQL database using JPA (Java Persistence API).

### Class Descriptions

#### 1. **PropertyController**
   - **Role**: The `PropertyController` acts as the entry point for HTTP requests from the frontend. It handles incoming requests related to properties and delegates the processing to the `PropertyService`.
   - **Responsibilities**:
     - Handle CRUD (Create, Read, Update, Delete) requests for properties.
     - Expose API endpoints for the frontend to interact with the backend.
     - Handle requests for searching properties by address and implementing pagination.
     - Return JSON responses to the frontend.
   - **Key Methods**:
     - `getAllProperties(Pageable pageable)`: Retrieves all properties with pagination.
     - `createProperty(Property property)`: Creates a new property.
     - `updateProperty(Long id, Property property)`: Updates an existing property.
     - `deleteProperty(Long id)`: Deletes a property by ID.
     - `searchProperties(String address, Pageable pageable)`: Searches for properties by address.

#### 2. **PropertyService**
   - **Role**: The `PropertyService` contains the business logic for managing properties. It interacts with the `PropertyRepository` to persist or retrieve data from the MySQL database.
   - **Responsibilities**:
     - Implement business logic for property management, such as creating, updating, deleting, and retrieving properties.
     - Validate incoming data before passing it to the repository for persistence.
     - Ensure that pagination and search functionality work efficiently.
   - **Key Methods**:
     - `createProperty(Property property)`: Validates and saves a new property to the database.
     - `getPropertyById(Long id)`: Retrieves a property by its ID.
     - `updateProperty(Long id, Property property)`: Updates the details of an existing property.
     - `deleteProperty(Long id)`: Deletes a property from the database.
     - `searchPropertiesByAddress(String address, Pageable pageable)`: Searches properties by address with pagination.

#### 3. **PropertyRepository**
   - **Role**: The `PropertyRepository` is an interface that provides access to the MySQL database using JPA (Java Persistence API). It extends the `JpaRepository` interface, which provides built-in CRUD operations.
   - **Responsibilities**:
     - Perform database operations, such as saving, finding, and deleting properties.
     - Handle paginated queries to retrieve properties from the database.
     - Execute custom queries, such as finding properties by address.
   - **Key Methods**:
     - `save(Property property)`: Persists a property to the database.
     - `findById(Long id)`: Retrieves a property by its ID.
     - `findAll(Pageable pageable)`: Retrieves all properties with pagination.
     - `deleteById(Long id)`: Deletes a property by its ID.
     - `findByAddress(String address, Pageable pageable)`: Searches for properties by address.

#### 4. **Property**
   - **Role**: The `Property` class represents a real estate property and is mapped to the `properties` table in the MySQL database using JPA annotations. It serves as the model for property data in the system.
   - **Responsibilities**:
     - Define the structure of property data, including attributes like address, price, size, and description.
     - Provide getter and setter methods for each attribute.
   - **Attributes**:
     - `id (Long)`: The unique identifier of the property.
     - `address (String)`: The address of the property.
     - `price (Double)`: The price of the property.
     - `size (Double)`: The size of the property (in square meters).
     - `description (String)`: A description of the property.
   - **JPA Annotations**:
     - `@Entity`: Indicates that this class is mapped to a database table.
     - `@Id`: Marks the primary key of the entity.
     - `@GeneratedValue`: Specifies that the `id` is auto-generated by the database.

#### 5. **User**
   - **Role**: The `User` class represents the users of the system. It is mapped to the `users` table in the database and is responsible for storing user-related information, such as username, email, and password.
   - **Responsibilities**:
     - Define the structure of user data, including attributes like username, email, and password.
     - Provide getter and setter methods for each attribute.
   - **Attributes**:
     - `id (Long)`: The unique identifier for the user.
     - `username (String)`: The username of the user.
     - `email (String)`: The email of the user.
     - `password (String)`: The encrypted password for the user.

### Relationships Between Classes

1. **`PropertyController` ↔ `PropertyService`**:
   - The `PropertyController` receives HTTP requests and delegates business logic to the `PropertyService`. This separation ensures that the controller is only responsible for handling API requests and responses, while the service layer focuses on business logic.

2. **`PropertyService` ↔ `PropertyRepository`**:
   - The `PropertyService` interacts with the `PropertyRepository` to perform database operations. The repository provides a simple interface to interact with the database without exposing database-specific details to the service layer.

3. **`PropertyRepository` ↔ `Property`**:
   - The `PropertyRepository` directly manages the persistence of the `Property` entity to the database. It uses JPA to map the `Property` class to the `properties` table and perform operations like saving, updating, and retrieving property records.

4. **`User` and Authentication**:
   - The `User` entity can be extended to support user authentication and authorization for the system. Although not included in the current class diagram, it can be integrated with Spring Security to provide login and access control functionality.


## Test Report

### Author
Name: Alexandra Cortes Tovar

### Date
Date: 16/10/2024

### Summary

This report outlines the unit and integration tests conducted for the Property Management System. The tests focus on ensuring that the CRUD (Create, Read, Update, Delete) operations for the `Property` entity work as expected, along with validation for pagination and search functionality. The `PropertyService` and `PropertyController` layers are the primary focus of these tests. Security measures such as CSRF protection and authentication were also validated.

### Tests Conducted

#### Controller Tests

1. **Test `testCreateProperty`**
   - **Description**: Validates that a new property can be created and stored in the database.
   - **Objective**: Ensure that the backend API can create a property with valid data and persist it in the database.
   - **Testing Scenario**: Simulate a POST request to create a property using valid JSON data.
   - **Expected Behavior**: The property is successfully created, assigned a unique ID, and stored in the database.
   - **Verification**: Confirms that the response contains the correct property details and that the property is saved in the database with a valid ID.

2. **Test `testGetAllProperties`**
   - **Description**: Ensures that all properties can be retrieved, and pagination is working as expected.
   - **Objective**: Validate that the API returns a paginated list of properties when a GET request is made.
   - **Testing Scenario**: Simulate a GET request to retrieve properties with pagination (using query parameters for page and size).
   - **Expected Behavior**: The API returns a paginated list of properties, including metadata such as page number, total elements, and total pages.
   - **Verification**: Confirms that the response contains the correct number of properties and pagination metadata.

3. **Test `testUpdateProperty`**
   - **Description**: Tests the ability to update an existing property’s details.
   - **Objective**: Ensure that a property’s information can be updated and persisted in the database.
   - **Testing Scenario**: Simulate a PUT request to update an existing property by its ID, using valid JSON data.
   - **Expected Behavior**: The property is successfully updated, and the new details are stored in the database.
   - **Verification**: Confirms that the updated property information is returned and that the database reflects the changes.

4. **Test `testDeleteProperty`**
   - **Description**: Validates the deletion of a property from the system.
   - **Objective**: Ensure that a property can be deleted by its ID and that it is removed from the database.
   - **Testing Scenario**: Simulate a DELETE request to remove a property by its unique ID.
   - **Expected Behavior**: The property is successfully deleted, and the deletion is confirmed.
   - **Verification**: Confirms that the property is no longer present in the database after deletion.

5. **Test `testSearchPropertiesByAddress`**
   - **Description**: Tests the ability to search for properties by address.
   - **Objective**: Ensure that properties can be retrieved based on their address, and the search results support pagination.
   - **Testing Scenario**: Simulate a GET request with a search query (address) and pagination parameters.
   - **Expected Behavior**: The API returns a list of properties that match the search query, along with paginated results.
   - **Verification**: Confirms that the properties returned match the search query and that pagination is applied correctly.

---

### Service Layer Tests

1. **Test `testGetAllProperties`**
   - **Description**: Tests the retrieval of all properties with pagination in the service layer.
   - **Objective**: Ensure that the `PropertyService` can return paginated property data from the database.
   - **Testing Scenario**: Simulate a call to the `getAllProperties(Pageable pageable)` method.
   - **Expected Behavior**: The method returns a paginated list of properties.
   - **Verification**: Confirms that the correct page of properties is returned from the repository, and pagination details (e.g., page size, total elements) are correct.

2. **Test `testGetPropertyById`**
   - **Description**: Validates that a specific property can be retrieved by its ID in the service layer.
   - **Objective**: Ensure that the `PropertyService` returns the correct property based on its ID.
   - **Testing Scenario**: Simulate a call to the `getPropertyById(Long id)` method.
   - **Expected Behavior**: The method returns the property details associated with the given ID.
   - **Verification**: Confirms that the returned property has the correct ID and associated details.

3. **Test `testCreateProperty`**
   - **Description**: Tests the property creation logic in the service layer.
   - **Objective**: Ensure that the `PropertyService` can create and save a new property in the database.
   - **Testing Scenario**: Simulate a call to the `createProperty(Property property)` method.
   - **Expected Behavior**: The property is successfully saved to the database.
   - **Verification**: Confirms that the `save` method in `PropertyRepository` is called and that the property data is persisted correctly.

4. **Test `testUpdateProperty`**
   - **Description**: Tests the property update logic in the service layer.
   - **Objective**: Ensure that the `PropertyService` can update an existing property in the database.
   - **Testing Scenario**: Simulate a call to the `updateProperty(Long id, Property property)` method.
   - **Expected Behavior**: The property is successfully updated in the database with the new data.
   - **Verification**: Confirms that the updated property is saved correctly and that the repository methods are called as expected.

5. **Test `testDeleteProperty`**
   - **Description**: Tests the deletion logic for properties in the service layer.
   - **Objective**: Ensure that the `PropertyService` can delete a property from the database.
   - **Testing Scenario**: Simulate a call to the `deleteProperty(Long id)` method.
   - **Expected Behavior**: The property is deleted from the database.
   - **Verification**: Confirms that the repository’s `delete` method is called, and the property is successfully removed from the database.

---

### Security Tests

In addition to the CRUD tests, the system includes security-related tests to ensure that the endpoints are protected from unauthorized access:

1. **CSRF Token Validation**:
   - **Test**: Verify that all POST, PUT, and DELETE requests include a valid CSRF token.
   - **Objective**: Ensure that the system rejects requests without a valid CSRF token to prevent CSRF attacks.
   - **Expected Behavior**: Requests without a CSRF token should result in a 403 Forbidden status.

2. **Authentication Tests**:
   - **Test**: Validate that only authenticated users can access certain endpoints (if implemented).
   - **Objective**: Ensure that unauthorized users cannot access or modify property data.
   - **Expected Behavior**: Requests from unauthenticated users should result in a 401 Unauthorized status.

---

## Test Coverage

- **Controller Layer**: All critical API endpoints in `PropertyController` have been tested, ensuring the correct handling of requests and responses.
- **Service Layer**: The `PropertyService` methods have been thoroughly tested to ensure correct business logic and interactions with the repository.
- **Repository Layer**: Repository functionality, such as saving and retrieving properties, has been validated indirectly through service and controller tests.
- **Security Layer**: Tests ensure that CSRF protection is correctly enforced for all state-changing operations (POST, PUT, DELETE).


![Test report](https://github.com/alexandrac1420/Seguridad_Nube/blob/master/Pictures/test.png)
## Docker Compose Configuration

The `docker-compose.yml` file contains the configuration needed to run both the MySQL database and the Spring Boot backend in Docker containers. Below is an explanation of the key parts of the configuration:

```yaml
services:
  mysql:
    container_name: 'propierties-mysql'
    image: 'mysql:latest'
    environment:
      - 'MYSQL_DATABASE=propierties'
      - 'MYSQL_PASSWORD=secret'
      - 'MYSQL_ROOT_PASSWORD=verysecret'
      - 'MYSQL_USER=myuser'
    ports:
      - '3306:3306'
```
### Explanation:

#### Services:
The `services` section defines the containers that will be started by Docker Compose. In this case, it includes the `mysql` container for the MySQL database.

#### mysql:
- **container_name**: The name given to the MySQL container. In this case, the container is named `propierties-mysql`.

- **image**: Specifies the Docker image to use for the MySQL service. Here, the latest version of the official `mysql` image is used.

- **environment**: Defines environment variables that configure MySQL:
  - `MYSQL_DATABASE`: The name of the database to be created inside the MySQL container.
  - `MYSQL_PASSWORD`: The password for the `myuser` account.
  - `MYSQL_ROOT_PASSWORD`: The root password for the MySQL server.
  - `MYSQL_USER`: The name of the non-root user who will have access to the database.

- **ports**: Maps port `3306` of the MySQL container to port `3306` of the host machine. This allows the MySQL database to be accessed from outside the container, for example, by the Spring Boot backend or external database clients.

This configuration allows you to run a MySQL database containerized within Docker, along with your Spring Boot backend, using Docker Compose for a simplified local development environment.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Backend Framework
* [MySQL](https://www.mysql.com/) - Database
* [Docker](https://www.docker.com/) - Containerization (for local development)
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Seguridad_Nube.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
