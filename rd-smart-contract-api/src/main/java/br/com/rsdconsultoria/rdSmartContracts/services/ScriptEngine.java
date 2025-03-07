package br.com.rsdconsultoria.rdSmartContracts.services;

import java.io.IOException;

import org.springframework.stereotype.Component;

import br.com.rsdconsultoria.engine.SmartContractEngine;

@Component
public class ScriptEngine extends SmartContractEngine {

    public ScriptEngine() throws IOException {
        super();
        //TODO Auto-generated constructor stub
        System.out.println("CONSTRUCTED");
    }

}
