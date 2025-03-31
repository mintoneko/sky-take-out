## 导学

- 适合人群

项目经验->实习

应届毕业生->毕设

在职人员->更新技术栈

- 项目优势

![image-20250331115004228](images/image-20250331115004228.png)

- 模块化开发

![image-20250331115110373](images/image-20250331115110373.png)

- 收获

![image-20250331120654421](images/image-20250331120654421.png)

- 软件开发流程

![image-20250331125934339](images/image-20250331125934339.png)

- 软件角色分工

![image-20250331130014879](images/image-20250331130014879.png)

- 软件环境

![image-20250331130243988](images/image-20250331130243988.png)

- 技术选型

![image-20250331132619875](images/image-20250331132619875.png)

## 1.环境配置

### 前端配置

- 启动nginx代理服务器代理即可

### 后端配置

- 项目结构

![image-20250331144252496](images/image-20250331144252496.png)

![image-20250331144404936](images/image-20250331144404936.png)

![image-20250331144425432](images/image-20250331144425432.png)

![image-20250331144452278](images/image-20250331144452278.png)

- 数据库设计

![image-20250331144823838](images/image-20250331144823838.png)

- 前后端联调

![image-20250331145354995](images/image-20250331145354995.png)

> 调试查看一个前端登陆请求是如何跳转的。

- API不一致问题

![image-20250331151938914](images/image-20250331151938914.png)

![image-20250331152056672](images/image-20250331152056672.png)

![image-20250331152153958](images/image-20250331152153958.png)

![image-20250331152222182](images/image-20250331152222182.png)

### 完善登陆功能

1. 修改数据库中的密码，改为MD5加密之后的密文
2. 修改Java代码，前端提交的密码进行MD5加密之后再跟数据库中密码对比

```java
password= DigestUtils.md5DigestAsHex(password.getBytes());
```

### 接口文档

- 开发流程

![image-20250331195455730](images/image-20250331195455730.png)

- Swegger使用

导入依赖

```xml
<!--knife4j-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
</dependency>
```

> localhost:8080/doc.html

打开即可查看，具体配置查看项目的`package com.sky.config`。

![image-20250331202026632](images/image-20250331202026632.png)

这几种Api形式可以查看：

```
EmployeeLoginDTO.java
EmployeeLoginVO.java
```

## 2.员工管理

![image-20250331204046870](images/image-20250331204046870.png)

### 新增员工

创建接口：修改EmployeeController --> 数据转换：DTO->VO --> 利用持久层mapper：编写SQL代码

大概就是这么个逻辑，不过注意JWT拦截器，Swegger测试需要对用token处理

### 代码完善

1. 用户名重复异常处理
2. 新增员工的创建人id和修改人id需要完善