package in.ravikalla.microservices.savingsaccount.repository;

import in.ravikalla.microservices.savingsaccount.domain.Savingsaccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Savingsaccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SavingsaccountRepository extends JpaRepository<Savingsaccount, Long>, JpaSpecificationExecutor<Savingsaccount> {

}
