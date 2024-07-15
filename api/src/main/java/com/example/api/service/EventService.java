package com.example.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.api.domain.event.Event;
import com.example.api.domain.event.EventRequestDTO;
import com.example.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Value("${aws.bucket.name")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepository;
    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if(data.imgUrl() != null){
            imgUrl =this.uploadImg(data.imgUrl());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(String.valueOf(data.date())));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());
        eventRepository.save(newEvent);
        return newEvent;
    }

    private String uploadImg(String multipartFile){

        String filename = UUID.randomUUID() + "-" + multipartFile.stripIndent();

        try{
            File file = this.convertMultipartFile(multipartFile);
            s3Client.putObject(bucketName,filename, file);
            file.delete();
            return s3Client.getUrl(bucketName, filename).toString();
        } catch (Exception e){
            System.out.println("erro ao subir arquivo");
            return "";
        }
    }

    private File convertMultipartFile(String multipartFile) throws IOException {

        File convFile = new File(Objects.requireNonNull(multipartFile.stripIndent()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public List<EventRequestDTO> getEvents(int page, int size) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        Page<Event> eventPage = this.eventRepository.findAll(pageable);
        return eventPage.map(event -> new EventRequestDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                "",
                "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl()
        )).stream().toList();
    }
}
