# Development Process

## 1. Create Entity

- Define the entity class with appropriate fields and annotations.
- Use `@Entity`, `@Id`, and other JPA annotations to map it to the database table.
- To define **foreign keys**, use `@ManyToOne`, `@JoinColumn` for relationships.
- Example:
  ```java
  @Entity
  public class Beehive {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String name;

      // Foreign key to Address
      @ManyToOne
      @JoinColumn(name = "address_id", nullable = false)
      private Address address;

      // Foreign key to AppUser (Owner)
      @ManyToOne
      @JoinColumn(name = "owner_id", nullable = false)
      private AppUser owner;
  }
  ```

### Foreign Keys:

- **@ManyToOne**: Defines a many-to-one relationship, used for foreign keys.
- **@JoinColumn**: Specifies the name of the foreign key column and whether it's nullable.

## 2. Create DTO and Form

- Create two classes:
    - **DTO** for outputting data.
    - **Form** for handling input data.
- Example:
  ```java
  public class MyEntityDto {
      private Long id;
      private String name;
      // other fields...
  }

  public class MyEntityForm {
      private String name;
      // fields for input...
  }
  ```

## 3. Create Repository

- Implement `JpaRepository` to handle database operations.
- Example:
  ```java
  public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
  }
  ```

## 4. Create Mapper

- Map between **Entity**, **DTO**, and **Form**.
- Example:

```java

@Mapper(componentModel = "spring")
public interface BeehiveMapper {
    BeehiveMapper INSTANCE = Mappers.getMapper(BeehiveMapper.class);

    //    Entity -> Dto
    BeehiveDto getDto(Beehive entity);

    //    Form -> Entity
    @Mapping(target = "targetExample", source = "sourceExample")
    Beehive getEntity(BeehiveForm form);
}
  ```

- @Mapping can be used to define a mapping between 2 properties with different names

## 5. Create Service

- Implement `CrudService` to define business logic for the entity.
- Example:
  ```java
  @Service
  public class MyEntityService implements CrudService<MyEntityDto, MyEntityForm> {
      private final MyEntityRepository repository;
      private final MyEntityMapper mapper;

      public MyEntityService(MyEntityRepository repository, MyEntityMapper mapper) {
          this.repository = repository;
          this.mapper = mapper;
      }

      // Implement CRUD methods...
  }
  ```

## 6. Create Controller

- Extend `ControllerBase` to handle HTTP requests for the entity.
- Example:
  ```java
  @RestController
  @RequestMapping("/api/my-entity")
  public class MyEntityController extends ControllerBase<MyEntityDto, MyEntityForm> {
      private final MyEntityService service;

      @Autowired
      public MyEntityController(MyEntityService service) {
          super(service);
          this.service = service;
      }
  }
  ```
