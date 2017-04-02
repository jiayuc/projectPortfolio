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


// added router for testing comment
router.get("/comment", function(req, res) {
    db.getCommentsByPathname("Assignment1/ChessGame/.idea/description.html", 
        (comments)=> {
            console.log("comments: ", comments);
            res.render('comment', {
                comments : comments
            });
    });
});

// route to detailed file page
router.get("/path", function(req, res) {
    console.log('Entering filename?', req.query.filename);

    project.getCommitsForFile(req.query.filename, function(err, revisions) {
        res.render('detail', {
            filepath: req.query.filename,
            revisions: revisions
        });
    });
});

// route to detailed file page
router.post("/comment/save", jsonParser, function(req, res) {
    console.log('Entering save: ', req.body, '\nend req body');
    db.insert_comment(req.body, 
         () => { res.send(req.body);});
    // project.getCommitsForFile(req.query.filename, function(err, revisions) {
    //     res.render('detail', {
    //         filepath: req.query.filename,
    //         revisions: revisions
    //     });
    // });
});

router.post("/comment/submit", bodyParser.urlencoded(), function(req, res) {
    console.log(req.body.comment.author);
    console.log('Entering submit: ', req.body, '\nend req body');
    db.insert_comment(req.body, 
         () => { } );
});

// route to project page
router.get('/:name', function(req, res) {
    console.log('Entering /project/:name?');

    project.get(req.params.name, function(err, project) {
        res.render('inner', {
            name: project.name,
            date: project.date,
            size: project.size,
            version: project.version,
            summary: project.summary,

            fileTree: project.fileTree.root.children
        });
    });
});


module.exports = router;
