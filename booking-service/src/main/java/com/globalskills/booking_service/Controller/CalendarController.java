package com.globalskills.booking_service.Controller;

import com.globalskills.booking_service.Common.BaseResponseAPI;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.CalendarResponse;
import com.globalskills.booking_service.Service.CalendarCommandService;
import com.globalskills.booking_service.Service.CalendarQueryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar")
@SecurityRequirement(name = "api")
public class CalendarController {

    @Autowired
    CalendarCommandService calendarCommandService;

    @Autowired
    CalendarQueryService calendarQueryService;


    @PostMapping
    public ResponseEntity<?> create(@Parameter(hidden = true)
                                    @RequestHeader(value = "X-User-ID",required = false) Long accountId){
        calendarCommandService.create(accountId);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Create calendar successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/all/current-user")
    public ResponseEntity<?> getByCurrentAccount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(hidden = true)
            @RequestHeader(value = "X-User-ID",required = false) Long accountId
    ){
        PageResponse<CalendarResponse> response = calendarQueryService.getbyAccount(page, size, sortBy, sortDir, accountId);
        BaseResponseAPI<PageResponse<CalendarResponse>> responseAPI = new BaseResponseAPI<>(true,"Get calendar successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }


    @GetMapping("/all/{accountId}")
    public ResponseEntity<?> getByAccountId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @PathVariable Long accountId
    ){
        PageResponse<CalendarResponse> response = calendarQueryService.getbyAccount(page, size, sortBy, sortDir, accountId);
        BaseResponseAPI<PageResponse<CalendarResponse>> responseAPI = new BaseResponseAPI<>(true,"Get calendar successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }
}
