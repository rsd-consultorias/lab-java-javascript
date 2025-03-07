package br.com.rsdconsultoria.worker.jobs;

import java.time.Duration;

import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.scheduling.BackgroundJobRequest;

public class TesteJob implements JobRequest {

    public TesteJob() {
        BackgroundJobRequest.scheduleRecurrently(Duration.ofMinutes(2l), this);
    }

    @Override
    public Class<TesteJobHandler> getJobRequestHandler() {
        return TesteJobHandler.class;
    }
}
