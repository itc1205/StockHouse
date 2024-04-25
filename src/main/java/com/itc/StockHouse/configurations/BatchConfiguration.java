package com.itc.StockHouse.configurations;

import com.itc.StockHouse.batching.processors.StockItemPriceProcessor;
import com.itc.StockHouse.model.StockEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Конфигурационный файл для работы с Spring Batch
 */
@Configuration
public class BatchConfiguration {
    /**
     * Путь до файла в который нужно будет записывать изменения
     */
    @Value("${app.scheduling.output_file}")
    private String fileOutput;

    /**
     * Процент увеличения цены
     */
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
    private BigDecimal priceIncreasePercentage;


    private static final String UPDATE_STOCKS_PRICE_QUERY = "UPDATE STOCKS SET price=:price WHERE id=:id";

    private static final String SELECT_ALL_STOCKS_QUERY = "SELECT * FROM STOCKS";

    /**
     * Метод конфигурирующий FlatFileItemWriter для записи изменений StockEntity в файл
     */
    @Bean
    public FlatFileItemWriter<StockEntity> fileWriter() {
        BeanWrapperFieldExtractor<StockEntity> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(
                new String[] {
                        "id",
                        "name",
                        "price",
                        "amount",
                        "category",
                        "vendorCode",
                        "description",
                        "updateDate",
                        "creationDate",
                }
        );
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<StockEntity> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<StockEntity>()
                .name("stockWriter")
                .resource(new FileSystemResource(fileOutput))
                .lineAggregator(lineAggregator)
                .shouldDeleteIfExists(true)
                .shouldDeleteIfEmpty(true)
                .build();
    }

    /**
     * Метод конфигурирующий ItemProcessor процессора для обработки StockEntity
     */
    @Bean
    public ItemProcessor<StockEntity, StockEntity> priceProcessor() {
        return new StockItemPriceProcessor(priceIncreasePercentage);
    }

    /**
     * Метод конфигурирующий JdbcCursorItemReader для чтения записей StockEntity из базы данных
     */
    @Bean
    public JdbcCursorItemReader<StockEntity> jdbcReader(DataSource dataSource) {

        return new JdbcCursorItemReaderBuilder<StockEntity>()
                .name("databaseStocksItemReader")
                .sql(SELECT_ALL_STOCKS_QUERY)
                .dataSource(dataSource)
                .beanRowMapper(StockEntity.class)
                .build();
    }

    /**
     * Метод конфигурирующий JdbcBatchItemWriter для записи изменений в цене у StockEntity в базу данных
     */
    @Bean
    public JdbcBatchItemWriter<StockEntity> jdbcWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<StockEntity>()
                .sql(UPDATE_STOCKS_PRICE_QUERY)
                .beanMapped()
                .dataSource(dataSource)
                .build();
    }

    /**
     * Метод конфигурирующий CompositeItemWriter для одновременной записи изменений StockEntity в файл и базу данных
     */
    @Bean
    public CompositeItemWriter<StockEntity> jdbcAndFileWriter(DataSource dataSource) {
        CompositeItemWriter<StockEntity> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(jdbcWriter(dataSource), fileWriter()));
        return writer;
    }

    /**
     * Метод конфигурирующий Step для чтения->обработки цены->записи изменений в файл и базу данных
     */
    @Bean
    public Step step1(JobRepository jobRepository, JpaTransactionManager transactionManager, DataSource dataSource) {
        return new StepBuilder("step1", jobRepository)
                .<StockEntity, StockEntity>chunk(10000, transactionManager)
                .reader(jdbcReader(dataSource))
                .processor(priceProcessor())
                .writer(jdbcAndFileWriter(dataSource))
                .build();
    }

    /**
     * Метод конфигурирующий Job для создания задачи обновления цены
     */
    @Bean
    public Job priceUpdateJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("priceUpdateJob", jobRepository)
                .start(step1)
                .build();
    }
}
