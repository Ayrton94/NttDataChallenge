package com.bank.accounts.infrastructure.adapter.in.web;

import com.bank.accounts.application.usecase.ReportService;
import com.bank.accounts.infrastructure.adapter.in.web.dto.MovementReportRow;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{clientId}")
    public Flux<MovementReportRow> report(
            @PathVariable("clientId") Long clientId,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant endDate
    ) {
        return reportService.reportByCustomerAndDates(clientId, startDate, endDate);
    }
}