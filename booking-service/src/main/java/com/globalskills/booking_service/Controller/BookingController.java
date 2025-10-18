package com.globalskills.booking_service.Controller;

import com.globalskills.booking_service.Common.BaseResponseAPI;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.BookingRequest;
import com.globalskills.booking_service.Dto.BookingResponse;
import com.globalskills.booking_service.Enum.BookingStatus;
import com.globalskills.booking_service.Service.BookingCommandService;
import com.globalskills.booking_service.Service.BookingQueryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BookingController {

    @Autowired
    BookingCommandService bookingCommandService;

    @Autowired
    BookingQueryService bookingQueryService;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookingRequest request,
                                    @Parameter(hidden = true)
                                    @RequestHeader(value = "X-User-ID",required = false) Long accountId){
        BookingResponse response = bookingCommandService.create(request, accountId);
        BaseResponseAPI<BookingResponse> responseAPI = new BaseResponseAPI<>(true,"Create booking successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id){
        BookingResponse response = bookingQueryService.getBookingById(id);
        BaseResponseAPI<BookingResponse> responseAPI = new BaseResponseAPI<>(true,"Get booking successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/all/current-user")
    public ResponseEntity<?> getAllBCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(hidden = true)
            @RequestHeader(value = "X-User-ID",required = false) Long accountId,
            @RequestParam(required = false)BookingStatus bookingStatus
            ){
        PageResponse<BookingResponse> response = bookingQueryService.getAllBCurrentUser(page, size, sortBy, sortDir, accountId, bookingStatus);
        BaseResponseAPI<PageResponse<BookingResponse>> responseAPI = new BaseResponseAPI<>(true,"Get all booking with status: "+ bookingStatus + " successfully",response,null);
        return ResponseEntity.ok(responseAPI);

    }


}
