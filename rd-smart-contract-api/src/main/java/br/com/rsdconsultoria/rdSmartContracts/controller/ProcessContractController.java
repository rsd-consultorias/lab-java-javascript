package br.com.rsdconsultoria.rdSmartContracts.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rsdconsultoria.rdSmartContracts.dto.ContractProcessRequest;
import br.com.rsdconsultoria.rdSmartContracts.services.ScriptEngine;

@RestController
@RequestMapping("/rest/process/v1")
public class ProcessContractController {

    @Autowired
    private ScriptEngine engine;

    @PostMapping("/{contractId}")
    public Object post(@PathVariable("contractId") String contractId,
            @RequestBody ContractProcessRequest contractRequest) throws Exception {
        engine.setParameters(contractRequest.getParameters());

        if (contractRequest.getScript() != null) {
            engine.setScript(contractRequest.getScript());
        } else {
            engine.setScript(new String(
                    Files.readAllBytes(Paths.get("/Users/rafaeldias/repositories/java/rd-smart-contract/%s.js".formatted(contractId)))));
        }

        return engine.execute();
    }

    @GetMapping
    public Object get() throws Exception {
        engine.setParameters("{\"amount\": 1700, \"quality\": 0.89}");
        engine.setScript(
                "function execute(params) {\n params = JSON.parse(params);\n output.id = rd.getContractId();\n output.id2 = rd.getContractId();\n}");
        return engine.execute();
    }
}
