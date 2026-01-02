package com.ebanking.accountservice.mapper;

import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.response.AccountDTO;
import com.ebanking.accountservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ebanking.accountservice.entity.AccountStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Entity to DTO
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "id", target = "accountId")
    AccountDTO toDTO(Account account);

    List<AccountDTO> toDTOList(List<Account> accounts);

    // DTO to Entity
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
    Account toEntity(AccountCreateRequest dto);
}
