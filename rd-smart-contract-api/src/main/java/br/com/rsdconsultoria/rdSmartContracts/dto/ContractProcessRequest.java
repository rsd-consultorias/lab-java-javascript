package br.com.rsdconsultoria.rdSmartContracts.dto;

public class ContractProcessRequest {
    private String parameters;
    private String script;

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
