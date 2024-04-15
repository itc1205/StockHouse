package com.itc.StockHouse.batching;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.processors.StockItemPriceProcessor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class BatchConfiguration {
    @Value("${app.output_file}")
    private String fileOutput;

    @Value("${app.priceIncreasePercentage}")
    private Double priceIncreasePercentage;

    @Autowired
    DataSource dataSource;

    @Bean
    public FlatFileItemWriter<StockEntity> fileWriter() {
        BeanWrapperFieldExtractor<StockEntity> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(
                new String[] {
                        "uuid",
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

    @Bean
    public ItemProcessor<StockEntity, StockEntity> processor() {
        return new StockItemPriceProcessor(priceIncreasePercentage);
    }

    @Bean
    public JdbcCursorItemReader<StockEntity> reader() {
        return new JdbcCursorItemReaderBuilder<StockEntity>()
                .name("databaseStocksItemReader")
                .sql("SELECT * FROM STOCKS_DB")
                .dataSource(dataSource)
                .beanRowMapper(StockEntity.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<StockEntity> jdbcWriter() {
        return new JdbcBatchItemWriterBuilder<StockEntity>()
                .sql(
                        "UPDATE STOCKS_DB SET price=:price WHERE uuid=:uuid"
                )
                .beanMapped()
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public CompositeItemWriter<StockEntity> databaseAndFileWriter() {
        CompositeItemWriter<StockEntity> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(jdbcWriter(), fileWriter()));
        return writer;
    }

    @Bean
    public Step step1(JobRepository jobRepository, JpaTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<StockEntity, StockEntity>chunk(10000, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(databaseAndFileWriter())
                .build();
    }

    @Bean
    public Job priceUpdateJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("priceUpdateJob", jobRepository)
                .start(step1)
                .build();
    }
}
