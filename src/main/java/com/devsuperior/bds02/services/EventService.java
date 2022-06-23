package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    public List<EventDTO> findAll(){
        List<Event> list = repository.findAll(Sort.by("name"));
        return list.stream()
                .map(x-> new EventDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto){
        try{

        Event currentEvent = repository.getOne(id);

        copyDtoEntity(currentEvent, dto);
        currentEvent = repository.save(currentEvent);

        return new EventDTO(currentEvent);
        } catch (EntityNotFoundException e){
            throw  new ResourceNotFoundException("Id não existe " + id);
        }
    }

    public void copyDtoEntity(Event event, EventDTO dto){
        event.setName(dto.getName());
        event.setCity(cityRepository.getOne(dto.getCityId()));
        event.setDate(dto.getDate());
        event.setUrl(dto.getUrl());

    }
}
