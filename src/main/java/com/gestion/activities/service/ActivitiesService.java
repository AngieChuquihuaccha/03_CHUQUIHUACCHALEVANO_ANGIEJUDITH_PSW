package com.gestion.activities.service;

import com.gestion.activities.domain.dto.ActivitiesRequestDto;
import com.gestion.activities.domain.dto.ActivitiesResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ActivitiesService {


    Mono<ActivitiesResponseDto> findById(Integer id);
    Flux<ActivitiesResponseDto> findAll();
    Flux<ActivitiesResponseDto> findAllActive();
    Flux<ActivitiesResponseDto> findAllInactive();
    Mono<ActivitiesResponseDto> saveNewActivities(ActivitiesRequestDto request);
    Mono<ActivitiesResponseDto> updateActivities(ActivitiesRequestDto request, Integer id);
    Mono<ActivitiesResponseDto> deleteLogicalActivities(Integer id);
    Mono<ActivitiesResponseDto> reactiveLogicalActivities(Integer id);
    Mono<Void> deleteActivities(Integer id);

}
