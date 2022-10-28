package com.example.nasapicture;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MarsLargestPicturesLocationHolder {
  private final Map<String, URI> largestPictureLocation = new ConcurrentHashMap<>();

  public Optional<URI> getLocationByCommandId(String commandId) {
    return Optional.ofNullable(largestPictureLocation.get(commandId));
  }

  public void putLocation(String commandId, URI location) {
    largestPictureLocation.put(commandId, location);
  }
}
