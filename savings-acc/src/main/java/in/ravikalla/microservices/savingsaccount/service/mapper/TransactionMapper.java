package in.ravikalla.microservices.savingsaccount.service.mapper;

import in.ravikalla.microservices.savingsaccount.domain.*;
import in.ravikalla.microservices.savingsaccount.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {SavingsaccountMapper.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "savingsaccount.id", target = "savingsaccountId")
    @Mapping(source = "savingsaccount.accountnumber", target = "savingsaccountAccountnumber")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(source = "savingsaccountId", target = "savingsaccount")
    Transaction toEntity(TransactionDTO transactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
