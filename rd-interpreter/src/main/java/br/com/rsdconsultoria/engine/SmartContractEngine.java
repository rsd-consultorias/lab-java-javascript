package br.com.rsdconsultoria.engine;

import java.io.IOException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import com.google.gson.Gson;

import br.com.rsdconsultoria.dto.ExecutionResult;
import br.com.rsdconsultoria.model.Participant;
import br.com.rsdconsultoria.model.SmartContract;

public class SmartContractEngine {
    private String contractId;
    private String contractName;
    private String script;
    private String parameters;
    private Gson gson;

    // public SmartContractEngine(String contractId, String contractName, String
    // script, String parameters)
    public SmartContractEngine()
            throws IOException {

        gson = new Gson();
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public ExecutionResult execute() throws Exception {
        ExecutionResult result = new ExecutionResult();
        System.setProperty("-Dpolyglot.engine.WarnInterpreterOnly", "false");

        try (Context context = Context.newBuilder("js").allowHostAccess(HostAccess.ALL).build()) {
            // Assuming context is your GraalVM context
            Value bindings = context.getBindings("js");
            // Carrega as funções do Smart Contract para usar no JavaScript
            Participant participant = new Participant("teste@teste");
            bindings.putMember("rd", new SmartContract(participant));

            // Executa o script e obtém o resultado
            context.eval("js", "var output = {}; %s".formatted(script));

            bindings.getMember("execute").executeVoid(parameters);

            Object output = bindings.getMember("output").as(Object.class);
            result.setBody(gson.toJson(output));
            result.setSuccess(true);

            return result;
        } catch (PolyglotException e) {
            result.setSuccess(false);
            String exception = "";
            exception = "Mensagem de erro: %s \n".formatted(e.getMessage());
            System.out.println("Mensagem de erro: %s".formatted(e.getMessage()));

            for (int i = 0; i < e.getStackTrace().length; i++) {
                if (e.getStackTrace()[i].getClassName().equals("<js>")) {
                    exception += "    Revise a linha %s: no método %s\n"
                            .formatted(e.getStackTrace()[i].getLineNumber(), e.getStackTrace()[i].getMethodName());

                    System.out.println("    Revise a linha %s: no método %s"
                            .formatted(e.getStackTrace()[i].getLineNumber(), e.getStackTrace()[i].getMethodName()));
                }
            }
            result.setBody(exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String toString() {
        return "SmartContract{" +
                "contractId='" + contractId + '\'' +
                ", contractName='" + contractName + '\'' +
                ", script='" + script + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
