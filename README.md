# spring-boot-vue3
## Here you go! Dependencies

* Java 18+
* Maven 3.6.3+
* MySQL 8+
* nodejs 16.20.0+
* yarn 1.22.10+

### Local Setup

On macOS (with homebrew):

```sh
brew install node@16
echo 'export PATH="/usr/local/opt/node@16/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
brew install yarn
```

Checkout git repo

## Configuration

### Database

MySQL is required to run the API service. You'll need to initialize an
empty database:

```sql
create database mdti;
```
## Running

backend and frontend components should be started individually in separate.

To start backend APIs, run following java class

  #### MainApiApplication.java


Open http://localhost:8088/swagger-ui/index.html url to see the swagger documentation.

To start frontend UI component, run following command

```sh
yarn serve
```

The application UI is now running on http://localhost:8080/. Try signing in using default user,
admin@at.com / password

# ENJOY !
# Thank You !
