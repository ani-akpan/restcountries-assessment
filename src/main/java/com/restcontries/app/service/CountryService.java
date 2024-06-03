package com.restcontries.app.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.restcontries.app.cache.FileCacheService;
import com.restcontries.app.configuration.AppProperties;
import com.restcontries.app.model.Country;
import com.restcontries.app.restclient.RestTemplateUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CountryService {
    private static final String ALL_COUNTRY_PATH = "all";
    private final AppProperties appProperties;
    private final RestTemplateUtil restTemplateUtil;
    private final FileCacheService fileCacheService;

    public List<Country> getCountriesByPopulationDensityInDesc() {
        final List<Country> countries = getCountries();
        return countries.stream()
                .filter(country -> country.population() > 0)
                .filter(country -> country.area() > 0)
                .sorted(Comparator.comparingDouble(country -> country.population() / country.area()))
                .toList();
    }

    public Country getContinentCountryWithMostBordersInDifferentRegion() {
        String continent = getContinent();
        List<Country> countries = getCountries();
        return countries.stream()
                .filter(country -> continent.equalsIgnoreCase(country.region()))
                .max(Comparator.comparingLong(country -> getBorderCount(countries, country)))
                .orElse(null);
    }

    private long getBorderCount(List<Country> countries, Country country) {
        if(ObjectUtils.isEmpty(country.borders())) {
            return 0;
        }
        return country.borders().stream()
                .map(borderCode -> findCountryByBorderCode(borderCode, countries))
                .filter(borderCountry -> borderCountry != null && !borderCountry.region().equalsIgnoreCase(getContinent()))
                .count();
    }

    private Country findCountryByBorderCode(String borderCode, List<Country> countries) {
        return countries.stream()
                .filter(country -> country.cca3().equalsIgnoreCase(borderCode))
                .findFirst()
                .orElse(null);
    }

    public List<Country> getCountries() {
        if (fileCacheService.isCacheAvailable()) {
            try {
                return fileCacheService.readFromCache();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read countries from cache", e);
            }
        } else {
            Country[] countriesArray = restTemplateUtil.doGetRequest(ALL_COUNTRY_PATH, Country[].class);
            if(ObjectUtils.isEmpty(countriesArray)) {
                return List.of();
            }
            List<Country> countries = Arrays.asList(countriesArray);
            try {
                fileCacheService.writeToCache(countries);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write countries to cache", e);
            }
            return countries;
        }
    }

    private String getContinent() {
        return Optional.ofNullable(appProperties.getClientContinent())
                .orElse("Asia");
    }
}


