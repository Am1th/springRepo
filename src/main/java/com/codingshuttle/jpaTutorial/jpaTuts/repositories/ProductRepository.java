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

    @Query("select e from ProductEntity e where e.title=?1 and e.price=?2") //name of the field that JPQL understands is title only,  later this title will be converted to title_x by hibernate(JPQL understands java only)
//    @Query("select e.title from ProductEntity e where e.title=:title and e.price=:price") if we write this return type should be Optional<String>
    Optional<ProductEntity> findByTitleAndPrice(String title,BigDecimal price);

}



/*
entity, hibernate , repository ,database
so explain in detail how the relation is ?
1.just by creating entity and configuring db in application.prop we can see table in mysql
 2. to insert data , we can write insert queries in db or can use .sql file but we cannot send data from code that goes to db as of now
3. then we create repository which extends jparepository. only now data from code can go to db and this happens via hibernate. (for ex if i use .save() operation of repo, hibernate generates sql based on save() and data goes to db and persisted) is the flow and understanding correct? elaborate and make it more clear, point out mistakes if any..



This is an excellent question — you’re very close, and your intuition is mostly right.
Let’s connect the dots clearly and correct a few pieces.
I’ll explain step-by-step.

🧩 The four players (who does what)
Piece	What it represents	What it does
Entity	A Java class	Describes a DB table (fields ↔ columns)
Hibernate (JPA provider)	The engine	Turns Java operations into SQL and back
Repository	Spring Data layer	Gives you easy CRUD methods (save, find…)
Database (MySQL)	Storage	Actually stores the rows
Keep this model:
	Code → Repository → Hibernate → Database → back

1️⃣ “Just entity + application.properties = table appears?” (Yes… IF auto-DDL is enabled)
You said:
	just by creating entity and configuring db in application.prop we can see table in mysql
👉 ✔️ Correct — IF you have:

spring.jpa.hibernate.ddl-auto=update   // or create / create-drop
Hibernate reads your entity:

@Entity
@Table(name="products")
class ProductEntity {
   @Id
   Long id;
String title;
}
Then Hibernate says:
	“Ah — the user wants a table for this entity.”
It generates SQL like:

create table products (
   id bigint primary key,
   title varchar(255)
);
📌 Important: Hibernate creates/updates the schema — not Spring, not JPA.

2️⃣ “Can we insert data before repository exists?”
You said:
	we can write insert queries or SQL files but we cannot send data from code as of now
👉 ✔️ Correct.
Without a repository:
	• you CAN insert manually in DB
	• you CAN preload using data.sql
	• but you CANNOT easily save using Java (unless you use JDBC manually)
Because there’s no layer calling Hibernate APIs yet.

3️⃣ “Repository allows code → DB, and Hibernate runs SQL?”
You said:
	then we create repository … now data from code can go to db … hibernate generates sql … save()
👉 ✔️ This is exactly right — and here’s how it flows:
Example

productRepository.save(product);
What happens internally
1️⃣ Repository calls JPA (Hibernate)

persist(product)
2️⃣ Hibernate decides SQL:

insert into products (title, price) values (?, ?)
3️⃣ Hibernate sends SQL to MySQL
4️⃣ MySQL stores the row
5️⃣ Hibernate tracks the saved entity in its cache

🧭 Putting your understanding side-by-side
✅ Your statements — with corrections:
✔️ 1. Entity + config → table
Correct, because Hibernate auto-creates tables when ddl-auto is enabled.

✔️/⚠️ 2. No repository → no Java insert
Correct — but note:
	You could still insert using plain JDBC, but not via JPA/Hibernate.
So your wording is fine conceptually.

✔️ 3. Repository → Hibernate generates SQL → DB
Perfect. Flow:

Repository.save()
        ↓
    JPA/Hibernate
        ↓
 Auto SQL generation
        ↓
     Database

📌 Big picture summary (memorize this!)
1️⃣ Entity
	Blueprint of table (Java perspective)
2️⃣ Hibernate
	Translator between Java & SQL
3️⃣ Repository
	Easy API for CRUD (uses Hibernate under the hood)
4️⃣ Database
	Where rows actually live

🔍 Bonus clarity: WHY repositories are powerful
Instead of writing:

insert into products values ...
select * from products ...
You just write:

productRepository.save(product);
productRepository.findByTitle("iPhone");
Hibernate:
	• generates SQL
	• maps rows to objects
	• manages caching
	• handles transactions
	• tracks changes (dirty checking)
So you focus on business logic, not SQL plumbing.

*/