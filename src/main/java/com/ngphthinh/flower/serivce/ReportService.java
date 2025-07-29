package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.dto.response.ReportResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    private final OrderService orderService;


    public ReportService(OrderService orderService) {
        this.orderService = orderService;
    }


    public ReportResponse generateReport(Long orderId) throws IOException, JRException {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        InputStream reportStream = new ClassPathResource("reports/InvoiceA5Flower.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Map<String, Object> params = getStringObjectMap(orderResponse);


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderResponse.getOrderDetailResponses());
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, dataSource);

        return ReportResponse.builder()
                .reportData(JasperExportManager.exportReportToPdf(print))
                .build();
    }

    private static Map<String, Object> getStringObjectMap(OrderResponse orderResponse) {

        // Format order date to a string : "dd/MM/yyyy HH:mm:ss"

        String orderDate = orderResponse.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Format total price to a string with VND currency format
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00 VND");

        String deliveryMethod = orderResponse.getDeliveryMethod().equalsIgnoreCase("DELIVERY")
                ? "Giao hàng tận nơi"
                : "Nhận tại cửa hàng";


        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderResponse.getOrderId().toString());
        params.put("customerName", orderResponse.getCustomerName());
        params.put("orderDate", orderDate);
        params.put("totalPrice", decimalFormat.format(orderResponse.getTotalPrice()));
        params.put("deliveryMethod", deliveryMethod);
        params.put("note", orderResponse.getNote().isEmpty() ? "N/A" : orderResponse.getNote());
        return params;
    }
}
