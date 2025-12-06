# Integer Expression Parser

一个使用 Java 25 和 Maven 的整数表达式解析器项目。

## 项目说明

本项目已从 Eclipse 项目转换为 Maven 项目，可以在 VS Code 中直接使用。

## 环境要求

- Java 25
- Maven 3.6+
- VS Code (推荐安装 Java Extension Pack)

## 项目结构

```
ex18-intexpr/
├── src/
│   └── intexpr/
│       ├── IntegerExpression.java      # 表达式接口
│       ├── IntegerExpressionParser.java # 解析器主类
│       ├── IntegerExpressionParserTest.java # 测试类
│       ├── IntegerExpression.g          # 语法定义文件
│       ├── Number.java                  # 数字表达式
│       ├── Plus.java                    # 加法表达式
│       └── Times.java                   # 乘法表达式
├── lib/
│   └── parserlib.jar                    # MIT parserlib 依赖
├── pom.xml                               # Maven 配置文件
└── .vscode/
    ├── settings.json                     # VS Code Java 设置
    └── launch.json                       # 调试配置
```

## 如何使用

### 首次使用 - 安装 parserlib 依赖

如果是首次克隆项目或清理了本地 Maven 仓库，需要先安装 parserlib.jar 到本地 Maven 仓库：

```bash
mvn install:install-file -Dfile=lib/parserlib.jar -DgroupId=edu.mit.eecs -DartifactId=parserlib -Dversion=3.2.0 -Dpackaging=jar
```

**注意**：只需要执行一次，之后 Maven 会从本地仓库 (`~/.m2/repository/`) 自动加载。

### 编译项目

```bash
mvn clean compile
```

### 运行主程序

```bash
mvn exec:java -Dexec.mainClass="intexpr.IntegerExpressionParser"
```

### 运行测试

```bash
mvn test
```

注意：当前语法文件仅支持加法运算，乘法相关测试会失败。

### 打包项目

```bash
mvn clean package -DskipTests
```

生成的 JAR 文件位于 `target/intexpr-1.0-SNAPSHOT.jar`

## VS Code 调试

项目已配置 VS Code 调试支持：

1. 打开 VS Code
2. 按 F5 或点击"运行和调试"
3. 选择 "Debug IntegerExpressionParser" 配置

## 依赖说明

- **JUnit Jupiter 5.10.1**: 用于单元测试
- **MIT Parserlib 3.2.0**: 用于语法解析
  - JAR 文件位于 `lib/parserlib.jar`
  - 已通过 `mvn install:install-file` 安装到本地 Maven 仓库
  - **可以删除 lib 目录**，但建议保留以便其他人使用或重新安装

## Maven 转换变更

1. ✅ 创建了 `pom.xml` 配置文件，使用 Java 25
2. ✅ 安装 parserlib.jar 到本地 Maven 仓库
3. ✅ 更新代码以使用 classpath resource 加载语法文件
4. ✅ 移除 Eclipse 配置文件（.classpath, .project, .settings）
5. ✅ 配置 VS Code 调试和 Java 支持
6. ✅ 更新 .gitignore 文件

## 注意事项

- 语法文件 (`IntegerExpression.g`) 当前仅支持加法和括号，不支持乘法运算
- 使用 Java 25，确保已安装对应版本的 JDK
- parserlib 依赖已从 lib/parserlib.jar 安装到本地 Maven 仓库
