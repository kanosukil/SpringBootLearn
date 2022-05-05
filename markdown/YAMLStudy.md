# ***YAML语言***

YAML Ain`t Markup Language 以数据为中心的标记语言(文件一般以.yml .yaml结尾)
+ 使用条件
    - SnakeYAML库添加到classpath下(`spring-boot-starter-web`或`spring-booter-starter`都对SnakeYAML库做了集成,有一SpringBoot便会自动添加到classpath下)

+ YAML语法
    - 使用缩进表示层级关系
    - 缩进时不允许Tab键,只允许空格
    - 缩进空格数不重要(但同级元素就必须左对齐)
    - 大小写敏感

e.g.
```
spring:
  profiles: dev
  datasource:
    url: jdbc:mysql://127.0.01/banchengbang_springboot
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
```

+ YAML常用写法
    - YAML支持的数据结构
        - 对象(键值对集合)
        - 数组(按次序排列的值)
        - 字面量\纯量(单个且不可拆分的值)

[comment]: <> "拓: YAML字面量写法"
[comment]: <> "字面量&#40;单个不可拆分的值&#41;[例:数字 字符串 布尔值 日期等]"
[comment]: <> "使用 `key: value` 表示一对键值对&#40;冒号后有空格且空格不可省略&#41;"
[comment]: <> "字面量直接写在 value 的位置上即可且默认字符串不需要单/双引号"

+ 键值对的表示
    - `key: value` (冒号后需要空格)
    - `key:{key: value, key: value, ...}`

缩进表示层级:

```
key: 
    child-key: value
    child-key2: value2
```

较为复杂的对象格式(可用问号加一个空格表示一个复杂的key, 配合一个冒号加一个空格表示一个value)

```
?  
    - complexkey1
    - complexkey2
:
    - complexvalue1
    - complexvalue2
```

+ 数组的表示
    - `-` 开头行表示构成一个数组

```
- A
- B
- C
```

- YAML支持多维数组(可使用行内表示)
```
key: [value1, value2, ...]
```

- 数据结构的子成员为数组(可在该结构下缩进一个空格)

```
-
 - A
 - B
 - C
```

***综合例子***

```
companies:
    -
        id: 1
        name: company1
        price: 200W
    -
        id: 2
        name: company2
        price: 500W
```

意为:companies属性是一个数组,每一个数组又有id name price三个属性构成

上式也可用`companies: [{id: 1,name: company1,price: 200W},{id: 2,name: company2,price: 500W}]`表示.

+ 复合结构
```
languages:
  - Ruby
  - Perl
  - Python 
websites:
  YAML: yaml.org 
  Ruby: ruby-lang.org 
  Python: python.org 
  Perl: use.perl.org
```
转换为json:
```
{ 
  languages: [ 'Ruby', 'Perl', 'Python'],
  websites: {
    YAML: 'yaml.org',
    Ruby: 'ruby-lang.org',
    Python: 'python.org',
    Perl: 'use.perl.org' 
  } 
}
```

+ 纯量/字面量的表示
    - 纯量 最基本的 不可拆分的值 有:
        - 字符串
        - 布尔值
        - 整数
        - 浮点数
        - Null
        - 时间
        - 日期

例子:
```
boolean: 
    - TRUE  #true,True都可以
    - FALSE  #false，False都可以
float:
    - 3.14
    - 6.8523015e+5  #可以使用科学计数法
int:
    - 123
    - 0b1010_0111_0100_1010_1110  #二进制表示
null:
    nodeName: 'node'
    parent: ~  #使用~表示null
string:
    - 哈哈
    - 'Hello world'  #可以使用双引号或者单引号包裹特殊字符
    - newline
      newline2  #字符串可以拆成多行，每一行之间(?)会被转化成一个空格
date:
    - 2018-02-17  #日期必须使用ISO 8601格式，即yyyy-MM-dd
datetime: 
    - 2018-02-17T15:02:31+08:00  #时间使用ISO 8601格式，时间和日期之间使用T连接，最后使用+代表时区
```
***注:字符串的单/双引号:单引号只会显示它里面的字符,不会转义;双引号会将转义字符转换成其要表达的含义显示***

**例:"\n"换行 '\n'显示\n**

+ 引用
    - `&` 锚点
    - `*` 别名

例子:
```
defaults: &defaults
  adapter:  postgres
  host:     localhost

development:
  database: myapp_development
  <<: *defaults

test:
  database: myapp_test
  <<: *defaults
```
相当于
```
defaults:
  adapter:  postgres
  host:     localhost

development:
  database: myapp_development
  adapter:  postgres
  host:     localhost

test:
  database: myapp_test
  adapter:  postgres
  host:     localhost
```

`&` 表示建立锚点 `<<` 表示合并到当前的数据 `*` 表示引用锚点

例子2:
```
- &showell Steve 
- Clark 
- Brian 
- Oren 
- *showell 
```
转化为JavaScript则为:`[ 'Steve', 'Clark', 'Brian', 'Oren', 'Steve' ]`

+ YAML组织结构
  - 一个 YAML 文件可以由一个或多个文档组成，文档之间使用“---”作为分隔符，且个文档相互独立，互不干扰。如果 YAML 文件只包含一个文档，则“---”分隔符可以省略。

```
---
website:
  name: bianchengbang
  url: www.biancheng.net
---
website: {name: bianchengbang,url: www.biancheng.net}
pets:
  -dog
  -cat
  -pig
---
pets: [dog,cat,pig]
name: "zhangsan \n lisi"
---
name: 'zhangsan \n lisi'
```

[参考网站]
1. [菜鸟](https://www.runoob.com/w3cnote/yaml-intro.html)
2. [C语言中文网](http://c.biancheng.net/spring_boot/yaml.html)