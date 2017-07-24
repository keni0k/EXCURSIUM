<?php
if(isset($_POST['submit'])) {
    echo "Заголовок новости: ".$_POST['head_news']."<br>";
    echo "Турпортал: ".$_POST['locate']."<br>";
    echo "Текст новости: ".$_POST['text_news']."<br><br><br>";
}
?>