package com.tuum.testassignment.account;

import reactor.core.publisher.Mono;

import com.tuum.testassignment.PostgreSQLExtension;
import com.tuum.testassignment.RabbitMQExtension;
import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;
import com.tuum.testassignment.balance.BalanceResponse;
import com.tuum.testassignment.common.Country;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

import static com.tuum.testassignment.common.CustomCurrency.EUR;
import static com.tuum.testassignment.common.CustomCurrency.USD;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({PostgreSQLExtension.class, RabbitMQExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceIntegrationTest {

    private static final String ACCOUNT_PATH = "/api/v1/accounts";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createAccount() {
        AccountRequest request = new AccountRequest(123L, Country.EE, Set.of(USD, EUR));

        AccountResponse response = webTestClient
                .post()
                .uri(ACCOUNT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AccountRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.accountId()).isNotNull();
        assertThat(response.customerId()).isEqualTo(request.customerId());
        assertThat(response.balances())
                .isNotEmpty()
                .hasSize(2)
                .containsAll(request.currencies().stream()
                        .map(currency -> new BalanceResponse(0L, currency.name()))
                        .toList());

        // Make sure account has been made
        webTestClient
                .get()
                .uri(ACCOUNT_PATH + "/{id}", response.accountId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .isEqualTo(response);
    }
}
