package br.com.rsdconsultoria.dto;

public class ExecutionResult {
    private Object body;
    private Boolean success;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
