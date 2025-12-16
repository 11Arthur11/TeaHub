package dev.parhamziaei.teahub.repository.jpa.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl {
    
//    private final EntityManager em;
//
//    @Override
//    public Optional<Invoice> findById(Long id) {
//        return em.createQuery("SELECT i FROM Invoice i WHERE i.id = :id", Invoice.class)
//                .setParameter("id", id)
//                .getResultList()
//                .stream()
//                .findFirst();
//    }
//
//    @Override
//    public Optional<Invoice> findByInvoiceToken(String token) {
//        return em.createQuery("SELECT i FROM Invoice i WHERE i.invoiceToken = :token", Invoice.class)
//                .setParameter("token", token)
//                .getResultList()
//                .stream()
//                .findFirst();
//    }
//
//    @Override
//    public Page<Invoice> findAll(Long userId, Pageable pageable) {
//        Query query = em.createQuery("SELECT i FROM Invoice i", Invoice.class);
//        query.setMaxResults(pageable.getPageSize());
//        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//        List<Invoice> invoices = query.getResultList();
//        long totalSize = em.createQuery("SELECT COUNT(i) FROM Invoice i", Long.class).getSingleResult();
//        return new PageImpl<>(invoices, pageable, totalSize);
//    }
//
//    @Override
//    public Page<Invoice> findAllByStatus(InvoiceStatus status, Pageable pageable) {
//        Query query = em.createQuery("SELECT i FROM Invoice i WHERE i.status = :status", Invoice.class)
//                .setParameter("status", status);
//        query.setMaxResults(pageable.getPageSize());
//        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//        List<Invoice> invoices = query.getResultList();
//        long totalSize = em.createQuery("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status", Long.class)
//                .setParameter("status", status)
//                .getSingleResult();
//        return new PageImpl<>(invoices, pageable, totalSize);
//    }
//
//    @Override
//    public Page<Invoice> findByUserId(Long userId, Pageable pageable) {
//        Query query = em.createQuery("SELECT i FROM Invoice i WHERE i.owner.id = :userId", Invoice.class)
//                .setParameter("userId", userId);
//        query.setMaxResults(pageable.getPageSize());
//        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//        List<Invoice> invoices = query.getResultList();
//        long totalSize = em.createQuery("SELECT COUNT(i) FROM Invoice i WHERE i.owner.id = :userId", Long.class)
//                .setParameter("userId", userId)
//                .getSingleResult();
//        return new PageImpl<>(invoices, pageable, totalSize);
//    }
//
//    @Override
//    public Page<Invoice> findByUserIdAndStatus(Long userId, InvoiceStatus status, Pageable pageable) {
//        Query query = em.createQuery("SELECT i FROM Invoice i WHERE i.owner.id = :userId AND i.status = :status", Invoice.class)
//                .setParameter("userId", userId)
//                .setParameter("status", status);
//        query.setMaxResults(pageable.getPageSize());
//        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//        List<Invoice> invoices = query.getResultList();
//        long totalSize = em.createQuery("SELECT COUNT(i) FROM Invoice i WHERE i.owner.id = :userId AND i.status = :status", Long.class)
//                .setParameter("userId", userId)
//                .setParameter("status", status)
//                .getSingleResult();
//        return new PageImpl<>(invoices, pageable, totalSize);
//    }
//
//    @Override
//    public void save(Invoice invoice) {
//
//    }
//
//    @Override
//    public void update(Invoice invoice) {
//
//    }
//
//    @Override
//    public void delete(Invoice invoice) {
//
//    }

}
