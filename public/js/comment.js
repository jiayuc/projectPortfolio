/*jslint node: true */
"use strict";

$(document).ready(function() {

    //mobile menu toggling
    $('button#reply').click(function() {
        var parent = $(this).closest('.cmmnt');
        console.log('reply clicked', parent.attr('slug'));

        // append hidden data to form
        var data = {
            discussion_id: parent.attr('discussion_id'),
            parent_slug : parent.attr('slug'),
            date: new Date($.now())
        };
        console.log('data: ', data);

        // $(this).closest('form').on('submit', ()=> {
        //     console.log('submitted! Lets reload');
        //     location.reload();
        // });
            
        $(this).closest('form').submit((event) => {
            for (var p in data) {
              if (data.hasOwnProperty(p)) {
                  $('<input />').attr('type', 'hidden')
                      .attr('name', p)
                      .attr('value', data[p])
                      .appendTo(this);
              }
            }
          return true;
        });
        // $.ajax({
        //     type: 'POST',
        //     data: JSON.stringify(data),
        //     contentType: 'application/json',
        //     url: 'http://127.0.0.1:8081/comment/save',                      
        //     success: function(data) {
        //         console.log('success');
        //         console.log(JSON.stringify(data));
        //     }
        // });
    });


//============
    //Contact Page Map Centering
    var hw = $('header').width() + 50;
    var mw = $('#map').width();
    var wh = $(window).height();
    var ww = $(window).width();

    $('#map').css({
        "max-width": mw,
        "height": wh
    });

    if (ww > 1100) {
        $('#map').css({
            "margin-left": hw
        });
    }

    // added onclick for each file
    $(".filename").click(function() {
        var filename = $(this).attr("filename");
        var base_url = window.location.origin;
        location.href = base_url + "/path?filename=" + filename;
        return false;
    });

    //Tooltip
    $("a").mouseover(function() {

        var attr_title = $(this).attr("data-title");

        if (attr_title == undefined || attr_title == "") return false;

        $(this).after('<span class="tooltip"></span>');

        var tooltip = $(".tooltip");
        tooltip.append($(this).data('title'));


        var tipwidth = tooltip.outerWidth();
        var a_width = $(this).width();
        var a_hegiht = $(this).height() + 3 + 4;

        //if the tooltip width is smaller than the a/link/parent width
        if (tipwidth < a_width) {
            tipwidth = a_width;
            $('.tooltip').outerWidth(tipwidth);
        }

        var tipwidth = '-' + (tipwidth - a_width) / 2;
        $('.tooltip').css({
            'left': tipwidth + 'px',
            'bottom': a_hegiht + 'px'
        }).stop().animate({
            opacity: 1
        }, 200);


    });

    $("a").mouseout(function() {
        var tooltip = $(".tooltip");
        tooltip.remove();
    });


});
