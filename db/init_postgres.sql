CREATE
DATABASE tuum_test;

\c tuum_test

CREATE TABLE Account
(
    id          SERIAL     NOT NULL PRIMARY KEY,
    customer_id INT        NOT NULL,
    country     varchar(2) NOT NULL
);

CREATE TABLE Balance
(
    id         SERIAL     NOT NULL PRIMARY KEY,
    account_id INT        NOT NULL,
    amount     BIGINT     NOT NULL CHECK (amount >= 0) DEFAULT 0,
    currency   VARCHAR(3) NOT NULL,
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE CASCADE
);

CREATE TABLE Transaction
(
    id          SERIAL       NOT NULL PRIMARY KEY,
    account_id  INT          NOT NULL,
    amount      BIGINT       NOT NULL CHECK (amount >= 0),
    currency    VARCHAR(3)   NOT NULL,
    direction   VARCHAR(3)   NOT NULL CHECK (direction IN ('IN', 'OUT')),
    description VARCHAR(100) NOT NULL,
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE CASCADE
);