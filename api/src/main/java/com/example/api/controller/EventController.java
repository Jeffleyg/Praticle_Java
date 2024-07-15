package com.example.api.controller;

import com.example.api.domain.event.Event;
import com.example.api.domain.event.EventRequestDTO;
import com.example.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> create(@RequestParam("id") UUID id,
                                        @RequestParam("title") String title,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam("date") Date date,
                                        @RequestParam("city") String city,
                                        @RequestParam("state") String state,
                                        @RequestParam("remote") Boolean remote,
                                        @RequestParam("eventUrl") String eventUrl,
                                        @RequestParam(value = "image", required = false) String imgUrl){
        EventRequestDTO eventRequestDTO = new EventRequestDTO(id, title, description, date, city, state, remote, eventUrl, imgUrl);

        Event newEvent = this.eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping

    public ResponseEntity<List<EventRequestDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        List<EventRequestDTO> allEvents = this.eventService.getEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }
}
