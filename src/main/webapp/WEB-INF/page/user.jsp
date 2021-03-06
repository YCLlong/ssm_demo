<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>用户信息</title>
    <link rel="stylesheet" href="css/layui.css">
    <script src="layui.js"></script>
</head>
<body>

<div class="layui-fluid">
    <%--form:form 中的 modelAttribute 和 commandName 属性用来绑定对象的名称，但是发现
    使用 commandName 绑定，需要对象的setter方法--%>
    <form:form modelAttribute="user" cssClass="layui-form">
        <form:input path="gender" cssClass="layui-input"/>
        <form:input path="age"/>
        <form:hidden path="name"/>
    </form:form>
</div>
<script>
  /*  //一般直接写在一个js文件中
    layui.use(['layer', 'form'], function(){
        var layer = layui.layer
            ,form = layui.form;
        layer.msg('Hello World');
    });*/
</script>
</body>
</html>
