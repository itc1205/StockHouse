package com.itc.StockHouse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyInputStreamMappingUtil {
    private final ObjectMapper jsonMapper;
    public HashMap<String, BigDecimal> mapFromStream(InputStream stream) throws IOException {
        return jsonMapper.readValue(stream, new TypeReference<HashMap<String, BigDecimal>>() {});
    }
}
