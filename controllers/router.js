/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router();
const project = require('../models/project');


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


// route to project page
router.get('/:name', function(req, res) {
    console.log('Entering /:name?');

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
