<%--
  Created by IntelliJ IDEA.
  User: Keni0k
  Date: 04.11.2017
  Time: 1:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title><s:message code="titles.events" text="default text"/></title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
    <!--<link rel="stylesheet" type="text/css" href="style.css"/>-->
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>
    <style>
        ul {
            list-style: none; /*убираем маркеры списка*/
            /*убираем отступы*/
            padding-left: 0; /*убираем отступы*/
            /*делаем отступ сверху*/
            margin: 25px 0 0;
        }

        .btn > a {
            text-decoration: none; /*убираем подчеркивание текста ссылок*/
            background: #30A8E6; /*добавляем фон к пункту меню*/
            color: #fff; /*меняем цвет ссылок*/
            padding: 10px; /*добавляем отступ*/
            font-family: arial, serif; /*меняем шрифт*/
            border-radius: 4px; /*добавляем скругление*/
            -moz-transition: all 0.3s 0.01s ease; /*делаем плавный переход*/
            -o-transition: all 0.3s 0.01s ease;
            -webkit-transition: all 0.3s 0.01s ease;
        }

        .btn a:hover {
            background: #1C85BB; /*добавляем эффект при наведении*/
        }

        li {
            float: left; /*Размещаем список горизонтально для реализации меню*/
            margin-right: 5px; /*Добавляем отступ у пунктов меню*/

        }
    </style>
</head>
<body>
<div class="page-header">
    <h1>Контакты</h1>
</div>

<nav class="four">
    <ul>
        <li><a class="btn" href="/"><i class="fa fa-home fa-fw"></i>Главная</a></li>
        <li><a class="btn" href="/#">EVENTS</a></li>
        <li><a class="btn" href="/persons">USERS</a></li>
    </ul>
</nav>
<hr/>
<hr/>
<hr/>
<div class="row">
    <div class="col-md-12 col-lg-6">

        <s:url var="formUrl" value="/addeventhttp"/>
        <sf:form modelAttribute="insertEvent" action="${formUrl}" method="post" enctype="multipart/form-data">

            <fieldset>

                <div>
                    <label><s:message code="words.type"/>: </label>
                    <ul>
                        <c:forEach var="type" items="${allTypes}" varStatus="typeStatus">
                            <li>
                                <sf:radiobutton path="type" value="${type}"/>
                                <label for="subscriptionType${typeStatus.count}">
                                    <s:message code="type.${type}"/>
                                </label>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                    <%--Тип:
                    <select name="type" th:selected="${insertEvent.getType()}">
                        <option value="-3">Не прошел модерацию</option>
                        <option value="-2">На перемодерации</option>
                        <option value="-1">Не активен</option>
                        <option value="1">Активен</option>
                    </select>--%>


                <div>
                    <label for="name"><s:message code="words.name"/>: </label>
                    <sf:input path="name"/>
                </div>

                <div>
                    <label for="guideId">ID <s:message code="words.of_guide"/>: </label>
                    <sf:input path="guideId"/>
                </div>


                    <%--<div>
                        <label for="file"><s:message code="words.photo" /> azure: </label>
                        <sf:input path="file" />
                    </div>--%>
                    <%--Фото azure: <input name="file" accept="image/jpeg,image/png" type="file"/>--%>
                    <%--<br/>--%>

                <div>
                    <label><s:message code="words.category"/>: </label>
                    <ul>
                        <c:forEach var="category" items="${allTypes}" varStatus="typeStatus">
                            <li>
                                <sf:radiobutton path="category" value="${insertEvent.category}"/>
                                <label for="subscriptionType${typeStatus.count}">
                                    <s:message code="category.${category}"/>
                                </label>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div>
                    <label for="place"><s:message code="words.place"/>: </label>
                    <sf:input path="place"/>
                </div>

                <div>
                    <label for="duration"><s:message code="words.place"/>: </label>
                    <sf:input path="duration"/>
                </div>

                <div>
                    <label for="description"><s:message code="words.description"/>: </label>
                    <sf:input path="description"/>
                </div>

                <div>
                    <label for="usersCount"><s:message code="words.usersCount"/>: </label>
                    <sf:input path="usersCount"/>
                </div>

                <div>
                    <label><s:message code="words.language"/>: </label>
                    <ul>
                        <c:forEach var="language" items="${allTypes}" varStatus="typeStatus">
                            <li>
                                <sf:radiobutton path="language" value="${insertEvent.language}"/>
                                <label for="subscriptionType${typeStatus.count}">
                                    <s:message code="language.${language}"/>
                                </label>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div>
                    <label for="price"><s:message code="words.price"/>: </label>
                    <sf:input path="price"/>
                </div>

                <button type="submit" class="btn btn-default">Отправить</button>
            </fieldset>
        </sf:form>
    </div>
</div>
<hr/>
<div>
    <label for="del">ID элемента</label>
    <input type="text" id="del" value=""/>
</div>
<script type='text/javascript'>
    function a() {
        var d = document.getElementById('del').value;
        window.location = '/deleteevent?id=' + d;
    }
</script>
<input type="button" onclick="javascript:a()" value="Удалить"/>
<hr/>

<div class="row">
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Users</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>ID</td>
                <td>Название</td>
                <td>Категория</td>
                <td>ID гида</td>
                <td>Стоимость</td>
                <td>Место</td>
                <td>Язык</td>
                <td>Кол-во человек</td>
                <td>Продолжительность</td>
                <td>Описание</td>
                <td></td>
                <td></td>
            </tr>
            <c:forEach var="event" items="${events}">
                <tr>
                    <td><c:out value="${event.id}"/></td>
                    <td th:text="${event.name}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.categoryString}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.guideId}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.price}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.place}"><c:out value="${event.id}"/>t</td>
                    <td th:text="${event.languageString}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.usersCount}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.duration}"><c:out value="${event.id}"/></td>
                    <td th:text="${event.description}"><c:out value="${event.id}"/></td>
                        <%--<td><a th:href="@{/events(id = ${event.id})}"><img src="../../resources/img/icons/edit.png"/></a>
                        </td>
                        <td><a th:href="@{/deleteevent(id = ${event.id})}"><img
                                src="../../resources/img/icons/del.png"/></a></td>--%>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>

<!DOCTYPE HTML>

