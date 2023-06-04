package com.tuum.testassignment.transaction;

import reactor.core.publisher.Mono;

import com.tuum.testassignment.PostgreSQLExtension;
import com.tuum.testassignment.RabbitMQExtension;
import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;
import com.tuum.testassignment.common.Country;
import com.tuum.testassignment.common.TransactionDirection;
import com.tuum.testassignment.transaction.dto.TransactionCreatedResponse;
import com.tuum.testassignment.transaction.dto.TransactionRequest;
import com.tuum.testassignment.transaction.dto.TransactionResponse;
import com.tuum.testassignment.transaction.dto.TransactionsResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Set;

import static com.tuum.testassignment.common.CustomCurrency.EUR;
import static com.tuum.testassignment.common.CustomCurrency.USD;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({PostgreSQLExtension.class, RabbitMQExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionServiceIntegrationTest {

    private static final String TRANSACTION_PATH = "/api/v1/transactions";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createTransaction() {
        AccountRequest request = new AccountRequest(123L, Country.EE, Set.of(USD, EUR));

        AccountResponse accountResponse = webTestClient
                .post()
                .uri("/api/v1/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AccountRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();


        TransactionRequest transactionRequest = new TransactionRequest(accountResponse.accountId(), "50000", USD, TransactionDirection.IN, "Transfer 500 USD");

        TransactionCreatedResponse transactionResponse = webTestClient
                .post()
                .uri(TRANSACTION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(transactionRequest), TransactionRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionCreatedResponse.class)
                .returnResult()
                .getResponseBody();

        List<TransactionResponse> accountTransactions = webTestClient
                .get()
                .uri(TRANSACTION_PATH + "/{id}", accountResponse.accountId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionsResponse.class)
                .returnResult()
                .getResponseBody()
                .transactions();

        assertThat(accountTransactions)
                .hasSize(1)
                .contains(new TransactionResponse(
                        transactionResponse.transactionId(),
                        accountResponse.accountId(),
                        Long.parseLong(transactionRequest.amount()),
                        transactionRequest.currency().name(),
                        transactionRequest.direction().name(),
                        transactionRequest.description()
                ));
    }
}
