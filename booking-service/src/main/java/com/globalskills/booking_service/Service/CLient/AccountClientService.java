package com.globalskills.booking_service.Service.CLient;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.Feign.AccountClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccountClientService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountClient accountClient;

    public AccountDto fetchAccount(Long id){
        return accountClient.getAccountById(id);
    }
    public List<AccountDto> fetchListAccount(List<Long> ids){
        return accountClient.getAccountByIds(ids);
    }

    public Map<Long, AccountDto> getAccountMapByIds(List<Long> accountIds) {
        List<AccountDto> accountDtos = fetchListAccount(accountIds);
        return accountDtos.stream()
                .collect(Collectors.toMap(AccountDto::getId, Function.identity()));
    }

}
