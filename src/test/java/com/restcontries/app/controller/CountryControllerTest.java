package com.restcontries.app.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.restcontries.app.model.Country;
import com.restcontries.app.service.CountryService;

@WebMvcTest(CountryController.class)
@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountriesByPopulationDensity() throws Exception {
        Country.Name name1 = new Country.Name("Country1", "Republic of Country1");
        Country.Name name2 = new Country.Name("Country2", "Republic of Country2");
        Country country1 = new Country(name1, "HNK", List.of("HNK", "KOR"), "Asia", "Eastern Asia", 1000000, 10000);
        Country country2 = new Country(name2, "EUR", List.of("HNK", "KOR", "NGN"), "Europe", "Eastern Europe", 2000000, 20000);
        List<Country> countries = Arrays.asList(country1, country2);

        when(countryService.getCountriesByPopulationDensityInDesc()).thenReturn(countries);

        mockMvc.perform(get("/api/countries-by-density"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetAsianCountryWithMostBorders() throws Exception {
        Country.Name name = new Country.Name("AsianCountry", "Republic of AsianCountry");
        Country country = new Country(name, "ASI", List.of("ASI", "EUR"), "Asia", "Eastern Asia", 5000000, 50000);

        when(countryService.getContinentCountryWithMostBordersInDifferentRegion()).thenReturn(country);

        mockMvc.perform(get("/api/asian-country-with-most-borders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name.common", is("AsianCountry")));

    }
}
