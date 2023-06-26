CREATE TABLE IF NOT EXISTS account(
    id          VARCHAR(255) PRIMARY KEY,
    customer_id      VARCHAR(50) NOT NULL,
    account_id       INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 500500001 ),
    country      VARCHAR(255) NOT NULL,
    create_timestamp      TIMESTAMP with time zone default current_timestamp NOT NULL,
    update_timestamp      TIMESTAMP with time zone default current_timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS currency_account_mapping(
   id               VARCHAR(255) PRIMARY KEY,
   account_id      INTEGER NOT NULL,
   currency      VARCHAR(30) NOT NULL,
   available_amount    double precision NOT NULL,
   create_timestamp      TIMESTAMP with time zone default current_timestamp NOT NULL,
   update_timestamp      TIMESTAMP with time zone default current_timestamp NOT NULL,
   constraint account_fk FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS transaction(
    id          VARCHAR(255) PRIMARY KEY,
    transaction_id      VARCHAR(255) NOT NULL,
    account_id      INTEGER NOT NULL,
    amount      double precision NOT NULL,
    currency      VARCHAR(30) NOT NULL,
    direction      VARCHAR(30) NOT NULL,
    description      VARCHAR(255) NOT NULL,
    create_timestamp      TIMESTAMP default current_timestamp NOT NULL,
    update_timestamp      TIMESTAMP default current_timestamp NOT NULL
);