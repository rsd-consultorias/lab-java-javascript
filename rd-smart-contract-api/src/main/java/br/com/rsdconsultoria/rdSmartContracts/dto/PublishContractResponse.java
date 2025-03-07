package br.com.rsdconsultoria.rdSmartContracts.dto;

public class PublishContractResponse {
    private String id;
    private boolean success;

    public PublishContractResponse(String id, boolean success) {
        this.id = id;
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
