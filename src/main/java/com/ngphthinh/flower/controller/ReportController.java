package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ReportResponse;
import com.ngphthinh.flower.serivce.ExcelReportService;
import com.ngphthinh.flower.serivce.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;
    private final ExcelReportService excelReportService;

    public ReportController(ReportService reportService, ExcelReportService excelReportService) {
        this.reportService = reportService;
        this.excelReportService = excelReportService;
    }

    @GetMapping("/invoice/{id}")
    public ApiResponse<ReportResponse> getInvoiceReport(@PathVariable("id") Long id) throws JRException, IOException {
        return ApiResponse.<ReportResponse>builder()
                .code(200)
                .message("Report generated successfully")
                .data(reportService.generateReport(id))
                .build();
    }

    @GetMapping("/invoice-to-excel")
    public ApiResponse<ReportResponse> getInvoiceReportToExcel(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.<ReportResponse>builder()
                .code(200)
                .message("Report generation to Excel is not implemented yet")
                .data(excelReportService.generateOrderReport(startDate, endDate))
                .build();
    }

}
