# SpringBoot—Demo
这一个基于SpringBoot的前后端分离多人在线博客平台
- 在线展示：![博客地址](http://8.137.18.37：8080")
- 项目材料
- - ![后端文档接口](https://github.com/jirengu-inc/vue-blog-preview)
- - ![前端代码](https://github.com/jirengu-inc/vue-blog-preview)

## 项目结构
- `Controller`: 存放 SpringBoot 对外接口，处理接收到的 HTTP 请求，对获取的请求参数进行验证和清洗，并将参数传递给业务逻辑 service 层。
- `Service`: 处理主要业务逻辑的方法实现，依赖于 Dao 层的数据库操作。
- `Dao`: 提供实现访问数据的方法,通过 MyBatis 完成和数据库的交互。
- `Mapper`: 存放使用 MyBatis 映射和数据库进行交互的 MYSQL 语句。 
- `Entity`: 用于存放实体类。
- `Converter`: 存放业务级别的实例和数据库实例的转换。  
- `Configuration`: 存放 Web 安全配置

## 自动化测试
- 代码质量
- - 使用 Maven 的 verify 周期绑定 CheckStyle 进行代码质量检查。
- 单元测试
- -对 Controller 层和 Service 层 进行了 JUnit 单元测试。在单元测试中使用 Mockito mock 进行不涉及真实依赖的检测。
- 集成测试
- - 对处理 HTTP 请求的接口进行继承测试，使用 HttpClient 模拟用户发送发送 HTTP 请求。使用 `mvn exec` 执行通过 Docker 创建启动和销毁数据库的操作，使用 Flyway 自动建表和插入测试数据。
- 自动化测试
- - 使用 TravisCi 对Github仓库进行测试，在每次 commit 里都进行测试，保证项目可演进性。
## 重现
#### clone 项目到本地
`git clone https://github.com/Procarihana/SpringBoot-Demo.git`
#### 使用 Docker 启动 MySQL数据库
` docker run --name springboot-mysql -e MYSQL_ROOT_PASSWORD=hana -e -p 3306:3306 -d mysql`
- Docker 使用注意事项
1. 需要外部访问数据库时，要进行 `-p`配置。
2. 数据库持久化需要进行 `-v`配置。
3. 远程访问数据库时，需要先通过 Docker 进入到 MySql 容器里对用户可登陆 IP 地址进行更改。
#### 使用 Flyway 进行数据库初始化
- 数据库新建以及 Maven 刷新后，可执行 Flyway 对数据库进行建表自动建表操作。
`mvn flyway:migrate`
#### 项目测试
- `mvn verify` 
#### 运行项目
- 运行 Run Application 类
- 访问 localhost:8080/
