/*jslint node: true */
"use strict";

function ObjectId(str) {return str;}
var data = [
    {
        "_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT)",
        "author" : "first ",
        "content" : "first comment",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e1624cbee3385540af9640"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:42:51 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:42:51 GMT-0700 (PDT)",
        "author" : "second",
        "content" : "second la",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e165811a7550571b2f47c9"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:56:31 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:56:31 GMT-0700 (PDT)",
        "author" : "1 1",
        "content" : "1 1",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e165a01a7550571b2f47ca"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:02 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:02 GMT-0700 (PDT)",
        "author" : "second 1 2",
        "content" : "1 2",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e165b41a7550571b2f47cb"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : ObjectId("58e165811a7550571b2f47c9"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:23 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:23 GMT-0700 (PDT)",
        "author" : "1 1 1",
        "content" : "1 1 1",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:57:37 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:37 GMT-0700 (PDT)",
        "author" : "3",
        "content" : "1",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e165d21a7550571b2f47cd"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:53 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:53 GMT-0700 (PDT)",
        "author" : "3 1 ",
        "content" : "3 1",
        "votes" : 0
    },
    {
        "_id" : ObjectId("58e1664b1a7550571b2f47ce"),
        "discussion_id" : "Assignment1/ChessGame/.idea/description.html",
        "parent_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:53 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:53 GMT-0700 (PDT)",
        "author" : "3 2",
        "content" : "3 2",
        "votes" : 0
    }
];

/**
* return array of sub-roots and map from id to childrenID
**/
function preprocess(comment_arr) {
    var roots = [];
    var idToChildrenID = {};
    var idToJson = {};
    
    comment_arr.forEach( (comment) => {
       idToChildrenID[comment._id] = [];
       idToJson[comment._id] = jQuery.extend(true, {}, comment); // deep copy
    }); 
    // find roots
    comment_arr.forEach( (comment) => {
       if (!(comment.parent_id in idToChildrenID)) {
           roots.push(comment._id);
       } else {
           idToChildrenID[comment.parent_id].push(comment._id);
       }
    }); 
    console.log('Roots: ', roots, " idToChildrenID: ", idToChildrenID, "idToJson: ", idToJson);
    return {roots: roots, idToChildrenID: idToChildrenID, idToJson: idToJson};
}

/**
 * Construct nested comments from raw data arr from db
 * @param  {Array} comments_arr  - array of comments
 * @return {Array}              - array of nested comments
 */
function construct_nested_comments(comments_arr) {
    console.log('in construct_nested_comments');
      var trees = []; // nested tree to return 
      var ret = preprocess(comments_arr);
      var roots = ret.roots;
      var idToChildrenID = ret.idToChildrenID;
      var idToJson = ret.idToJson;

    function buildtree(root_id) {
          var tree =  jQuery.extend(true, {}, idToJson[root_id]);
          tree.children = [];
          console.log('idToChildrenID[', root_id, ']: ', idToChildrenID[root_id]);
          idToChildrenID[root_id].forEach( (children_id) => {
              tree.children.push(buildtree(children_id));
          });
          return tree;
      }

      for (let i=0; i<roots.length; ++i) {
          var root_id = roots[i];
          console.log('Subroot id, lets build root tree: ', root_id);
          trees.push(buildtree(root_id));
      }
    
      return trees;
}

$(document).ready(function() {

    //mobile menu toggling
    $('button#upvoke').click(function() {
        var trees = construct_nested_comments(data);
        console.log('upvoke clicked', trees);
    // });
        $.ajax({
            type: 'POST',
            data: JSON.stringify(trees),
            contentType: 'application/json',
            url: 'http://127.0.0.1:8081/comment/tmp',                      
            success: function(data) {
                console.log('success');
                // console.log(JSON.stringify(data));
            }
        });
    });

    //mobile menu toggling
    $('button#reply').click(function() {
        console.log('reply clicked');
        var parent = $(this).closest('.cmmnt');
        var container = $(this).closest('#container');
        console.log('container', container, '====');
        console.log(container.attr('discussion_id'));
        // append hidden data to form
        var data = parent.get().length ? {
            discussion_id: parent.attr('discussion_id'),
            parent_slug : parent.attr('slug'),
            pubdate: new Date($.now())
        } : { 
            discussion_id: container.attr('discussion_id'),
            parent_slug : null,
            pubdate: new Date($.now())
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
              if (data.hasOwnProperty(p)) {
                  $('<input />').attr('type', 'hidden')
                      .attr('name', p)
                      .attr('value', data[p])
                      .appendTo(this);
              }
            }
          return true;
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


});
