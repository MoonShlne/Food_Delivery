#### **一、 核心思想**



将热点数据（如频繁查询的菜品、分类信息）存入 Redis，是为了减轻数据库的压力，大幅提升应用的响应速度。每次查询时，我们遵循“**先查缓存，后查数据库**”的原则。

- **缓存命中 (Cache Hit)**：如果要查询的数据在 Redis 中存在，则直接从 Redis 中获取并返回，不再访问数据库。
- **缓存穿透 (Cache Miss)**：如果要查询的数据在 Redis 中不存在，则访问数据库，查询到数据后，先将其**放入 Redis 缓存**，然后再返回给用户。

当数据发生变动时（如修改、删除），我们必须同步更新 Redis 中的数据，以保证数据的一致性。这个过程称为**缓存管理**。



#### **二、 两种实现方式**



1. **手动编码**：在业务代码中，手动注入 `RedisTemplate`，并编写 `set`, `get`, `del` 等操作来管理缓存。
   - **优点**：控制力最强，可以实现非常复杂的缓存逻辑。
   - **缺点**：代码侵入性高，缓存逻辑与业务逻辑耦合在一起，不够优雅。
2. **使用 Spring Cache 注解（推荐）**：通过在方法上添加注解，以 AOP 的方式将缓存逻辑与业务逻辑解耦，代码更简洁、更易于维护。
   - **前提**：必须在项目的一个配置类或主启动类上添加 **`@EnableCaching`** 注解来开启 Spring Cache 功能。



#### **三、 Spring Cache 核心注解详解**



| 注解             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| `@EnableCaching` | 开启缓存注解功能，通常加在启动类上。                         |
| `@Cacheable`     | 在方法执行前先查询缓存中是否有数据，如果有数据，则直接返回缓存数据；如果没有缓存数据，调用方法并将方法返回值放到缓存中。 |
| `@CachePut`      | 将方法的返回值放到缓存中。                                   |
| `@CacheEvict`    | 将一条或多条数据从缓存中删除。                               |



#### **添加 `@EnableCaching` 注解**

 `@EnableCaching` 添加到您的**主启动类**上。





##### **1. `@Cacheable`：查询缓存 / 写入缓存**



这是最常用、也最核心的注解，它完美实现了“先查缓存，没有再查数据库并写入缓存”的逻辑。

- **注解**：`@Cacheable(cacheNames = "userCache", key = "#id")`

- **作用**：在方法执行**前**，Spring 会先根据 `cacheNames` 和 `key` 生成一个 Redis 的键（例如 `userCache::1`），然后去 Redis 中查找。

  - **如果缓存命中**：直接将 Redis 中的数据反序列化后返回，**方法体内的代码将不会被执行**。
  - **如果缓存未命中**：**执行方法体**（查询数据库），然后 Spring 会自动将方法的**返回值**存入 Redis 缓存（Key 就是之前生成的那个），最后再将返回值返回。

- **示例代码**：

  Java

  ```
  // #id 是一个 SpEL 表达式，表示使用方法的第一个参数 id 作为 key
  @Cacheable(cacheNames = "userCache", key = "#id")
  @GetMapping
  public User getById(Long id){
      // 只有当缓存中没有数据时，这行代码才会被执行
      User user = userMapper.selectById(id);
      return user;
  }
  ```



##### **2. `@CachePut`：更新缓存**



`@CachePut` 的主要应用场景是**新增**或**修改**数据时，它能确保缓存中的数据始终是**最新**的。

- **注解**：`@CachePut(cacheNames = "userCache", key = "#user.id")`

- **作用**：这个注解与 `@Cacheable` 不同，它**总是会执行方法体内的代码**。在方法成功执行后，它会**无条件地**将方法的**返回值**存入或更新到指定的 Redis 缓存中。

- **示例代码 (新增)**：

  Java

  ```
  // 使用 #result.id 可以获取到【方法返回对象】的 id 属性
  // 这对于新增操作非常重要，因为传入的 user 对象 id 为 null
  @PostMapping
  @CachePut(cacheNames = "userCache", key = "#result.id")
  public User save(@RequestBody User user){
      userMapper.insert(user); // MyBatis-Plus 会将自增 ID 回填到 user 对象中
      return user;
  }
  ```



##### **3. `@CacheEvict`：删除缓存**



当数据库中的数据被删除时，我们需要同步地将缓存中对应的数据也删除掉。

- **注解**：`@CacheEvict(cacheNames = "userCache", key = "#id")`

- **作用**：在方法成功执行后，根据 `cacheNames` 和 `key` 生成 Redis 的键，并执行 **`DEL`** 命令删除该缓存。

- **示例代码 (删除单条)**：

  Java

  ```
  @DeleteMapping
  @CacheEvict(cacheNames = "userCache", key = "#id")
  public void deleteById(Long id){
      userMapper.deleteById(id);
  }
  ```

- **示例代码 (删除全部)**： `@CacheEvict` 还有一个强大的属性 `allEntries = true`，用于清空整个缓存区域。

  Java

  ```
  @DeleteMapping("/delAll")
  @CacheEvict(cacheNames = "userCache", allEntries = true)
  public void deleteAll(){
      // 注意：这里可能需要执行删除数据库所有用户的逻辑
      userMapper.deleteAll();
  }
  ```





![image-20251004182719116](D:\porjects\Food_Delivery\Food_Delivery\md\图片\image-20251004182719116.png)





