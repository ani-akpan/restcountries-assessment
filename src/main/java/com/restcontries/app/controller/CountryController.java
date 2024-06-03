package com.restcontries.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restcontries.app.model.Country;
import com.restcontries.app.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CountryController {
    private CountryService countryService;

    @GetMapping("/countries-by-density")
    public List<Country> getCountriesByPopulationDensity() {
        return countryService.getCountriesByPopulationDensityInDesc();
    }

    @GetMapping("/asian-country-with-most-borders")
    public Country getAsianCountryWithMostBorders() {
        return countryService.getContinentCountryWithMostBordersInDifferentRegion();
    }
}

