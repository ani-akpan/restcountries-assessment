package com.restcontries.app.cache;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restcontries.app.model.Country;

@ExtendWith(MockitoExtension.class)
class FileCacheServiceTest {

    private static final String CACHE_FILE_PATH = "countries_cache.json";

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FileCacheService fileCacheService;

    private Country sampleCountry1;
    private Country sampleCountry2;

    @BeforeEach
    void setUp() {

        Country.Name name1 = new Country.Name("Angola", "Republic of Angola");
        Country.Name name2 = new Country.Name("Congo", "Republic of Congo");

        sampleCountry1 = new Country(name1, "AGO", Arrays.asList("COG", "COD", "ZMB", "NAM"), "Africa", "Middle Africa", 32866268, 1246700.0);
        sampleCountry2 = new Country(name2, "COG", List.of("AGO"), "Africa", "Middle Africa", 5518087, 342000.0);
    }

    @AfterEach
    void tearDown() {
        File file = new File(CACHE_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void readFromCache_CacheFileExists_ReturnsCountriesList() throws IOException {
        Country[] expectedCountries = {sampleCountry1, sampleCountry2};
        when(objectMapper.readValue(any(InputStream.class), eq(Country[].class)))
                .thenReturn(expectedCountries);

        List<Country> result = fileCacheService.readFromCache();
        assertNotNull(result);
        assertEquals(expectedCountries.length, result.size());
    }

    @Test
    void readFromCache_CacheFileDoesNotExist_ReturnsNull() throws IOException {
        File file = new File(CACHE_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        List<Country> result = fileCacheService.readFromCache();

        assertNull(result);
    }

    @Test
    void writeToCache_WritesCountriesToFile() throws IOException {
        List<Country> countriesToWrite = Arrays.asList(sampleCountry1, sampleCountry2);

        fileCacheService.writeToCache(countriesToWrite);

        File file = new ClassPathResource(CACHE_FILE_PATH).getFile();
        assertTrue(file.exists());
        verify(objectMapper, times(1)).writeValue(file, countriesToWrite);
    }

    @Test
    void isCacheAvailable_CacheFileExists_ReturnsTrue() {
        File file = new File(CACHE_FILE_PATH);
        try {
            file.createNewFile();
        } catch (IOException e) {
            fail("Test setup failed to create cache file");
        }

        boolean result = fileCacheService.isCacheAvailable();

        assertTrue(result);
    }
}