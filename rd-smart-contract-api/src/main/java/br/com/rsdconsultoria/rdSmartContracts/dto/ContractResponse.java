package br.com.rsdconsultoria.rdSmartContracts.dto;

public class ContractResponse {
    private String script;
    private boolean published;

    public ContractResponse(String script, boolean published) {
        this.script = script;
        this.published = published;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
