package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.configurations.BatchConfiguration;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Оптимизированный шедулер обновления цены
 *
 * <p>
 *     При работе также записывает все измененные записи в файл app.output_file указанный в application.yaml
 * </p>
 *
 * <p>
 *     Под капотом постоянно перезапускает задачу из {@link BatchConfiguration}
 *     через время app.scheduling.rate указанное в application.yaml
 * </p>
 * <p>
 *     За счет батчинга потребляет меньше и отрабатывает быстрее чем
 *     {@link com.itc.StockHouse.scheduling.DefaultProductPriceScheduler}
 * </p>
 */
@Component
@Profile("prod")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('batch')}")
@AllArgsConstructor
public class OptimisedProductPriceBatchScheduler {

    private JobLauncher jobLauncher;

    private JobExplorer jobExplorer;

    private JobRepository jobRepository;

    private JobOperator jobOperator;

    private Job priceUpdateJob;

    /**
     * Метод для запуска задачи через каждые app.scheduling.delay миллисекунд указанные в application.yaml
     */
    @Scheduled(fixedDelayString = "${app.scheduling.delay}")
    public void runScheduledTask() throws Exception {

        // Проверяем если последний JobExecution упал
        if (checkIfLastExecutionRequiresRestart(priceUpdateJob)) {
            restartLastJob(priceUpdateJob);
            return;
        }

        jobLauncher.run(priceUpdateJob,
                new JobParametersBuilder()
                        .addLocalDateTime("startDateTime", LocalDateTime.now())
                        .addString("uuid", UUID.randomUUID().toString(), true)
                        .toJobParameters());
    }

    /**
     * Метод, проверяющий необходимость перезапуска последнего выполнения таска.
     *
     * <p>
     *     В этих условиях будет возвращено true:
     *     <li>
     *          Если выполнение таска "потеряно". Такое происходит когда приложение завершилось до конца выполнения таска
     *     </li>
     *     <li>
     *         Если таск завершился с статусом FAILED
     *     </li>
     * </p>
     *
     * @param job Работа, которую требуется проверить
     * @return Нужен ли перезапуск или нет
     */
    private boolean checkIfLastExecutionRequiresRestart(@NotNull Job job) {
        JobInstance instance = jobExplorer.getLastJobInstance(job.getName());
        if (instance == null) {
            return false;
        }
        JobExecution execution = jobExplorer.getLastJobExecution(instance);

        if (execution == null) {
            return false;
        }

        return execution.getExitStatus().equals(ExitStatus.EXECUTING)
                || execution.getExitStatus().equals(ExitStatus.FAILED)
                || execution.getExitStatus().equals(ExitStatus.UNKNOWN);
    }


    /**
     * Метод для перезапуска таска
     *
     * <p>
     *     В процессе выполнения ставит таску статус FAILED и затем перезапускает его
     * </p>
     *
     * @param job Таск которому необходим перезапуск
     * @throws Exception Исключение выбрасываемое таском
     */
    private void restartLastJob(Job job) throws Exception {
        JobInstance instance = jobExplorer.getLastJobInstance(job.getName());
        if (instance == null) {
            return;
        }

        JobExecution execution = jobExplorer.getLastJobExecution(instance);
        if (execution == null) {
            return;
        }
        // Сброс состояния выполнения таска
        execution.setStatus(BatchStatus.FAILED);
        execution.setExitStatus(ExitStatus.FAILED);
        for (StepExecution stepExecution: execution.getStepExecutions()) {
            if (stepExecution.getStatus() == BatchStatus.STARTED) {
                stepExecution.setStatus(BatchStatus.FAILED);
                stepExecution.setExitStatus(ExitStatus.FAILED);
            }
            jobRepository.update(stepExecution);
        }
        jobRepository.update(execution);
        // Перезапускаем таску
        jobOperator.restart(execution.getId());
    }
}
