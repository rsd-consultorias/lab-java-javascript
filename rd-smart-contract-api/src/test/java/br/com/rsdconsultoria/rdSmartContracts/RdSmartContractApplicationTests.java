package br.com.rsdconsultoria.rdSmartContracts;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rsdconsultoria.rdSmartContracts.services.ScriptEngine;

@SpringBootTest
class RdSmartContractApplicationTests {
	@Mock
    private ScriptEngine engine;

	@Test
	void contextLoads() {
	}

}
