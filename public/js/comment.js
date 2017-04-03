/*jslint node: true */
"use strict";

function ObjectId(str) {
    return str;
}

/**
 * update vote to database, then update the html view
 * @param  {Object} context   - context of clicked element
 * @param  {Number} change_by - 1 indicates increment by 1, -1 indicates decrement by 1
 */
function update_votes(context, change_by) {
    var vote_display = $(context).closest('div:has("form")')
        .find('button#display');
    console.log('vote display: ', vote_display, '----\n', vote_display.html());
    // get hidden info for database update use
    var parent = $(context).closest('.cmmnt');

    $.post("http://127.0.0.1:8081/comment/voteUpdate", {
            _id: parent.attr('_id'),
            votes: vote_display.html(),
            add: change_by == 1
        }, (result) => {
            vote_display[0].innerHTML = result.votes;
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

    $('button#reply').click(function() {
        console.log('reply clicked', this);
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


        $(this).closest('form').submit((e) => {
            console.log('data: ', data);
            for (var p in data) {
                var hasHiddenField = false; //($(this).find('input [name='+ p +']').length) === 0;
                if (data.hasOwnProperty(p) && hasHiddenField === false) {
                    // console.log('===Appending input html', this);
                    $('<input />').attr('type', 'hidden')
                        .attr('name', p)
                        .attr('value', data[p])
                        .appendTo(this);
                }
            }

            e.preventDefault(); // Prevents the page from refreshing
            var $this = $(this).closest('form');
            // console.log('this: ', this, ' vs. ', $this);
            $.post(
                $this.attr("action"), // Gets the URL to sent the post to
                $this.serialize(), // Serializes form data in standard format
                // his refers to the current form element
                // console.log('this is ', this, '$this is: ', $this);
                (data) => {
                    // console.log('reply form got res: ', data); 
                    var cmn_li = $(this).closest('li.cmmnt');
                    if (cmn_li.get().length === 0) {
                        console.log('First comment');
                        $(this).closest('div#container').find('ul#comments').first().append(data.html);
                    } else {
                        cmn_li.append(data.html);
                    }
                },
                "json" // The format the response should be in
            );
            // return true;
        });

    });

    // added onclick for change sort by
    $("select#sort_by").change(function() {
        var value = '&sort_by=' + this.value;
        var base_url = $(location).attr('href');
        console.log('Change sorting: ', base_url + value);
        location.href = base_url + value;
        return false;
    });

});
