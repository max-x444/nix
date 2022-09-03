package com.utils;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class IOUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
    private static final Pattern PATTERN = Pattern.compile(
            "\"(?<keyJSON>\\w*)\": \"(?<valueJSON>.*)\"|<(?<keyXML>\\w*)( (?<keyCurrency>\\w*)=\"(?<valueCurrency>.*)\")?>(?<valueXML>.*)<");

    private IOUtil() {
    }

    public static Map<String, Object> readFile(@NonNull final File file) {
        final Map<String, Object> map = new HashMap<>();
        if (file.exists()) {
            try (final FileReader fileReader = new FileReader(file);
                 final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    parseData(map, line);
                }
            } catch (IOException exception) {
                LOGGER.error(exception.getMessage());
            }
        }
        return map;
    }

    private static void parseData(@NonNull final Map<String, Object> map, @NonNull final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        final String checkKeyCurrency;
        if (matcher.find()) {
            map.put(Optional.ofNullable(matcher.group("keyJSON")).orElse(matcher.group("keyXML")),
                    Optional.ofNullable(matcher.group("valueJSON")).orElse(matcher.group("valueXML")));
            checkKeyCurrency = matcher.group("keyCurrency");
            if (checkKeyCurrency != null) {
                map.put(checkKeyCurrency, matcher.group("valueCurrency"));
            }
        }
    }
}