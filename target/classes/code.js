$(function () {

    $("body").on('click', function () {
        $('.groupPopup').fadeOut();
        $('.context').fadeOut();
    });

    $(".pCorref").on('click', function (e) {
        e.stopPropagation();
        $(this).find('.groupPopup').fadeIn();
    })


    $('.Wrapper').on('contextmenu', function (e) {
        var selection = window.getSelection().toString();
        if (selection !== '') {
            $('.context').fadeIn().css({"left": e.clientX, "top": e.clientY});
            return false;
        }
    })

    $('.Wrapper').mousedown(function (event) {

        if (event.which == 3) {
            event.preventDefault();

            $.post('/explain', {text: window.getSelection().toString()}, function (response) {
                $('.selectionVal').html(response)
            })
        }
    });


    $('.op-tfidf').on('click', function (e) {

        if (!$(this).hasClass("btn-on")) {
            $.each($('.term'), function (i, e) {
                var percent = $(e).data('tfidf') / maxTfIdf;
                $(e).css({'background-color': 'rgba(127,191,63,' + percent + ')'});
            });
            $(this).addClass("btn-on");
        } else {
            $.each($('.term'), function (i, e) {
                var percent = $(e).data('tfidf') / maxTfIdf;
                $(e).css({'background-color': ''});
            });
            $(this).removeClass("btn-on");
        }
    })


    $('.op-nps').on('click', function (e) {
        if (!$(this).hasClass("btn-on")) {
            $.each($('.group[data-group="noun"]'), function (i, e) {
                $(e).addClass("pCorref");
            });
            $(this).addClass("btn-on");
        } else {
            $.each($('.group[data-group="noun"]'), function (i, e) {
                $(e).removeClass("pCorref");
            });
            $(this).removeClass("btn-on");
        }
    })


    $('.op-verbs').on('click', function (e) {
        if (!$(this).hasClass("btn-on")) {
            $.each($('.group[data-group="verb"]'), function (i, e) {
                $(e).addClass("pCorref");
            });
            $(this).addClass("btn-on");
        } else {
            $.each($('.group[data-group="verb"]'), function (i, e) {
                $(e).removeClass("pCorref");
            });
            $(this).removeClass("btn-on");
        }
    })




})
