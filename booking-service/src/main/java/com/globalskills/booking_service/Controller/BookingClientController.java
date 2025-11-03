package com.globalskills.booking_service.Controller;

import com.globalskills.booking_service.Dto.WebHookRequest;
import com.globalskills.booking_service.Service.BookingCommandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-client")
@SecurityRequirement(name = "api")
public class BookingClientController {

    @Autowired
    BookingCommandService bookingCommandService;

    @PostMapping("/booking/status")
    public ResponseEntity<?> BookingStatus(@RequestBody WebHookRequest request){
        boolean response = bookingCommandService.updateBookingStatus(request);
        return ResponseEntity.ok(response);
    }

}
