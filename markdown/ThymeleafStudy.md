##Thymeleaf
渲染 XML/XHTML/HTML5d 内容的模板引擎(与 JSP Velocity FreeMaker 等模板引擎类似,可轻易与 SpringMVC 等 Web 框架集成)[最大特点:不启动 Web 应用也可以直接在浏览器种打开并正确显示模板页面]

+ 特点:
    - 新一代Java模板引擎
    - 支持HTML原型(后缀 '.html'),因此可直接被浏览器打开(此时,未定义的Thymeleaf标签将被忽略,并展示其静态页面效果)
    - 通过Web应用访问时,Thymeleaf将动态地替换静态内容,使页面动态显示
    - 提供 Spring 标准方言以及一个与 SpringMVC 完美集成 的可选模块,可以快速实现表单绑定 属性编辑器 国际化等功能
    - 多方言支持: 提供 Thymeleaf 标准和 Spring 标准两种方言,可直接套用模板实现 JSTL OGNL表达式(必要时可拓展和创建自定义方言)
    - 和SpringBoot完美融合:SpringBoot为Thymeleaf提供默认设置和视图解析器

* 语法规则:
    * 首先在页面 html 标签声明名称空间 (非必须,仅避免编辑器出现html验证错误)
      `<html lang="zh" xmlns:th="http://www.thymeleaf.org">`
    * 标准表达式语法
    * th属性

+ 标准表达式语法
    - 变量表达式 `${...}`
        - 获取对象的属性和方法
            - `${person.lastName}` 获取 person 对象的 lastName 属性
        - 使用内置的基本对象(获取内置对象的属性 调用内置对象的方法)
            - 常用内置基本对象
                - `#ctx` 上下文对象
                - `#vars` 上下文变量
                - `#locale` 上下文语言环境
                - `#request` HttpServletRequest 对象 (仅web)
                - `#response` HttpServletResponse 对象 (仅web)
                - `#session` HttpSession 对象 (仅web)
                - `#servletContext` ServletContext 对象 (仅web)
            - 例子: `${#session.getAttribute('map')}  =  ${session.map}` 获得 session 对象的 map 属性
        - 使用内置工具对象
            - 内置工具对象
                - strings:字符串工具对象 (常用方法:equals equalsIgnoreCase length trim toUpperCase toLowerCase indexOf substring replace startsWith endsWith contains containsIgnoreCase etc.)
                - numbers:数字工具对象 (常用方法:formatDecimal...)
                - bools:布尔工具对象 (常用方法:isTrue isFalse...)
                - arrays:数组工具对象 (常用方法:toArray length isEmpty contains containsAll...)
                - lists/sets:List/Set集合工具对象 (常用方法:toList size isEmpty contains containsAll sort...)
                - maps:Map集合工具对象 (常用方法:size isEmpty containsKey containsValue)
                - dates:日期工具对象 (常用方法:format year month hour createNow...)
            - 例子: `${#strings.equals('asd', name)}` 判断字符串与对象的某个属性是否相等
    - 选择变量表达式 `*{...}`
        - 与变量表达式功能基本一致
        - 例子:
      ```
        <div th:object="${session.user}">
          <p th:text="*{firstName}">firstname</p>
        </div>
      ```
        - 使用 th:object 存储一个对象(临时变量)后,可在其后代(只能在后代)中使用选择变量表达式获取对象中的属性('*'即表示object对象)
    - 链接表达式 `@{...}`
        - 使用情景:
            - 静态资源的引用
            - form表单的请求
        - 请求形式结构
            - 无参请求: `@{/xxx}`
            - 有偿请求: `@{/xxx(k1=v1, k2=v2)}`
        - 例子: `<link href="asserts/css/signin.css" th:href="@{/asserts/css/signin.css}" rel="stylesheet">`
    - 国际化表达式 `#{...}`
        - 消息表达式 `th:text="#{msg}"`
    - 片段引用表达式 `~{...}`
        - 用于模板页面中引用其他的模板片段
        - 语法结构:
            - 推荐: `~{templatename::fragmentname}`
            - 支持: `~{templatename::#id}`
        - 说明:
            - templatename 模板名 根据模板名解析完整路径(`/resources/templates/templatename.html`)
            - fragmentname 片段名 通过th:fragment声明定义代码块(`th:fragment="fragmentname"`)
            - id HTML的id选择器 使用时需加'#' 不支持class选择器
+ th 属性
    - 可直接在HTML标签中使用
    - 常见th属性:
        - th:id 替换HTML的id属性 `<input id="html-id" th:id="thymeleaf-di"/>`
        - th:text 文本替换 转义特殊字符 `<h1 th:text="hello world">hello springboot</h1>`
        - th:utext 文本替换 不转义特殊字符 `<div th:utext="<h1>welcome!</h1>">well!</div>`
        - th:object 在父标签使用(选择对象),子标签使用选择表达式取值[没有 object 选择对象,子标签使用选择表达式和变量表达式的效果是一样的;有选择对象,子标签也可以使用变量表达式的]
      ```
      <div th:object="${session.user}">
          <p th:text="*{firstName}">firstname</p>
      </div>
      ```
        - th:value 替换value属性 `<input th:value="${user.name}"/>`
        - th:with 局部变量赋值运算 `<div th:with="isEvens = ${prodStat.count}%2 == 0" th:text="${isEvens}"></div>` (isEvens 为 boolean)
        - th:style 设置样式 `<div th:style="'color:#F00;font-weight:bold'">text</div>`
        - th:onclick 点击事件 `<td th:onclick="'getInfo()'"></td>`
        - th:each 遍历 支持Iterable Map 数组等
      ```
      <table>
          <tr th:each="m:${session.map}">
              <td:text="${m.getKey()}"></td>
              <td:text="${m.getValue()}"></td>
          </tr>
      </table>
      ```
        - th:if 根据条件判断是否需要展示此标签 `<a th:if="${userId==collect.userId}">`
        - th:unless 和th:if判断相反,满足条件是不显示 `<div th:unless="${m.getKey()=='name'}"></div>`
        - th:switch 与Java的switch case 语句类似;通常与th:case配合使用,根据不同条件展示不同内容
      ```
      <div th:switch="${name}">
          <span th:case="a">SpringBoot</span>
          <span th:case="b">Thymeleaf</span>
      </div>
      ```
        - th:fragment 模板布局(定义一段被引用或包含的模板片段) `<footer th:fragment="footer">内容</footer>`
        - th:insert 布局标签;将th:fragment指定的模板片段插入到标识的标签中 `<div th:insert="commons/bar::footer"></div>`
        - th:replace 布局标签;将th:fragment指定的模板片段替换到标识的标签中 `<div th:replace="commons/bar::footer"></div>`
        - th:selected select选择框选中 (value为true则select框为选中状态)
      ```
      <select>
          <option>---</option>
          <option th:selected="${name=='a'}">
              springboot
          </option>
          <option th:selected="${name=='b'}">
              thymeleaf
          </option>
      </select>
      ```
        - th:src 替换HTML中的src属性 `<img  th:src="@{/asserts/img/bootstrap-solid.svg}" src="asserts/img/bootstrap-solid.svg" />`
        - th:inline 内联属性;三种取值(text none javascript) 在 `<script>` 标签中使用时,js代码中可以获取到后台传递页面的对象
      ```
      <script type="text/javascript" th:inline="javascript">
          var name = /*[[${name}]]*/ 'bianchengbang';
          alert(name)
      </script>
      ```
        - th:action 替换表单提交地址 `<form th:action="@{/user/login}" th:method="post"></form>`
+ Thymeleaf公共页面抽取
    - 公共页面片段(重复代码)[存放在一个独立的页面,再由其他页面根据需要引用]
    - 抽取公共页面
        - 使用 th:fragment 将公共页面抽取出来,存放在 commons.html 中(th:fragment 为片段名称)
    - 引用公共页面
        - 3个引用公共页面属性
            1. th:insert 插入整个片段(不去除母标签)
            2. th:replace 替换整个片段(去除母标签)
            3. th:include 插入片段包含的内容(不去除母标签,去除th:fragment的标签,保留其内容插入)
        - 实现方法:
            - `~{templatename::selector}` 模板名::选择器
            - `~{templatename::fragment}` 模板名::片段名
      > 通常 ~{} 可省略,其行内写法为 [[~{...}]] (可使用转义字符) 或 [(~{...})] (不可使用转义字符)
```
<!--th:insert 片段名引入-->
<div th:insert="commons::fragment-name"></div>
<!--th:insert id 选择器引入-->
<div th:insert="commons::#fragment-id"></div>
------------------------------------------------
<!--th:replace 片段名引入-->
<div th:replace="commons::fragment-name"></div>
<!--th:replace id 选择器引入-->
<div th:replace="commons::#fragment-id"></div>
------------------------------------------------
<!--th:include 片段名引入-->
<div th:include="commons::fragment-name"></div>
<!--th:include id 选择器引入-->
<div th:include="commons::#fragment-id"></div>
```
+ 插入参数
    - 抽取和引入公共页面时,还可以进行参数传递,其步骤:
        1. 传入参数
        2. 使用参数
    - 传入参数
        - 传入参数到被引用的页面片段中的方法:
            - 模板名::选择器名或片段名(参数1=参数值1, 参数2=参数值2)
            - 模板名::选择器名或片段名(参数值1, 参数值2)
          > * 参数较少时,一般使用第二种方式(直接传)
          > * 参数较多时,建议使用第一种方式(明确参数名和参数值)
            - 示例:
          ```
          <!--th:insert 片段名引入-->
          <div th:insert="commons::fragment-name(var1='insert-name',var2='insert-name2')"></div>
          <!--th:insert id 选择器引入-->
          <div th:insert="commons::#fragment-id(var1='insert-id',var2='insert-id2')"></div>
          ------------------------------------------------
          <!--th:replace 片段名引入-->
          <div th:replace="commons::fragment-name(var1='replace-name',var2='replace-name2')"></div>
          <!--th:replace id 选择器引入-->
          <div th:replace="commons::#fragment-id(var1='replace-id',var2='replace-id2')"></div>
          ------------------------------------------------
          <!--th:include 片段名引入-->
          <div th:include="commons::fragment-name(var1='include-name',var2='include-name2')"></div>
          <!--th:include id 选择器引入-->
          <div th:include="commons::#fragment-id(var1='include-id',var2='include-id2')"></div>
          ```
        - ***在引入的页面***
    - 使用参数
        - 声明页面片段时,可在片段中声明并使用参数
        - 示例:
      ```
      <!--使用 var1 和 var2 声明传入的参数，并在该片段中直接使用这些参数 -->
      <div th:fragment="fragment-name(var1,var2)" id="fragment-id">
          <p th:text="'参数1:'+${var1} + '-------------------参数2:' + ${var2}">...</p>
      </div>
      ```
        - ***在声明的页面***
  