package com.itc.StockHouse.scheduling;

import com.itc.StockHouse.support.LogMethodExecutionTime;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.UUID;

@Slf4j
@Component
@Profile("prod")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('optimised')}")
@RequiredArgsConstructor
public class OptimisedProductPriceScheduler {
    private final EntityManagerFactory entityManagerFactory;

    @Value("${app.schedulingA.output_file}")
    private String fileOutput;

    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
    private BigDecimal priceIncreasePercentage;

    @Value("${app.scheduling.exclusive-lock:false}")
    private Boolean exclusiveLock;

    @LogMethodExecutionTime
    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduling.delay}")
    public void increaseProductPrice() {
        final Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

        try (session) {
            session.doWork(new Work() {
                @Override
                public void execute(@NotNull Connection connection) throws SQLException {
                    try (
                            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileOutput));
                            connection
                    ) {
                        connection.setAutoCommit(false);
                        long rowCounter = 0L;

                        if (exclusiveLock) {
                            String lockQuery = "LOCK TABLE STOCKS IN ACCESS EXCLUSIVE MODE";
                            Statement lockStatement = connection.createStatement();
                            lockStatement.execute(lockQuery);
                        }

                        String selectQuery = "SELECT * FROM STOCKS FOR UPDATE";
                        Statement statement = connection.createStatement();

                        String updateQuery = "UPDATE STOCKS SET PRICE = ? WHERE id = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                        ResultSet rs = statement.executeQuery(selectQuery);

                        while (rs.next()) {
                            fileWriter.write(buildString(rs, rowCounter++));
                            fileWriter.newLine();

                            preparedStatement.setBigDecimal(1, calculateNewPrice(rs.getBigDecimal("price"), priceIncreasePercentage));
                            preparedStatement.setObject(2, UUID.fromString(rs.getString("id")));
                            preparedStatement.addBatch();

                            if (rowCounter % 100000 == 0) {
                                preparedStatement.executeBatch();
                                log.debug("Rows updated: {}", rowCounter);
                            }
                        }
                        log.debug("Commiting");
                        preparedStatement.executeBatch();
                        connection.commit();
                    } catch (Exception e) {
                        connection.rollback();
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private BigDecimal calculateNewPrice(BigDecimal oldPrice, BigDecimal priceIncreasePercentage) {
        return oldPrice.multiply(
                priceIncreasePercentage
                        .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64)
                        .add(BigDecimal.ONE)
        );
    }

    private String buildString(ResultSet resultSet, Long counter) throws SQLException {
        final Character delimiter = ' ';

        final String[] rows = new String[]{
                "id",
                "name",
                "price",
                "amount",
                "category",
                "vendor_code",
                "description",
                "update_date",
                "creation_date",
        };

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(counter);

        for (String row : rows) {
            stringBuilder.append(delimiter);
            stringBuilder.append(resultSet.getString(row));
        }

        return stringBuilder.toString();
    }
}
