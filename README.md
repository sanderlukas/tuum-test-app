# Introduction

Solution to Tuum test assignment.

## How to run

I provided scripts that would make setting up a little quicker. Just run the script from the command line in the project root folder to create `.env` file.
Replace _{your_password}_ with your input.

In Windows:

```
init {your_password}
```

In Linux:

```
./init.sh {your_password}
```

This will help setting up environment variable DATABASE_PASSWORD for docker compose. I know there is a better way to store passwords for Docker (e.g. Docker secrets), but I went
with this. The script will also do `gradlew build` to have the `.jar` file ready for building a Docker image.
After that you can run `docker compose up` that will get application, Postgres and RabbitMQ up and running.

## Explanations

This app is reachable at `localhost:8080` and has two paths available.

- /api/v1/accounts
- /api/v1/transactions

Both have GET and POST methods available.
Examples:

GET /api/v1/accounts/{accountId} - Get one particular account

POST /api/v1/accounts - Create account

```
{
  "customerId": 1000,
  "country": "US",        // All _country_ values should be given in [ISO 3166 Alpha-2 code](https://www.iban.com/country-codes), meaning two letter representation
  "currencies": ["GBP"]   // List of currencies. Available values: "EUR", "SEK", "GBP", "USD"
}
```

GET /api/v1/transaction/{accountId} - Get a list of transactions account has made

POST /api/v1/accounts - Create account

```
{
  "accountId": 10,
  "amount": 5000,         // Amount has to be provided in currency’s smallest unit. For example 50 USD would be 5000 cents.
  "currency": "USD",      // Available values: "EUR", "SEK", "GBP", "USD"
  "direction": "IN",      // Available values: "IN", "OUT"
  "description" : "Transfering 50 dollars"
}
```

After doing some research what would be a good way to handle monetary values I decided to go with representing amounts in cents. Some stackoverflow threads and
[Stripe docs](https://stripe.com/docs/currencies#zero-decimal) helped me here. I think this approach is feasible for a small test application like this.

Other than that the application has quite a simple configuration. To have Postgres container to load the data initialization script, I defined a volume mount
in `docker-compose.yml`. Also I used [Testcontainers](https://www.testcontainers.org/) to test locally to actually have real database.

```
...
    volumes:
    - pgdata:/var/lib/postgresql/data
    - ./db/init_postgres.sql:/docker-entrypoint-initdb.d/init_postgres.sql     // Any *.sql file will be run in this directory
...
```

RabbitMQ will have different queues for account, balance and transaction create/update events. Also dead letter queues are defined even though there isn't a consumer for these
events.

## Questions

> Estimate how many transactions can your account application handle per second on your development machine.

> Describe what do you have to consider to be able to scale applications horizontally