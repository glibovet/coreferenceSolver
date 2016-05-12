$(function () {

    $("body").on('click', function () {
        $('.groupPopup').fadeOut();
        $('.context').fadeOut();
    });

    $(".pCorref").on('click', function (e) {
        e.stopPropagation();
        $(this).find('.groupPopup').fadeIn();
    })


    $(document).on('contextmenu', function (e) {
        $('.context').fadeIn().css({"left": e.clientX, "top": e.clientY});
        return false;

    })

    $('body').mousedown(function (event) {

        if (event.which == 3) {
            event.preventDefault();

            $.post('/explain', {text: window.getSelection().toString()}, function (response) {
                $('.selectionVal').html(response)
            })
        }

    });

})
