package br.com.rsdconsultoria.worker.jobs;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

public class TesteJobHandler implements JobRequestHandler<TesteJob> {

    @Override
    @Job(name = "TesteJob", retries = 3)
    public void run(TesteJob jobRequest) throws Exception {
        System.out.println("Teste de teste");
    }

}
