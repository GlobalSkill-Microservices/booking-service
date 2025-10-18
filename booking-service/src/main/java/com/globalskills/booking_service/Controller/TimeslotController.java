package com.globalskills.booking_service.Controller;

import com.globalskills.booking_service.Common.BaseResponseAPI;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.TimeslotRequest;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Service.TimeslotCommandService;
import com.globalskills.booking_service.Service.TimeslotQueryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timeslot")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TimeslotController {

    @Autowired
    TimeslotCommandService timeslotCommandService;

    @Autowired
    TimeslotQueryService timeslotQueryService;

    @PostMapping
    public ResponseEntity<?> createByTeacher(@Parameter(hidden = true)
                                             @RequestHeader(value = "X-User-ID",required = false) Long accountId,
                                             @RequestBody TimeslotRequest request){
        TimeslotResponse response = timeslotCommandService.createByTeacher(accountId, request);
        BaseResponseAPI<TimeslotResponse> responseAPI = new BaseResponseAPI<>(true,"Create timeslot successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateByTeacher(@PathVariable Long id,
                                             @RequestBody TimeslotRequest request){
        TimeslotResponse response = timeslotCommandService.updateByTeacher(id, request);
        BaseResponseAPI<TimeslotResponse> responseAPI = new BaseResponseAPI<>(true,"Update timeslot successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        timeslotCommandService.delete(id);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Delete timeslot successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeslotById(@PathVariable Long id){
        TimeslotResponse response = timeslotQueryService.getTimeslotById(id);
        BaseResponseAPI<TimeslotResponse> responseAPI = new BaseResponseAPI<>(true,"Get timeslot successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/all/current-user")
    public ResponseEntity<?> getAllByAccountId(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(defaultValue = "desc") String sortDir,
                                               @Parameter(hidden = true)
                                               @RequestHeader(value = "X-User-ID",required = false) Long accountId){
        PageResponse<TimeslotResponse> response = timeslotQueryService.getAllByAccountId(page, size, sortBy, sortDir, accountId);
        BaseResponseAPI<PageResponse<TimeslotResponse>> responseAPI = new BaseResponseAPI<>(true,"Get timeslot successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }
}
