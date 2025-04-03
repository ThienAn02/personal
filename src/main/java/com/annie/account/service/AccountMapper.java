package com.annie.account.service;

import com.annie.account.dto.AccountDto;
import com.annie.account.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")

public interface AccountMapper {
    AccountDto toDto(Account account);
    List<AccountDto> toListDto(List<Account> accounts);
}
