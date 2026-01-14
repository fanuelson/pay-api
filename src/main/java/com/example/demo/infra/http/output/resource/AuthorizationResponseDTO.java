package com.example.demo.infra.http.output.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.nonNull;

/*
 * Consulta serviço autorizador externo
 * GET https://util.devi.tools/api/v2/authorize
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponseDTO {

  @JsonProperty("status")
  private String status;

  @JsonProperty("data")
  private AuthorizationData data;

  public boolean isAuthorized() {
    return "success".equals(status) && nonNull(data) && data.getAuthorization();
  }

  @Value
  public static class AuthorizationData {
    @JsonProperty("authorization")
    Boolean authorization;
  }
}