package com.restcontries.app.cache;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.restcontries.app.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileCacheService {
    private static final String CACHE_FILE_PATH = "countries_cache.json";
    private final ObjectMapper objectMapper;

    public List<Country> readFromCache() throws IOException {
        Resource resource = new ClassPathResource(CACHE_FILE_PATH);
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                Country[] countries = objectMapper.readValue(inputStream, Country[].class);
                if (ObjectUtils.isEmpty(countries)) {
                    return null;
                }
                return Arrays.asList(countries);
            }
        }
        return null;
    }

    public void writeToCache(List<Country> countries) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(CACHE_FILE_PATH);
        if(!classPathResource.exists()){
            ClassPathResource rootClassPathResource = new ClassPathResource("");
            String rootPath = rootClassPathResource.getURL().getPath();
            String filePath = rootPath + CACHE_FILE_PATH;
            File file = new File(filePath);
           if(file.createNewFile()){
               objectMapper.writeValue(file, countries);
           }
        } else {
            File file = classPathResource.getFile();
            objectMapper.writeValue(file, countries);
        }

    }

    public boolean isCacheAvailable() {
        return new ClassPathResource(CACHE_FILE_PATH).exists();
    }
}

