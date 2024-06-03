package com.restcontries.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.restcontries.app.cache.FileCacheService;
import com.restcontries.app.configuration.AppProperties;
import com.restcontries.app.model.Country;
import com.restcontries.app.restclient.RestTemplateUtil;

class CountryServiceTest {

    @Mock
    private AppProperties appProperties;

    @Mock
    private RestTemplateUtil restTemplateUtil;

    @Mock
    private FileCacheService fileCacheService;

    @InjectMocks
    private CountryService countryService;

    private Country sampleCountry1;
    private Country sampleCountry2;
    private Country sampleCountry3;
    private Country sampleCountry4;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Country.Name name1 = new Country.Name("Angola", "Republic of Angola");
        Country.Name name2 = new Country.Name("Congo", "Republic of Congo");
        Country.Name name3 = new Country.Name("Namibia", "Republic of Namibia");
        Country.Name name4 = new Country.Name("Zambia", "Republic of Zambia");

        sampleCountry1 = new Country(name1, "AGO", Arrays.asList("COG", "COD", "ZMB", "NAM"), "Africa", "Middle Africa", 32866268, 1246700.0);
        sampleCountry2 = new Country(name2, "COG", Collections.singletonList("AGO"), "Africa", "Middle Africa", 5518087, 342000.0);
        sampleCountry3 = new Country(name3, "NAM", Collections.singletonList("AGO"), "Africa", "Southern Africa", 2540916, 825615.0);
        sampleCountry4 = new Country(name4, "ZMB", Collections.singletonList("AGO"), "Africa", "Eastern Africa", 17861030, 752612.0);

        when(appProperties.getClientContinent()).thenReturn("Africa");
    }

    @Test
    void getCountriesByPopulationDensityInDesc_WithValidCountries_ReturnsSortedCountries() {
        when(fileCacheService.isCacheAvailable()).thenReturn(false);
        when(restTemplateUtil.doGetRequest(anyString(), eq(Country[].class)))
                .thenReturn(new Country[]{sampleCountry1, sampleCountry2, sampleCountry3, sampleCountry4});

        List<Country> result = countryService.getCountriesByPopulationDensityInDesc();

        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    void getContinentCountryWithMostBordersInDifferentRegion_WithValidCountries_ReturnsCorrectCountry() {
        when(fileCacheService.isCacheAvailable()).thenReturn(false);
        when(restTemplateUtil.doGetRequest(anyString(), eq(Country[].class)))
                .thenReturn(new Country[]{sampleCountry1, sampleCountry2, sampleCountry3, sampleCountry4});

        Country result = countryService.getContinentCountryWithMostBordersInDifferentRegion();

        assertNotNull(result);
        assertEquals(sampleCountry1, result); // Angola has the most borders with countries in a different region
    }

    @Test
    void getCountriesByPopulationDensityInDesc_WithCachedCountries_ReturnsSortedCountries() throws IOException {
        when(fileCacheService.isCacheAvailable()).thenReturn(true);
        when(fileCacheService.readFromCache()).thenReturn(List.of(sampleCountry1, sampleCountry2, sampleCountry3, sampleCountry4));

        List<Country> result = countryService.getCountriesByPopulationDensityInDesc();

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(sampleCountry3, result.get(0)); // Highest population density
        assertEquals(sampleCountry2, result.get(1));
        assertEquals(sampleCountry4, result.get(2));
        assertEquals(sampleCountry1, result.get(3)); // Lowest population density
    }

    @Test
    void getCountriesByPopulationDensityInDesc_WithEmptyCountries_ReturnsEmptyList() {
        when(fileCacheService.isCacheAvailable()).thenReturn(false);
        when(restTemplateUtil.doGetRequest(anyString(), eq(Country[].class)))
                .thenReturn(new Country[]{});

        List<Country> result = countryService.getCountriesByPopulationDensityInDesc();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getContinentCountryWithMostBordersInDifferentRegion_WithEmptyCountries_ReturnsNull() {
        when(fileCacheService.isCacheAvailable()).thenReturn(false);
        when(restTemplateUtil.doGetRequest(anyString(), eq(Country[].class)))
                .thenReturn(new Country[]{});

        Country result = countryService.getContinentCountryWithMostBordersInDifferentRegion();

        assertNull(result);
    }

    @Test
    void getCountries_ThrowsRuntimeException_WhenCacheReadingFails() throws IOException {
        when(fileCacheService.isCacheAvailable()).thenReturn(true);
        when(fileCacheService.readFromCache()).thenThrow(new IOException("IO Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> countryService.getCountries());

        assertEquals("Failed to read countries from cache", exception.getMessage());
    }

    @Test
    void getCountries_ThrowsRuntimeException_WhenCacheWritingFails() throws IOException {
        when(fileCacheService.isCacheAvailable()).thenReturn(false);
        when(restTemplateUtil.doGetRequest(anyString(), eq(Country[].class)))
                .thenReturn(new Country[]{sampleCountry1, sampleCountry2, sampleCountry3, sampleCountry4});
        doThrow(new IOException("IO Error")).when(fileCacheService).writeToCache(anyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> countryService.getCountries());

        assertEquals("Failed to write countries to cache", exception.getMessage());
    }

}