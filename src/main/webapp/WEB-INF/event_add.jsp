<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="" />
	<meta name="title" content="" />
	<meta name="author" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	<meta name="keywords" content="excursium" />


	<link rel="stylesheet" type="text/css" href="../resources/css/styles.css" />
	<link rel="stylesheet" href="../resources/css/media.css" />
	<link rel="stylesheet" type="text/css" href="../resources/css/footer.css" />
	<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" />

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<link rel="stylesheet" href="../resources/libs/bootstrap-select/css/bootstrap-select.css"/>
	<script type="text/javascript" src="../resources/libs/bootstrap-select/js/bootstrap-select.js"></script>

	<link rel="stylesheet" href="../resources/css/segoe_ui.css" />

	<link rel="stylesheet" href="../resources/libs/bootstrapformhelpers/css/bootstrap-formhelpers.min.css"/>
	<script type="text/javascript" src="../resources/libs/bootstrapformhelpers/js/bootstrap-formhelpers.min.js"></script>

	<title>Добавить экскурсию</title>
</head>
<body style="background-color: #f9f9f9;">
	<style type="text/css">
	
	.navbar-fixed-top {
		min-height: 95px;
	}

	.navbar-nav > li > a {
		padding-top: 0px;
		padding-bottom: 0px;
		line-height: 95px;
	}

	.navbar-custom {
		background-color:#bfd9e3;
		color:#000000;
		border-radius:0;
	}
	
	.navbar-custom .navbar-nav > li > a {
		color:#000000;
	}

	.navbar-custom .navbar-nav > .active > a {
		color: #000000;
		background-color:transparent;
	}
	
	.navbar-custom .navbar-nav > li > a:hover,
	.navbar-custom .navbar-nav > li > a:focus,
	.navbar-custom .navbar-nav > .active > a:hover,
	.navbar-custom .navbar-nav > .active > a:focus,
	.navbar-custom .navbar-nav > .open >a {
		text-decoration: none;
		background-color: #B2EBF2;
	}
	
	.navbar-custom .navbar-brand {
		color:#eeeeee;
	}
	.navbar-custom .navbar-toggle {
		background-color:#eeeeee;
	}
	.navbar-custom .icon-bar {
		background-color:#33aa33;
	}
</style>
<div class="content">
	<nav class="navbar navbar-default navbar-custom" role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle Navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>

				<a class="navbar-brand" href="index.html">
					<img id="brand-image" alt="Website Logo" src="../resources/images/logo.png" />
				</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

				<!-- Содержимое основной части -->
				<ul class="nav navbar-nav navbar-right">
					<li><a href="../download.html">Скачать</a></li>
					<li><a href="../features.html">Функции</a></li>
					<li><a href="#">FAQ</a></li>
					<li><a href="../contact.html">Контакты</a></li>
					<li><a href="../registration.html">Регистрация</a></li>
				</ul>

			</div><!-- navbar collapse end -->
		</div><!-- container-fluid end -->
	</nav>
	<div class="container">
		<div class="page-header">
			<h2>Добавление экскурсии</h2>
		</div>
		<div class="row">
			<div class="col-md-12 col-lg-6">

				<form th:object="${insertEvent}" action="/addeventhttp" method="POST">
					<br/>
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">ФИ</span>
						<input name="time" type="text" class="form-control" placeholder="ФИ" aria-label="ФИ" aria-describedby="basic-addon1"/>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Название</span>
						<input name="name" type="text" class="form-control" placeholder="Название" aria-label="Название" aria-describedby="basic-addon1"/>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Описание</span>
						<input name="description" type="text" class="form-control" placeholder="Описание" aria-label="Описание" aria-describedby="basic-addon1"/>
					</div>
					<br/>
					<div class="panel panel-default">
						<div class="panel-heading">
							Тип экскурсии
						</div>
						<div class="panel-body">
							<div class="input-group">
								<select name="category" class="form-control selectpicker">
									<option value="avto">Авто</option>
									<option value="pesh">Пешеходная</option>
									<option value="kvest">Квест</option>
									<option value="extrime">Экстремальная</option>
									<option value="other">Другое</option>
								</select>
							</div>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Количество участников</span>
						<input name="photo" type="number" class="form-control" placeholder="Количество участников" aria-label="Количество участников" aria-describedby="basic-addon1"/>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Продолжительность</span>
						<input name="duration" type="text" class="form-control" placeholder="Продолжительность" aria-label="Продолжительность" aria-describedby="basic-addon1"/>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Место встречи</span>
						<input name="place" type="text" class="form-control" placeholder="Место встречи" aria-label="Место встречи" aria-describedby="basic-addon1"/>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Цена</span>
						<input name="price" type="number" class="form-control" placeholder="Цена" aria-label="Цена" aria-describedby="basic-addon1"/>
						<span class="input-group-addon" id="basic-addon1">&#8381;</span>
					</div>
					<br />
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Телефон</span>
						<input name="guideId" type="text" class="form-control bfh-phone" placeholder="Телефон" aria-label="Телефон" aria-describedby="basic-addon1" data-format="+7 (ddd) ddd-dddd"/>
					</div>
					<br />

					<div class="input-group">
						<span class="input-group-addon">Доступные интервалы времени</span>
					<select id="done" class="form-control selectpicker" multiple="multiple" data-done-button="true">
						<option value="1">До 9 утра</option>
						<option value="2">С 9 до 12</option>
						<option value="3">С 12 до 15</option>
						<option value="4">С 15 до 18</option>
						<option value="5">С 18 до 21</option>
						<option value="6">С 21 до 24</option>
					</select>
					</div>
					<br />
					<button type="submit" class="btn btn-default">Отправить</button>

				</form>
			</div>
		</div>
	</div>
</div>
<footer id="myFooter">
	<div class="container">
		<div class="row">
			<div class="col-sm-3">
				<h5>Excursium</h5>
				<ul>
					<li><a href="../download.html">Скачать</a></li>
					<li><a href="../features.html">Функции</a></li>
				</ul>
			</div>
			<div class="col-sm-3">
				<h5>Контакты</h5>
				<ul>
					<li><a href="../about.html">О нас</a></li>
					<li><a href="../contact.html">Контакты</a></li>
				</ul>
			</div>
			<div class="col-sm-3">
				<h5>Скачать</h5>
				<ul>
					<li><a href="#">Android</a></li>
				</ul>
			</div>
			<div class="col-sm-3">
				<h5>Помощь</h5>
				<ul>
					<li><a href="#">FAQ</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="second-bar">
		<div class="container">
			<h7 class="logo"><a href="#"> 2017 &copy; Excursium </a></h7>
			<div class="social-icons">
				<a href="#" class="twitter"><i class="fa fa-twitter"></i></a>
				<a href="#" class="facebook"><i class="fa fa-facebook"></i></a>
				<a href="#" class="google"><i class="fa fa-google-plus"></i></a>
			</div>
		</div>
	</div>
</footer>

</body>
</html>