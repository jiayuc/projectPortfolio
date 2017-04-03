/*jslint node: true */
"use strict";

function ObjectId(str) {
    return str; }

/**
 * update vote to database, then update the html view
 * @param  {Object} context   - context of clicked element
 * @param  {Number} change_by - 1 indicates increment by 1, -1 indicates decrement by 1
 */
function update_votes(context, change_by) {
    var vote_display = $(context).closest('div:has("form")')
        .find('button#display');
    console.log('vote display: ', vote_display, '----\n', vote_display.html() );
    // get hidden info for database update use
    var parent = $(context).closest('.cmmnt');

    $.post("http://127.0.0.1:8081/comment/voteUpdate", {
            _id: parent.attr('_id'),
            votes: vote_display.html(),
            add: change_by == 1
        },
        (result) => {
            vote_display[0].innerHTML  = result.votes;
        });
}

$(document).ready(function() {

    $('button#upvoke').click(function() {
        console.log('upvoke clicked');
        update_votes(this, 1);
    });

    $('button#downvoke').click(function() {
        console.log('downvoke clicked');
        update_votes(this, -1);
       
    });

    //mobile menu toggling
    $('button#reply').click(function() {
        console.log('reply clicked');
        var parent = $(this).closest('.cmmnt');
        var container = $(this).closest('div#container');
        console.log('parent: ', parent.get(), 'container', container, '====');
        console.log('discussion_id: ', container.attr('discussion_id'));
        // append hidden data to form
        var data = parent.get().length ? {
            parent_id: parent.attr('comment_id'),
            parent_slug: parent.attr('slug'),
            pubdate: new Date($.now()),
            discussion_id: parent.attr('discussion_id')
        } : {
            parent_id: container.attr('comment_id'),
            parent_slug: null,
            pubdate: new Date($.now()),
            discussion_id: container.attr('discussion_id')
        };


        // $(this).closest('form').on('submit', (e)=> {
        //     e.preventDefault();
        //     console.log('submitted! Lets reload');
        // setTimeout(function() {
        //      window.location.reload();
        // },2);     
        
        $(this).closest('form').submit((e) => {
            console.log('data: ', data);
            for (var p in data) {
                var hasHiddenField = ($(this).find('input [name='+ p +']').length) === 0;
                if (data.hasOwnProperty(p) && hasHiddenField===false) {
                    console.log('===Appending input html', this);
                    $('<input />').attr('type', 'hidden')
                        .attr('name', p)
                        .attr('value', data[p])
                        .appendTo(this);
                }
            }
            e.preventDefault(); // Prevents the page from refreshing
            
            $.post(

                var $this = $(this).closest('form'); // `this` refers to the current form element
                console.log('this is ', this, '$this is: ', $this);
                $this.attr("action"), // Gets the URL to sent the post to
                $this.serialize(), // Serializes form data in standard format
                (data) => { 
                    console.log('reply form got res: ', data); 
                    var cmn_li = $(this).closest('li.cmmnt');
                    if (cmn_li.get().length === 0) {
                        $(this).closest('div#container').find('li.cmmnt').first().append(data.html);
                    } else {
                        cmn_li.append(data.html);
                    }
                },
                "json" // The format the response should be in
            );            
            // return true;
        });
        // });
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


// jQuery(function(){
//       jQuery('button#upvoke')[0].click();
//     });

});
