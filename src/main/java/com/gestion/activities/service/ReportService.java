package com.gestion.activities.service;


import com.gestion.activities.domain.model.Activities;
import com.gestion.activities.repository.ActivitiesRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ActivitiesRepository repository;

    public Mono<String> generateReport(String reportFormat) {
        String path = "C:\\Users\\Personal\\Downloads";

        return repository.findAll()
                .collectList()
                .flatMap(activities -> generateAndSaveReport(activities, reportFormat, path))
                .onErrorResume(e -> Mono.just("Error generating report: " + e.getMessage()));
    }

    private Mono<String> generateAndSaveReport(List<Activities> activities, String reportFormat, String path) {
        return Mono.fromCallable(() -> {
            // load file and compile it
            File file = ResourceUtils.getFile("classpath:activities.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(activities);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Angie");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            saveReport(jasperPrint, reportFormat, path);

            return "Report generated successfully. Path: " + path;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private void saveReport(JasperPrint jasperPrint, String reportFormat, String path) throws JRException {
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\activities.html");
        } else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\activities.pdf");
        } else {
            throw new IllegalArgumentException("Invalid report format: " + reportFormat);
        }
    }

}
