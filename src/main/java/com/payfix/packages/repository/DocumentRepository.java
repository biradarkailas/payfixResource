package com.payfix.packages.repository;

import com.payfix.packages.domain.Document;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    @Query("select document from Document document where document.user.login = ?#{principal.username}")
    List<Document> findByUserIsCurrentUser();
}
