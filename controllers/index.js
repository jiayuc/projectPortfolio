/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router();
var project = require('../models/project');

// Home page route
router.get('/index', function(req, res) {
    console.log("Got a GET request for the jiayu's homepage");
    var cover_pics = [
        'swim.jpg', 'model.jpg', 'view.jpg', 'chess.jpg', 'webCrawler.png', 'webNet.png'
    ];

    project.getAll(req.params.id, function(err, projects_info) {
        console.log(projects_info); // passed from model
        res.render('index', {
            rootPath: projects_info.rootPath,
            project_nodes: projects_info.projects,
            tagline: 'hellow',
            cover_pics: cover_pics
        });
    });

    router.use('/', require('./router.js'));

});


module.exports = router;
