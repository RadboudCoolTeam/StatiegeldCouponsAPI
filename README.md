# StatiegeldCouponsAPI

## Project description

This is a Web API part for the [StatiegeldCoupons](https://github.com/RadboudCoolTeam/StatiegeldCouponsApp) project.

## Installation guide
The project requires Java version 13+ in order to run the API on a server. 

There are a few different ways how a user can obtain the jar file to execute the project:
- The latest stable version of the project is available in the [Releases](https://github.com/RadboudCoolTeam/StatiegeldCouponsAPI/releases) section of the repository
- The user can build the executable jar from sources by using the script ```build.sh```

After obtaining the executable jar, user can just run the following command:

```
java -jar StatiegeldCouponsApi*.jar
```

## How to use the project
The web API is created by using Spring boot. There are several REST API mappings that allow the app to interact with the server: transfer data, create new users, coupons, and statistics elements.
The idea here is quite simple, all data objects of our information system are represented like Java objects. Both server and client have copies of those classes with the same signatures to make the JSON serialization process possible.

### Specifications of the API

**<api_url_address>** - The address of the server where `StatiegeldCouponsApi*.jar` is running.
```
http://<api_url_address>/api/users + …
```
| Url(...) | Arguments | Response | Description |
|----------|-----------|----------|-------------|
| / | - | List<User> | Debug API that allows the developer to verify the User's records in the database. |
| /{id} | PathVariable Long id | User | Debug API that allows the developer to verify the record of the specific User. |
| /{id}/coupons | PathVariable Long id, RequestBody User user  | List<Coupon> | Requires the User object in the request body to check the access rights of the user. Returns the coupons belonging to the specified user. |
| /{id}/statistics | PathVariable Long id, RequestBody User user | List<StatisticsElement> | Requires the User object in the request body to check the access rights of the user. Returns the statistics data belonging to the specified user. |
| {id}/updateStatistics | PathVariable Long id, RequestBody Pair<User, List<StatisticsElement>> pair | List<StatisticsElement>>  | Requires the User object in the request body to check the access rights of the user. Allows user to upload the updated statistic data from the client. |
| {id}/updateCoupon | PathVariable Long id, RequestBody Pair<User, Coupon> pair | Coupon | Requires the User object in the request body to check the access rights of the user. Allows the user to upload the updated coupon data to the server from the app. Returns the updated coupon. |
| {id}/delete | PathVariable long id, RequestBody Pair<User, Coupon> pair | String | Requires the User object in the request body to check the access rights of the user. Allows user to delete the specified coupon from the server. Returns the server response message. |
| /new | RequestBody User user | String | Allows the client to create new user records in the database. This method also performs server-side user data validation. If the user data fulfills all criteria, the server creates a new user, otherwise, it returns the error message. The password that the user provides to the server is not stored as clear text in the database. In our implementation, we are using SHA-256 hashing to store the hash of the password to be able to check it during the user verification procedures. |
| /auth | RequestBody User user | User | Performs the authentication procedure - checks if the user with the given password and email exists in the database and all data provided is correct. Returns the copy of the user in the database on auth success. |

In order to make API work on the new machine, the developer should create a MongoDB database “statiegeld_coupons_api” and collections “coupon”, “gift”, “statistics”, “user”.

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.
