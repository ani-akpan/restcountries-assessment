package com.restcontries.app.model;

import java.util.List;

public record Country(Name name, String cca3, List<String> borders, String region, String subregion, long population, double area) {

    public record Name(String common, String official) {
    }
}
