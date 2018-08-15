package in.ravikalla.microservices.savingsaccount.service.mapper;

import in.ravikalla.microservices.savingsaccount.domain.*;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Savingsaccount and its DTO SavingsaccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SavingsaccountMapper extends EntityMapper<SavingsaccountDTO, Savingsaccount> {



    default Savingsaccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        Savingsaccount savingsaccount = new Savingsaccount();
        savingsaccount.setId(id);
        return savingsaccount;
    }
}
