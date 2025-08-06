package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.dto.response.ReportResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelReportService {
    private final OrderService orderService;

    private static final String MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public ExcelReportService(OrderService orderService) {
        this.orderService = orderService;
    }

    public ReportResponse generateOrderReport(LocalDate startDate, LocalDate endDate) {

        var orders = orderService.getAllOrderByDateRange(startDate, endDate);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");
        int rowIndex = 0;

        Row header = sheet.createRow(rowIndex++);
        String[] headers = {"Order ID", "Customer Name", "Phone", "Order Date", "Product", "Qty", "Price", "Subtotal"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
        }

        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);

        int startMerge = rowIndex;

        for (int i = 0; i < orders.size(); i++) {
            OrderResponse order = orders.get(i);
            String currentCustomer = order.getCustomerName();
            boolean isFirstDetail = true;

            for (OrderDetailResponse detail : order.getOrderDetailResponses()) {
                Row row = sheet.createRow(rowIndex++);

                if (isFirstDetail) {
                    row.createCell(0).setCellValue(order.getOrderId());
                    row.createCell(1).setCellValue(currentCustomer);
                    row.createCell(2).setCellValue(order.getCustomerPhone());
                    row.createCell(3).setCellValue(order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    isFirstDetail = false;
                } else {
                    row.createCell(0).setCellValue("");
                    row.createCell(1).setCellValue("");
                    row.createCell(2).setCellValue("");
                    row.createCell(3).setCellValue("");
                }

                row.createCell(4).setCellValue(detail.getProductName());
                row.createCell(5).setCellValue(detail.getQuantity());
                row.createCell(6).setCellValue(detail.getPrice());
                row.createCell(7).setCellValue(detail.getSubtotal());

                for (int c = 0; c <= 7; c++) {
                    row.getCell(c).setCellStyle(borderStyle);
                }
            }

            boolean isLastOrder = i == orders.size() - 1;
            String nextCustomer = isLastOrder ? null : orders.get(i + 1).getCustomerName();

            if (isLastOrder || !currentCustomer.equals(nextCustomer)) {
                int endMerge = rowIndex - 1;
                if (endMerge > startMerge) {
                    for (int col = 0; col <= 3; col++) {
                        sheet.addMergedRegion(new CellRangeAddress(startMerge, endMerge, col, col));
                    }
                }
                startMerge = rowIndex;
            }

        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            outputStream.toByteArray();
            return ReportResponse.builder()
                    .reportData(outputStream.toByteArray())
                    .mediaType(MEDIA_TYPE)
                    .build();
        } catch (IOException e) {
            return ReportResponse.builder()
                    .reportData(new byte[0])
                    .mediaType(MEDIA_TYPE)
                    .build();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
