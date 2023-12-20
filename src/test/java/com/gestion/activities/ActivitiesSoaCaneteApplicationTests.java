package com.gestion.activities;


import com.gestion.activities.domain.dto.ActivitiesRequestDto;
import com.gestion.activities.domain.dto.ActivitiesResponseDto;
import com.gestion.activities.service.ActivitiesService;
import com.gestion.activities.web.ActivitiesController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
class ActivitiesSoaCaneteApplicationTests {
    @Mock
    private ActivitiesService activitiesService;

    @InjectMocks
    private ActivitiesController activitiesController;

    @Test
    void getDataActivitiesById() {
        when(activitiesService.findById(anyInt())).thenReturn(Mono.just(new ActivitiesResponseDto()));

        Mono<ActivitiesResponseDto> result = activitiesController.getDataActivitiesById(1);

        verify(activitiesService, times(1)).findById(anyInt());
        assertEquals(result.block(), new ActivitiesResponseDto());
    }

    @Test
    void getDataActivitiesComplete() {
        when(activitiesService.findAll()).thenReturn(Flux.just(new ActivitiesResponseDto(), new ActivitiesResponseDto()));

        Flux<ActivitiesResponseDto> result = activitiesController.getDataActivitiesComplete();

        verify(activitiesService, times(1)).findAll();
        assertEquals(result.collectList().block(), Flux.just(new ActivitiesResponseDto(), new ActivitiesResponseDto()).collectList().block());
    }

    @Test
    void updateDataActivities() {
        when(activitiesService.updateActivities(any(ActivitiesRequestDto.class), anyInt()))
                .thenReturn(Mono.just(new ActivitiesResponseDto()));

        Mono<ActivitiesResponseDto> result = activitiesController.updateDataActivities(new ActivitiesRequestDto(), 1);

        verify(activitiesService, times(1)).updateActivities(any(ActivitiesRequestDto.class), anyInt());
        assertEquals(result.block(), new ActivitiesResponseDto());
    }

    @Test
    void deleteTotalActivities() {
        when(activitiesService.deleteActivities(anyInt())).thenReturn(Mono.empty());

        Mono<Void> result = activitiesController.deleteTotalActivities(558);

        verify(activitiesService, times(1)).deleteActivities(anyInt());
        assertEquals(result, Mono.empty());
    }
    /*-----SIMULACION DE ESCENARIO INCORRECTO CON UN ID INVALIDO-------*/


    @Test
    void updateDataActivities_ShouldThrowErrorWhenIdIsNull() {
        ActivitiesRequestDto requestDto = new ActivitiesRequestDto(null, "Nombre", "Descripción", LocalDate.now(), "Duración", "Ubicación", "Activo", "Tipo Pronacej", "Tipo SOA");

        assertThrows(IllegalArgumentException.class, () -> activitiesController.updateDataActivities(requestDto, 1));

        verify(activitiesService, never()).updateActivities(any(ActivitiesRequestDto.class), anyInt());
    }

    /*-----CORREGIMOS EL DATO NULO QUE LE ESTABAMOS PASANDO EN EL METODO ANTERIOR-------*/

    @Test
    void updateDataActivities_ShouldUpdateWhenIdIsValid() {
        ActivitiesRequestDto requestDto = new ActivitiesRequestDto(1, "Nuevo Nombre", "Nueva Descripción", LocalDate.now(), "Nueva Duración", "Nueva Ubicación", "Activo", "Tipo Pronacej", "Tipo SOA");

        when(activitiesService.updateActivities(any(ActivitiesRequestDto.class), anyInt()))
                .thenReturn(Mono.just(new ActivitiesResponseDto()));

        activitiesController.updateDataActivities(requestDto, 1);

        verify(activitiesService, times(1)).updateActivities(eq(requestDto), eq(1));
    }
}