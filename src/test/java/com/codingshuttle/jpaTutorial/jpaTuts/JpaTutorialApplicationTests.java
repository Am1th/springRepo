package com.codingshuttle.jpaTutorial.jpaTuts;

import com.codingshuttle.jpaTutorial.jpaTuts.entities.ProductEntity;
import com.codingshuttle.jpaTutorial.jpaTuts.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class JpaTutorialApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testRepository(){   //wkt you will see createdAt,updatedAt fields also filled in dbeaver for below as the below is done by hibernate(hibernate generates sql query to insert below data), whereas the values from data.sql that were filled in the table will have null data for these createdAt , updatedAt columns as they are run by us(wkt instead of writing insert query in dbeaver, we wrote insert query in data.sql and ran..this is also kind of manual only),and hibernate is not involved.
		ProductEntity productEntity = ProductEntity.builder()
				.sku("nestle234")
				.title("Nestle chocolate")
				.price(BigDecimal.valueOf(123.45))
				.quantity(12)
				.build();
		ProductEntity savedProductEntity = productRepository.save(productEntity);
		System.out.println(savedProductEntity);
	}

	@Test
	void getRepository() { //tests run independently, the above entry is not showing in dbeaver as i am only running this test. you must run above test (or can run all tests) if you want to see above entry in dbeaver. wkt Already existing data in dbeaver will show anyway.
//		List<ProductEntity> productEntities = productRepository.findAll();
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByTitle("Pepsi"); //there is certain way in which we write method names and these are decrypted by hibernate and hibernate will generate the query for us
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByCreatedAtAfter(LocalDateTime.of(2024,1,1,0,0,0));
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByQuantityAndPrice(4, BigDecimal.valueOf(12.4));
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByQuantityGreaterThanAndPriceLessThan(1, BigDecimal.valueOf(14.4)); //look at query generated and condition
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByTitleLike("%Choco%"); //run above test case, else this will not show any result
//		System.out.println(productEntities);

//		List<ProductEntity> productEntities = productRepository.findByTitleContaining("Choco");
//		System.out.println(productEntities);

		List<ProductEntity> productEntities = productRepository.findByTitleContainingIgnoreCase("CHoCo");
		System.out.println(productEntities);
	}

    @Test
	void getSingleFromRepository() {
		Optional<ProductEntity> productEntity = productRepository.findByTitleAndPrice("Pepsi", BigDecimal.valueOf(14.4));
        productEntity.ifPresent(System.out::println);
		//productEntity.map(productEntity1 -> productEntity1.getTitle()); //The map() function is intended to return a transformed value. Since System.out.println() does not return anything (void), it doesn't fit into map() properly.
		//Use ifPresent() instead of map():
	}

}
