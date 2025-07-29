package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ReportResponse;
import com.ngphthinh.flower.serivce.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/invoice/{id}")
    public ApiResponse<ReportResponse> getInvoiceReport(@PathVariable("id") Long id) throws JRException, IOException {
        return ApiResponse.<ReportResponse>builder()
                .code(200)
                .message("Report generated successfully")
                .data(reportService.generateReport(id))
                .build();
    }
}
