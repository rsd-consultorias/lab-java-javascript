package br.com.rsdconsultoria.worker.jobs;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

public class TesteJobHandler implements JobRequestHandler<TesteJob> {

    @Override
    @Job(name = "TesteJob", retries = 3, labels = { "Teste", "TESTE 2" })
    public void run(TesteJob jobRequest) throws Exception {
        handle();
    }

    private void handle() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Teste de teste");
    }

}
