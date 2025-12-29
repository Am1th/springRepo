package com.codingshuttle.jpaTutorial.jpaTuts.repositories;

import com.codingshuttle.jpaTutorial.jpaTuts.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findByTitle(String title);

//    to get elements after a certain date
    List<ProductEntity> findByCreatedAtAfter(LocalDateTime after);

    //filter by quantity and price
    List<ProductEntity> findByQuantityAndPrice(Integer quantity, BigDecimal price);

    List<ProductEntity> findByQuantityGreaterThanAndPriceLessThan(Integer quantity, BigDecimal price);

    List<ProductEntity> findByTitleLike(String title);

    List<ProductEntity> findByTitleContaining(String title);

    List<ProductEntity> findByTitleContainingIgnoreCase(String title);

//    Optional<ProductEntity> findByTitleAndPrice(String title, BigDecimal price); // we can be sure there will be only one ProductEntity (and not list of entities) with a particular title and price as we wrote unique constraint

//    @Query("select e from ProductEntity e where e.title=?1 and e.price=?2") //name of the field that JPQL understands is title only,  later this title will be converted to title_x by hibernate(JPQL understands java only)
    @Query("select e.title from ProductEntity e where e.title=:title and e.price=:price")
    Optional<ProductEntity> findByTitleAndPrice(String title,BigDecimal price);

}
