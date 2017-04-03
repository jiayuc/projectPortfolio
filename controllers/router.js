/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router();
const project = require('../models/project');
const db = require("../models/Database.js");
// middleware to help parse
var bodyParser = require('body-parser');
// create application/json parser
var jsonParser = bodyParser.json();

// prevet favicon 
router.get('/favicon.ico', function(req, res) {
    res.send(204);
});

// added router for testing comment
router.get("/comment", function(req, res) {
    db.getCommentsByPathname("Assignment1/ChessGame/.idea/description.html",
        (comments) => {
            console.log("comments: ", comments);
            res.render('comment', {
                comments: comments
            });
        });
});


// route to detailed file page
router.post("/comment/save", jsonParser, function(req, res) {
    console.log('Entering save: ', req.body, '\nend req body');
    db.insert_comment(req.body,
        () => {
            console.log('comment inserted');
            // res.send(req.body);
        });
});

router.post("/comment/tmp", jsonParser, function(req, res) {
    console.log('Entering tmp: ', req.body, '\nend req body');
});

// handle post for submit new comment
router.post("/comment/submit", bodyParser.urlencoded(), function(req, res) {
    console.log('Entering submit: ', req.body, '\nend req body');
    db.insert_comment(req.body,
        (comment) => {
            console.log("new comment: ", comment);

            var fs = require('fs');
            var templateString = fs.readFileSync(require('path').join(__dirname, '../views') + '/comment_single.ejs', 'utf-8');
            res.send({ html: require('ejs').render(templateString, { comment: comment.ops[0] }) });
        });
});

// update comment votes 
router.post("/comment/voteUpdate", bodyParser.urlencoded(), function(req, res) {
    console.log('Entering voteUpdate: ', req.body, '\nend req body');
    db.updateVotes(req.body,
        (new_votes) => { res.send(new_votes); }
    );

});
// route to detailed file page
router.get("/path", function(req, res) {
    var str = req.query.filename;
    var delim = '/sort_by=';
    var lastIndex = str.lastIndexOf(delim);
    var filename = str.substr(0, lastIndex);
    var sort_by = 'pubdate'; // default
    if (lastIndex != -1) {
        sort_by = str.substr(lastIndex);
        sort_by = sort_by.substr(sort_by.lastIndexOf('=')+1);
    }


    console.log('Entering filename?', filename, 'sort_by: ', sort_by);

    project.getCommitsForFile(filename, function(err, commits) {
        db.getCommentsByPathname(filename, sort_by,
            (comments) => {
                console.log(filename, comments);
                res.render('detail', {
                    filepath: filename,
                    revisions: commits,

                    name: filename,
                    comments: comments
                });
            }); // end db

    });
});


// route to project page
router.get('/:name', function(req, res) {
    var str = req.params.name;
    var delim = '/sort_by=';
    var lastIndex = str.lastIndexOf(delim);
    var filename = str;
    var sort_by = 'pubdate'; // default
    if (lastIndex != -1) {
        filename = str.substr(0, lastIndex);
        sort_by = str.substr(lastIndex);
        sort_by = sort_by.substr(sort_by.lastIndexOf('=')+1);
    }

    console.log('Entering /project/:name?', filename, sort_by);

    project.get(filename, function(err, commits) {
        db.getCommentsByPathname(commits.name, 'votes',
            (comments) => {
                res.render('inner', {
                    name: commits.name,
                    date: commits.date,
                    size: commits.size,
                    version: commits.version,
                    summary: commits.summary,

                    fileTree: commits.fileTree.root.children,
                    comments: comments,
                    comment_sort_by: 'votes'
                });
            }); // end db

    });
});


module.exports = router;
