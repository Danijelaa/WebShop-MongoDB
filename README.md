# WebShop-MongoDB
## Description
This is simple Web Shop application which enables: 
- logged users to search products by category, minimum and maximum price;
- logged users to chose products and quantity of products they wish to buy and create shopping cart;
- logged users to get list of their shopping carts;
- moderator to add new products;
- moderator to update products;
- moderator to delete products.  

Backend of the application is developed in **Spring Boot** framework and frontend by using **HTML** and **AngularJS**. Application uses NoSQL database **MongoDB** to store data.
## Installation
1. Start MongodDB server.
2. Clone or download the project and unpack the folder. Navigate to project where pom.xml file is. Start the application using Maven command:
`mvn spring-boot:run`
3. Open browser and connect to: `localhost:8080`
4. Log in as a moderator using credentials: `moderator` for username and `moderator` for password.  
If you want to log in as a regular user, you can type in one of the following credentials for username and password, respectively:  
`username1`  `password1`  
`username2`  `password2`
## REST api
| Method | URL |Request Body (JSON)|Request Parameter(s)| Description | Authority |
|--------|-----|-------------------|--------------------|-------------|-----------|
|POST  |`localhost:8080/users/login`|user*||Login to application.||
|GET  |`localhost:8080/users/logout`|||Logout from application.||
|GET  |`localhost:8080/products`||_category_id_, _min_, _max_, _sortOrder_, _page_<br>(None of the parameters is required.)|Paginated listing of all products. Listed parameters enable search by category, mimimum price, maximum price, sorting by price and search by page number respectively.||
|GET  |`localhost:8080/products/{id}`<br> _id_-type String (textual representation of an ObjectId)|||Retrieving product by specified ID.||
|PUT  |`localhost:8080/products/{id}`<br> _id_-type String (textual representation of an ObjectId)|product**||Updating product with specified ID.|moderator|
|POST  |`localhost:8080/products/`|product***||Creating new product.|moderator|
|DELETE  |`localhost:8080/products/{id}`<br> _id_-type String (textual representation of an ObjectId)|||Deleting product with specified ID.|moderator|
|GET  |`localhost:8080/shopping-carts`|||Retrieving shopping carts of logged user.|user|
|POST  |`localhost:8080/shopping-carts`|shopping-cart-array****||Creating new shopping cart.|user|
|GET  |`localhost:8080/categories`|||Retrieving all categories of products.||

#### Formats of JSON objects required in request body
*user 

| Field | Type |
|-------|------|
|username|String|
|password|String|

**product

| Field | Type |
|-------|------|
|id|String (textual representation of an ObjectId)|
|category_id|String (textual representation of an ObjectId)|
|price|Numeric (double)|
|quantity|Numeric (integer)|

***product

| Field | Type |
|-------|------|
|category_id|String (textual representation of an ObjectId)|
|price|Numeric (double)|
|quantity|Numeric (integer)|
|title|String|

****shopping-cart-array  
Represents an array of following json object:

| Field | Type |
|-------|------|
|product_id|String (textual representation of an ObjectId)|
|quantity|Numeric (integer)|

