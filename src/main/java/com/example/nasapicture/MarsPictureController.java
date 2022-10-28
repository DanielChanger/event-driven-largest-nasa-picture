package com.example.nasapicture;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class MarsPictureController {

  private final MarsPictureService marsPictureService;

  @PostMapping("/largest")
  public ResponseEntity<String> startLargestMarsPictureSearchCommand(@RequestBody PictureRequestDto pictureRequestDto) {
    String commandId = marsPictureService.startLargestMarsPictureSearchCommand(pictureRequestDto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .pathSegment(commandId)
                                              .build()
                                              .toUri();
    return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                         .location(location)
                         .build();
  }

  @GetMapping("/largest/{commandId}")
  public ResponseEntity<?> getLargestMarsPicture(@PathVariable String commandId) {
    Optional<URI> largestMarsPicture = marsPictureService.getLargestMarsPicture(commandId);
    if (largestMarsPicture.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body("No pictures found");
    }
    return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                         .location(largestMarsPicture.get())
                         .build();
  }
}
