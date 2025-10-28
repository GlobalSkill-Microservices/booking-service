package com.globalskills.booking_service.Common.Feign;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="user-service", url = "https://gateway-service-w2gi.onrender.com/api/user-client",configuration = FeignClientInterceptor.class)
public interface AccountClient {

    @GetMapping("/{id}")
    AccountDto getAccountById(@PathVariable Long id);

    @GetMapping("/batch")
    List<AccountDto> getAccountByIds(@RequestParam("ids") List<Long> ids);

}
