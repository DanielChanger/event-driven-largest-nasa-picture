package com.example.nasapicture;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LargestMarsPictureCommandListener {

  private static final String IMG_SRC_FIELD_NAME = "img_src";
  private final RestTemplate restTemplate;
  private final NasaApiInfo nasaApiInfo;
  private final MarsLargestPicturesLocationHolder locationHolder;

  @RabbitListener(queues = {"largest-picture-command-queue"})
  public void putLargestMarsPictureInHolder(LargestPictureCommand command) {
    PictureRequestDto pictureRequestDto = command.requestDto();
    URI largestMarsPictureUri =
        nasaApiInfo.getLargestMarsPictureUri(pictureRequestDto.sol(), pictureRequestDto.camera());
    restTemplate.getForObject(largestMarsPictureUri, JsonNode.class)
                .findValuesAsText(IMG_SRC_FIELD_NAME)
                .parallelStream()
                .map(URI::create)
                .map(this::getSizeAndFinalUri)
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .ifPresent(uri -> locationHolder.putLocation(command.id(), uri));
  }

  private Map.Entry<Long, URI> getSizeAndFinalUri(URI imgSrc) {
    HttpHeaders headers = restTemplate.headForHeaders(imgSrc);
    URI location = headers.getLocation();
    URI finalUri = imgSrc;
    while (location != null) {
      finalUri = location;
      headers = restTemplate.headForHeaders(location);
      location = headers.getLocation();
    }
    return new AbstractMap.SimpleEntry<>(headers.getContentLength(), finalUri);
  }
}
