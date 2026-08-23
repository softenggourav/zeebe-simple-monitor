package io.zeebe.monitor.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class DisplayTimeFormatterTest {

  @Test
  void shouldFormatTimestampInUtc() {
    final var formatter = new DisplayTimeFormatter("UTC");
    final long timestamp = Instant.parse("2026-08-23T12:00:00.123Z").toEpochMilli();

    assertThat(formatter.format(timestamp)).isEqualTo("2026-08-23T12:00:00.123Z");
  }

  @Test
  void shouldFormatTimestampInConfiguredTimezone() {
    final var formatter = new DisplayTimeFormatter("Asia/Kolkata");
    final long timestamp = Instant.parse("2026-08-23T12:00:00Z").toEpochMilli();

    assertThat(formatter.format(timestamp)).isEqualTo("2026-08-23T17:30:00+05:30");
  }

  @Test
  void shouldApplyDaylightSavingTimeForRegionTimezone() {
    final var formatter = new DisplayTimeFormatter("Europe/Berlin");
    final long winterTimestamp = Instant.parse("2026-01-15T12:00:00Z").toEpochMilli();
    final long summerTimestamp = Instant.parse("2026-07-15T12:00:00Z").toEpochMilli();

    assertThat(formatter.format(winterTimestamp)).isEqualTo("2026-01-15T13:00:00+01:00");
    assertThat(formatter.format(summerTimestamp)).isEqualTo("2026-07-15T14:00:00+02:00");
  }

  @Test
  void shouldRejectInvalidTimezone() {
    assertThatThrownBy(() -> new DisplayTimeFormatter("Invalid/Timezone"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("monitor.display.timezone")
        .hasMessageContaining("Invalid/Timezone");
  }
}
