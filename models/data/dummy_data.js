/*jslint node: true */
"use strict";
// function ObjectId(str) {return str;}
var ObjectId = require('mongodb').ObjectID;
exports.data = [
    {
        "_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "discussion_id" : "Assignment0",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT)",
        "author" : "first ",
        "content" : "first comment",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e1624cbee3385540af9640"),
        "discussion_id" : "Assignment0",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:42:51 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:42:51 GMT-0700 (PDT)",
        "author" : "second",
        "content" : "second la",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e165811a7550571b2f47c9"),
        "discussion_id" : "Assignment0",
        "parent_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:56:31 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:56:31 GMT-0700 (PDT)",
        "author" : "1 1",
        "content" : "1 1",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e165a01a7550571b2f47ca"),
        "discussion_id" : "Assignment0",
        "parent_id" : ObjectId("58e161e139e5cc54eb8c9d89"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:02 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:02 GMT-0700 (PDT)",
        "author" : "second 1 2",
        "content" : "1 2",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e165b41a7550571b2f47cb"),
        "discussion_id" : "Assignment0",
        "parent_id" : ObjectId("58e165811a7550571b2f47c9"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:23 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:23 GMT-0700 (PDT)",
        "author" : "1 1 1",
        "content" : "1 1 1",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "discussion_id" : "Assignment0",
        "parent_id" : null,
        "slug" : "hello",
        "full_slug" : "Sun Apr 02 2017 13:57:37 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:37 GMT-0700 (PDT)",
        "author" : "3",
        "content" : "1",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e165d21a7550571b2f47cd"),
        "discussion_id" : "Assignment0",
        "parent_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:53 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:53 GMT-0700 (PDT)",
        "author" : "3 1 ",
        "content" : "3 1",
        "votes" : 0,
        "children": []
    },
    {
        "_id" : ObjectId("58e1664b1a7550571b2f47ce"),
        "discussion_id" : "Assignment0",
        "parent_id" : ObjectId("58e165c31a7550571b2f47cc"),
        "slug" : "hello/hello",
        "full_slug" : "Sun Apr 02 2017 13:41:03 GMT-0700 (PDT):hello/Sun Apr 02 2017 13:57:53 GMT-0700 (PDT):hello",
        "pubdate" : "Sun Apr 02 2017 13:57:53 GMT-0700 (PDT)",
        "author" : "3 2",
        "content" : "3 2",
        "votes" : 0,
        "children": []
    }
];