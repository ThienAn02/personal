package com.annie.account.service;

import com.annie.account.constants.AccountExceptionMessage;
import com.annie.account.dao.AccountDao;
import com.annie.account.dto.AccountDto;
import com.annie.account.dto.AccountUpdateRequest;
import com.annie.account.entity.Account;
import com.annie.base.exception.IdNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class AccountService {

    @Inject
    private AccountDao accountDao;

    @Inject
    private AccountMapper accountMapper;

    public List<AccountDto> findAll() {
        return accountMapper.toListDto(accountDao.findAll());
    }

    public AccountDto findById(Long accountId) {
        Account account = accountDao.findById(accountId)
                .orElseThrow(() -> new IdNotFoundException(AccountExceptionMessage.ACCOUNT_NOT_FOUND_BY_ID + accountId));
        return accountMapper.toDto(account);
    }

    public AccountDto findByEmail(String email) {
        Account account = accountDao.getByEmail(email)
                .orElseThrow(() -> new IdNotFoundException(AccountExceptionMessage.ACCOUNT_NOT_FOUND_BY_EMAIL + email));
        return accountMapper.toDto(account);
    }

    public AccountDto updateAccount(Long accountId, AccountUpdateRequest accountDTO) {
        Account updateAccount = accountDao.findById(accountId)
                .orElseThrow(() -> new IdNotFoundException(AccountExceptionMessage.ACCOUNT_NOT_FOUND_BY_ID+ accountId));

        updateAccount.setEmail(accountDTO.getEmail());
        return accountMapper.toDto(accountDao.update(updateAccount));
    }

    public void deleteAccount(Long accountId) {
        Account deleteAccount = accountDao.findById(accountId)
                .orElseThrow(() -> new IdNotFoundException(AccountExceptionMessage.ACCOUNT_NOT_FOUND_BY_ID + accountId));
        accountDao.delete(deleteAccount);
    }
}
