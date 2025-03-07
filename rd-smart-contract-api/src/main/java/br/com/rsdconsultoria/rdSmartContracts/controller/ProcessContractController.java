package br.com.rsdconsultoria.rdSmartContracts.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rsdconsultoria.engine.SmartContractEngine;
import br.com.rsdconsultoria.rdSmartContracts.dto.ContractProcessRequest;
import br.com.rsdconsultoria.rdSmartContracts.dto.ContractResponse;
import br.com.rsdconsultoria.rdSmartContracts.dto.PublishContractResponse;
import br.com.rsdconsultoria.utils.ScalableUniqueIDGenerator;

@RestController
@RequestMapping("/rest/process/v1")
public class ProcessContractController {

    @Value("${rd.scripts.repository}")
    private String SCRIPTS_REPOSITORY;

    @PostMapping("/")
    public PublishContractResponse publishContract(@RequestBody ContractProcessRequest contractRequest)
            throws Exception {
        String newContractId = ScalableUniqueIDGenerator.generateUniqueID("RD");
        Files.writeString(Paths.get("%s/%s".formatted(SCRIPTS_REPOSITORY, newContractId)), contractRequest.getScript(),
                StandardOpenOption.CREATE);
        return new PublishContractResponse(newContractId, true);
    }

    @PostMapping("/{contractId}")
    public Object post(@PathVariable("contractId") String contractId,
            @RequestBody ContractProcessRequest contractRequest) throws Exception {
        SmartContractEngine engine = new SmartContractEngine();
        engine.setParameters(contractRequest.getParameters());

        if (contractRequest.getScript() != null) {
            engine.setScript(contractRequest.getScript());
        } else {
            engine.setScript(new String(
                    Files.readAllBytes(Paths.get("%s/%s".formatted(SCRIPTS_REPOSITORY, contractId)))));
        }

        return engine.execute();
    }

    @GetMapping("/{contractId}")
    public ContractResponse get(@PathVariable("contractId") String contractId) throws Exception {
        return new ContractResponse(new String(
                Files.readAllBytes(Paths.get("%s/%s".formatted(SCRIPTS_REPOSITORY, contractId)))), true);
    }
}
