package com.example.demo.infra.repository.jpa.entities;

import com.example.demo.domain.helper.StringHelper;
import com.example.demo.domain.notification.NotificationChannel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class EnabledNotificationChannelsConverter
  implements AttributeConverter<Set<NotificationChannel>, String> {

  private static final String DEFAULT_SEPARATOR = ";";

  @Override
  public String convertToDatabaseColumn(Set<NotificationChannel> attribute) {
    if(Objects.isNull(attribute) || attribute.isEmpty()) return "";

    return attribute.stream().map(Enum::name).collect(Collectors.joining(DEFAULT_SEPARATOR));
  }

  @Override
  public Set<NotificationChannel> convertToEntityAttribute(String dbData) {
    final var values = EnumSet.noneOf(NotificationChannel.class);
    if(StringHelper.isBlank(dbData)) return values;

    return Arrays.stream(dbData.split(DEFAULT_SEPARATOR))
      .map(NotificationChannel::valueOf)
      .collect(Collectors.toCollection(() -> values));

  }
}
