package com.itc.StockHouse.service;

import com.itc.StockHouse.dto.search.criteria.CreationDateCriteria;
import com.itc.StockHouse.dto.search.criteria.CriteriaDTO;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.dto.search.criteria.NameCriteria;
import com.itc.StockHouse.dto.search.criteria.PriceCriteria;
import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.motherobject.MotherObject;
import com.itc.StockHouse.repository.ProductRepository;
import com.itc.StockHouse.utils.criteriamapping.CriteriaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class SearchServiceImplTest {

    private final CriteriaMapper criteriaMapping = new CriteriaMapper();
    private final ProductEntity stockWith200Price = MotherObject
            .aStockWithRandomVendorCode()
            .withPrice(BigDecimal.valueOf(100))
            .build();
    private final ProductEntity stockWith200PriceAndNameExample1 = MotherObject
            .aStockWithRandomVendorCode()
            .withName("Example1")
            .withPrice(BigDecimal.valueOf(200))
            .build();
    private final ProductEntity stockWithExample1Name = MotherObject
            .aStockWithRandomVendorCode()
            .withName("Example1")
            .withPrice(BigDecimal.TEN)
            .build();
    private final ProductEntity stockWithExampleName = MotherObject
            .aStockWithRandomVendorCode()
            .withName("Example")
            .withPrice(BigDecimal.TEN)
            .build();
    private final ProductEntity stockWithCreationDateOfMin = MotherObject
            .aStockWithRandomVendorCode()
            .withCreationDate(OffsetDateTime.MIN)
            .withPrice(BigDecimal.TEN)
            .build();
    private final ProductEntity stockWithCreationDateOfMax = MotherObject
            .aStockWithRandomVendorCode()
            .withCreationDate(OffsetDateTime.MAX)
            .withPrice(BigDecimal.TEN)
            .build();
    private final ProductEntity stockWithCreationDateOfToday = MotherObject
            .aStockWithRandomVendorCode()
            .withPrice(BigDecimal.TEN)
            .build();
    @Autowired
    private ProductRepository productRepository;
    private SearchService searchService;

    SearchServiceImplTest() {
        this.searchService = new SearchServiceImpl(productRepository);
    }

    @BeforeEach
    void setUp() {

        productRepository.saveAll(
                Arrays.asList(
                        stockWith200Price,
                        stockWith200PriceAndNameExample1,
                        stockWithExample1Name,
                        stockWithExampleName,
                        stockWithCreationDateOfMin,
                        stockWithCreationDateOfMax,
                        stockWithCreationDateOfToday
                )
        );
        productRepository.flush();
        searchService = new SearchServiceImpl(productRepository);
    }

    @Test
    public void givenTaskCriteria_thenReturnAllMatching() {
        PriceCriteria priceCriteria1 = new PriceCriteria();
        priceCriteria1.setField("price");
        priceCriteria1.setValue(BigDecimal.valueOf(110.0));
        priceCriteria1.setOp(OperationDTO.GREATER_THAN_OR_EQ);

        PriceCriteria priceCriteria2 = new PriceCriteria();
        priceCriteria2.setField("price");
        priceCriteria2.setValue(BigDecimal.valueOf(240.1));
        priceCriteria2.setOp(OperationDTO.LESS_THAN_OR_EQ);

        CreationDateCriteria createdAtCriteria = new CreationDateCriteria();
        createdAtCriteria.setField("creationDate");
        createdAtCriteria.setValue(LocalDateTime.parse("2024-03-12T19:25:22").atOffset(ZoneOffset.UTC));
        createdAtCriteria.setOp(OperationDTO.GREATER_THAN_OR_EQ);

        NameCriteria nameCriteria = new NameCriteria();
        nameCriteria.setField("name");
        nameCriteria.setValue("1");
        nameCriteria.setOp(OperationDTO.LIKE);


        List<CriteriaDTO<?>> criteriaList = Arrays.asList(
                priceCriteria1,
                priceCriteria2,
                createdAtCriteria,
                nameCriteria
        );

        List<ProductEntity> expectedList = Collections.singletonList(
                stockWith200PriceAndNameExample1
        );

        List<ProductEntity> resultList = searchService
                .searchBySpecification(
                        criteriaMapping.mapToSpecification(criteriaList),
                        PageRequest.of(0, 100))
                .stream().toList();


        assertEquals(expectedList, resultList);
    }

    @Test
    public void givenImpossibleCriteria_thenReturnAllMatching() {

        NameCriteria nameCriteria = new NameCriteria();
        nameCriteria.setField("name");
        nameCriteria.setValue("ThisNameDoesNotExist");
        nameCriteria.setOp(OperationDTO.LIKE);


        List<CriteriaDTO<?>> criteriaList = List.of(
                nameCriteria
        );

        List<ProductEntity> expectedList = List.of();

        List<ProductEntity> resultList = searchService
                .searchBySpecification(
                        criteriaMapping.mapToSpecification(criteriaList),
                        PageRequest.of(0, 100))
                .stream()
                .toList();


        assertEquals(expectedList, resultList);
    }
}