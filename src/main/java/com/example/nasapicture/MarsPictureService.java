package com.example.nasapicture;

import java.net.URI;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarsPictureService {

  @Value("${largest.picture.command.queue}")
  public String queueName;
  private final MarsLargestPicturesLocationHolder locationHolder;
  private final RabbitTemplate rabbitTemplate;

  public String startLargestMarsPictureSearchCommand(PictureRequestDto pictureRequestDto) {
    String commandId = RandomStringUtils.randomAlphabetic(6);
    LargestPictureCommand largestPictureCommand = new LargestPictureCommand(commandId, pictureRequestDto);
    rabbitTemplate.convertAndSend(queueName, largestPictureCommand);
    return commandId;
  }

  public Optional<URI> getLargestMarsPicture(String commandId) {
    return locationHolder.getLocationByCommandId(commandId);
  }
}
