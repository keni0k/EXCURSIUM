function remove_img(el) {
    el.parent().remove();
    var $result = $('#result');
    var $demoImg = $('#demo-img');
    if ($result.children().length < 4)
        $('#add-photo').css('display', 'block');
    var src = $result.find('div:first-child img:first-child').attr('src');
    if ($demoImg.attr('src') !== src)
        $demoImg.attr("src", src);
    if (src==="undefined")
        $demoImg.attr("src", "../../resources/img/no-img.jpg");
}

function sbmt() {
    var type = Number($('input[name=typeOfDates]:checked').val());
    var $radio0 = $('#radio0');
    var $radio1 = $('#radio1');
    var $radio2 = $('#radio2');
    var inp = document.getElementById('active_dates');
    if (type === 0) {
        $radio0.children().each(function (index, elem) {
            for (var childItem in elem.childNodes) {
                var elem1 = elem.childNodes[childItem];
                if (elem1.nodeType === 1 && elem1.className === "my-sp form-control") {
                    //console.log(elem1.value);
                    inp.value += elem1.value;
                    inp.value += '-';
                }
            }
            if (inp.value[inp.value.length - 1] === '-')
                inp.value = inp.value.substring(0, inp.value.length - 1);
            else inp.value += 'null';
            inp.value += ';';
        });
    }
    else if (type === 1) {
        for (var i = 0; i < 7; i++) {
            var el = document.getElementById('1' + i);
            if (el !== null && el.childNodes !== null) {
                for (var childItemT in el.childNodes) {
                    var el1 = el.childNodes[childItemT];
                    if (el1.nodeType === 1 && el1.className === "my-sp form-control") {
                        // console.log(el1.value);
                        inp.value += el1.value;
                        inp.value += ',';
                    }
                }
            }
            if (inp.value[inp.value.length - 1] === ',')
                inp.value = inp.value.substring(0, inp.value.length - 1);
            else inp.value += 'null';
            inp.value += ';';
        }
    }
    else if (type === 2) {
        var $e1 = $('#20');
        $e1.children().each(function (index, elem) {
            if (elem !== null && elem.className === "day-of-week") {
                if (elem.getElementsByClassName("input-group date").length !== 0) {
                    var child3 = elem.getElementsByClassName("input-group date")[0];
                    console.log(child3.className);
                    for (var childItem in child3.childNodes) {
                        var e5 = child3.childNodes[childItem];
                        if (e5.nodeType === 1) {
                            if (e5.className === "form-control") {
                                // console.log("date: " + e5.value);
                                inp.value += e5.value + ':';
                            }
                            if (e5.className === "my-sp form-control") {
                                // console.log("time: " + e5.value);
                                inp.value += e5.value + ';';
                            }
                        }
                    }
                }
            }
        });
        if ($e1.children().length === 0) inp.value += 'null';
    }

    $radio0.remove();
    $radio1.remove();
    $radio2.remove();

    var st = '';
    $('#result').children().each(function (index, elem) {
        var img = elem.getElementsByClassName('img-rounded')[0];
        st += img.src.toString().substring(44, 76) + ';';
    });
    if (st.length > 0)
        st = st.substring(0, st.length - 1);
    $('#photos').val(st);

    inp.value = inp.value.substring(0, inp.value.length - 1);
    //inp.value = inp.value.split("null").join("");
    // console.log("inp.value: " + inp.value);
    $("#upload_input").remove();
    $('#sbmt').click();
}

function generateOptions(select) {
    var st = '<select class="my-sp form-control"' + select + '><option value="null">--:--</option>';
    var hour = 7;
    var zero = '0';
    for (var i = 0; i < 48; i++) {
        if (i % 2 === 0) hour++;
        if (hour === 24) hour = 0;
        var minutes;
        if (i % 2 === 0)
            minutes = '0';
        else minutes = '3';
        if (hour < 10) zero = '0';
        else zero = '';
        st += '<option value="' + i + '">' + zero + hour + ':' + minutes + '0</option>';
    }
    st += '</select>';
    return st;
}

function addDate() {
    $('#20').append('<div class="day-of-week">' +
        '<div class="form-group" style="margin-top: 15px;">' +
        '<div class="input-group date" style="display: flex;">' +
        '<input type="text" class="form-control" style="width: auto;">' +
        '<span class="input-group-addon" style="width: auto;margin-right: 20px;border-left-width: 0;border-radius: 0 4px 4px 0;">' +
        '<span class="glyphicon glyphicon-calendar"></span>' +
        '</span>' +
        generateOptions(' style="border-radius: 4px;width: auto;" name="time"') +
        '</div></div></div>');
    $('.date').datetimepicker({
        locale: 'ru',
        format: 'DD.MM.YYYY',
        minDate: moment(),
        maxDate: moment().add(3, 'months')
    });
}

function addTime(type, dayOfWeek) {
    if (type === 0) {
        $('#' + type + dayOfWeek).append(generateOptions(' name="time-down"') +
            '<span> –</span>' +
            generateOptions(' name="time-up"'));
        $('#' + dayOfWeek).css('display', 'none');
    }
    if (type === 1) {
        $('#' + type + dayOfWeek).append(generateOptions(' name="time"'));
    }
}

function radio(type) {
    if (type === 0) {
        $('#radio0').css('display', 'block');
        $('#radio1').css('display', 'none');
        $('#radio2').css('display', 'none');
    }
    if (type === 1) {
        $('#radio0').css('display', 'none');
        $('#radio1').css('display', 'block');
        $('#radio2').css('display', 'none');
    }
    if (type === 2) {
        $('#radio0').css('display', 'none');
        $('#radio1').css('display', 'none');
        $('#radio2').css('display', 'block');
    }
}

function setimage() { // функция загрузки на сервер
    var iter = $('.upload-img').length;
    var max = 5 - iter;
    if (max <= 0) {
        alert('Максимально количество фотографий = 4!');
        return;
    }
    $input = $("#upload_input");
    var id = [[${inputEvent.getId()}]];
    var $url = '/upload_images';
    var inputFile = document.getElementById('upload_input').files;
    for (var i = 0; i < inputFile.length; i++) {
        var fd = new FormData;
        if (i > max - 1) { // проверка на кол-во
            alert('Максимально количество фотографий = 4!');
            break;
        }
        fd.append('img', $input.prop('files')[i]);
        $.ajax({
            url: $url,
            data: fd,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (data) {
                var final = '<div class="upload-img"><img src="' + data + '" class="img-rounded"/>"' +
                    '"<div class="closer" onclick="remove_img($(this));"></div></div>';
                var $result = $('#result');
                var $demoImg = $('#demo-img');
                if ($result.children().length < 4)
                    $result.append(final); // вставляю миниатюру в #result
                if ($result.children('.upload-img').length >= 4)
                    $('#add-photo').css('display', 'none');
                var src = $result.find('div:first-child img:first-child').attr('src');
                if ($demoImg.attr('src') !== src)
                    $demoImg.attr("src", src);
            },
            error: function (data) {
                alert("Не все фотографии загрузились. Повторите, пожалуйста, попытку.");
            }
        });
    }
}

$(document).ready(function () {
    $("#upload_button").click(function () {$("#upload_input").click();});
    $('.btn-success').click(function () {sbmt();});

    $('.date').datetimepicker({locale: 'ru', format: 'DD.MM.YYYY', minDate: moment(), maxDate: moment().add(3, 'months')});

    $("#upload_input").change(function () {setimage();});

    $('[data-toggle=confirmation]').confirmation({rootSelector: '[data-toggle=confirmation]'});

    var container = document.getElementById("result");
    var sort = Sortable.create(container, {
        animation: 150,
        /*handle: ".tile__title", // Restricts sort start click/touch to the specified element*/
        /*draggable: ".tile", // Specifies which items inside the element should be sortable*/
        onUpdate: function (evt/**Event*/) {
            var src = $('#result').find('div:first-child img:first-child').attr('src');
            var $demoImg = $('#demo-img');
            if ($demoImg.attr('src') !== src)
                $demoImg.attr("src", src);
            // var item = evt.item; // the current dragged HTMLElement
        },
        draggable: ".upload-img"
    });
    $("#description").keyup(function () {
        var txt = $('#description').val();
        if (txt.length < 150) $('#card-text').text(txt);
        else {
            var i;
            for (i = 1; i < 149; i++) {
                var substr = txt.substring(149 - i, 149);
                if (substr.indexOf('.') !== -1 || substr.indexOf('!') !== -1 || substr.indexOf('?') !== -1) break;
            }
            if (i !== 149 && i < 70) $('#card-text').text(txt.substring(0, 149 - i + 1));
            else $('#card-text').text(txt.substring(0, 148) + '…');
        }
    });
    $("#name").keyup(function () {
        var txt = $('#name').val();
        $('#card-title').text(txt);
    });
    $("#price").keyup(function () {
        var txt = $('#price').val();
        $('#card-price').text(txt + "₽");
        if (txt !== '') {
            $('#price-help').css('display', 'block');
            var our_price = (txt / 20 * 3).toFixed();
            $('#our_price').text(our_price);
            $('#guide_price').text(txt - our_price);
        } else $('#price-help').css('display', 'none');
    });
    $("#category").change(function () {
        var txt = $('#category').val();
        var lang = [[${#locale.getLanguage().toString()=='en'?1:0}]];
        var ru = ["Развлечения", "Наука", "История", "Искусство", "Квесты", "Экстрим", "Производство", "Гастрономия"];
        var en = ["Entertainment", "Science", "History", "Art", "Quests", "Extreme", "Manufacture", "Gastronomy"];
        var categories = lang === 0 ? ru[txt] : en[txt];
        $('#card-category').text(categories);
    });
    $("#age_limit").change(function () {
        var age = $("#age_limit").val();
        var src = "../../resources/img/age_limits/" + age + ".png";
        $("#card-age_limit").attr("src", src);
    })
});