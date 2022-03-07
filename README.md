# E-Commerce Backend
A Spring Boot backend server, equipped with a front-end dashboard. 

This uses the Spring Boot framework, connecting the dashboard made using SvelteKit, to the MySQL database.

# [Screenshots](/screenshots)

# Cloning the Repository
1. Go to your preferred directory
2. Open your terminal and execute `git@github.com:laazyCmd/ecommerce-backend.git`
3. Import the project, using your preferred IDE
4. Wait for Maven to load dependencies
5. Before running, ensure that in `src/main/resources/application.properties`:
   - You are using your preferred database URL in `spring.datasource.url`
   - You are using your correct driver in `spring.datasource.driver-class-name`
6. Go to `src/main/java/BackendApplication.java`, then press `SHIFT + F10` to run the application
7. Open your browser, and go see [`https://localhost:8093`](https://localhost:8093) if this is successfully running
