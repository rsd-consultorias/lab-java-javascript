package br.com.rsdconsultoria.worker;

import java.time.Duration;
import java.time.LocalDateTime;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.jobrunr.storage.InMemoryStorageProvider;

import br.com.rsdconsultoria.worker.jobs.TesteJob;

public class ServiceWorkerApplication {

    public static void main(String[] args) throws Exception {
        JobRunr
                .configure()
                .useStorageProvider(new InMemoryStorageProvider())
                .useDashboard(JobRunrDashboardWebServerConfiguration.usingStandardDashboardConfiguration()
                .andBasicAuthentication("admin", "admin"))
                .useBackgroundJobServer(1)
                .initialize();

        new TesteJob();

        // BackgroundJobRequest.enqueue(new TesteJob());
        // BackgroundJobRequest.schedule(LocalDateTime.now().plusMinutes(2), new
        // TesteJob());
        // BackgroundJobRequest.scheduleRecurrently(Duration.ofSeconds(2l), new
        // TesteJob());
    }
}
