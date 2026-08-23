package io.zeebe.monitor.rest;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DisplayTimeFormatter {

    private static final String PROPERTY_NAME = "monitor.display.timezone";

    private final DateTimeFormatter formatter;

    public DisplayTimeFormatter(
            @Value("${" + PROPERTY_NAME + ":UTC}") final String configuredTimezone) {
        final ZoneId zoneId = resolveZoneId(configuredTimezone);
        formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zoneId);
    }

    private static ZoneId resolveZoneId(final String configuredTimezone) {
        try {
            return ZoneId.of(configuredTimezone);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(
                    "Invalid timezone configured for '" + PROPERTY_NAME + "': " + configuredTimezone, e);
        }
    }

    public String format(final long epochMillis) {
        return formatter.format(Instant.ofEpochMilli(epochMillis));
    }
}
