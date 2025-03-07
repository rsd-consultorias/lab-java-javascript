package br.com.rsdconsultoria.worker.jobs;

import org.jobrunr.jobs.lambdas.JobRequest;

public class TesteJob implements JobRequest {

    @Override
    public Class<TesteJobHandler> getJobRequestHandler() {
        return TesteJobHandler.class;
    }
}
