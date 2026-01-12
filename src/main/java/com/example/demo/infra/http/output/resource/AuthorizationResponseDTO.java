package com.example.demo.infra.http.output.resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AuthorizationData {

    @JsonProperty("authorization")
    private Boolean authorization;
  }

  public boolean isAuthorized() {
    return "success".equals(status) && data != null && Boolean.TRUE.equals(data.getAuthorization());
  }
}