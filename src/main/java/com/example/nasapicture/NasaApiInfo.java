package com.example.nasapicture;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NasaApiInfo {

  @Value("${nasa.api.url}")
  private String url;
  @Value("${nasa.api.key}")
  private String apiKey;
  @Value("${nasa.api.key.param-name}")
  private String apiKeyParamName;
  @Value("${nasa.api.sol.param-name}")
  private String solParamName;
  @Value("${nasa.api.camera.param-name}")
  private String cameraParamName;

  public URI getLargestMarsPictureUri(int sol, String camera) {
    return UriComponentsBuilder.fromUriString(url)
                               .queryParam(apiKeyParamName, apiKey)
                               .queryParam(solParamName, sol)
                               .queryParamIfPresent(cameraParamName, Optional.ofNullable(camera))
                               .build()
                               .toUri();
  }
}
