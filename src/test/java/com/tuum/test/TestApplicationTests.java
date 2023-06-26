package com.tuum.test;

import com.tuum.test.dto.*;
import com.tuum.test.repository.AccountCurrencyBalanceRepository;
import com.tuum.test.repository.AccountRepository;
import com.tuum.test.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableJpaRepositories("com.tuum.test.repository.*")
@EntityScan("com.tuum.test.model.*")
@Sql({"/schema-test.sql"})
class TestApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl="http://localhost:";

	private static RestTemplate restTemplate;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountCurrencyBalanceRepository accountCurrencyBalanceRepository;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		baseUrl = baseUrl.concat(port+"/");
	}

	@Test
	public void testCreateAndGetAccount() {
		AccountCreationRequestDTO accountCreationRequestDTO = AccountCreationRequestDTO.builder()
				.customerId("C1001")
				.country("Estonia")
				.currencies(Collections.singletonList("EUR"))
				.build();


		ResponseEntity<AccountDetailsResponseDTO> createAccountResponse = restTemplate.postForEntity(baseUrl+"account", accountCreationRequestDTO, AccountDetailsResponseDTO.class);
		ResponseEntity<AccountDetailsResponseDTO> getAccountResponse = restTemplate.getForEntity(baseUrl+"account/"+ Objects.requireNonNull(createAccountResponse.getBody()).getAccountId(), AccountDetailsResponseDTO.class);

		//Assert for create account
		//Checking if account is created with any account_id greater than the set starting account ID
		assertTrue(createAccountResponse.getBody().getAccountId()>500500001);

		//Assert for get account
		//Checking if account retrieved has customer id same as the one sent in request for account creation
		assertEquals("C1001", Objects.requireNonNull(getAccountResponse.getBody()).getCustomerId());
	}

	@Test
	public void testCreateAndGetTransaction() {
		AccountCreationRequestDTO accountCreationRequestDTO = AccountCreationRequestDTO.builder()
				.customerId("C1001")
				.country("Estonia")
				.currencies(Collections.singletonList("EUR"))
				.build();

		TransactionCreationRequestDTO transactionCreationRequestDTO = TransactionCreationRequestDTO.builder()
				.accountId(500500001L)
				.amount(500d)
				.currency("EUR")
				.directionOfTransaction("IN")
				.description("payment1")
				.build();

		ResponseEntity<AccountDetailsResponseDTO> createAccountResponse = restTemplate.postForEntity(baseUrl+"account", accountCreationRequestDTO, AccountDetailsResponseDTO.class);
		ResponseEntity<TransactionWithBalanceResponseDTO> createTransactionResponse = restTemplate.postForEntity(baseUrl+"transaction", transactionCreationRequestDTO, TransactionWithBalanceResponseDTO.class);
		ResponseEntity<TransactionDetailsResponseDto[]> getTransactionResponse = restTemplate.getForEntity(baseUrl+"transactions/"+ Objects.requireNonNull(createAccountResponse.getBody()).getAccountId(), TransactionDetailsResponseDto[].class);

		//Assert for create transaction
		//Checking if transaction was created with same amount as sent for creation
		assertEquals(500d, Objects.requireNonNull(createTransactionResponse.getBody()).getAmount());

		//Asser for get transaction
		//Checking if only 1 transaction exists for the account 500500001
		assertEquals(1, Objects.requireNonNull(getTransactionResponse.getBody()).length);
	}

}
