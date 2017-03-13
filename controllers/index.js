/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router(),
      path = require("path");

var project = require('../models/project');
// mount the router 
// router.use('/projects', require('./projects.js'));
// Home page route
router.get('/index', function (req, res) {
   console.log("Got a GET request for the jiayu's homepage");
   var cover_pics = [
      'swim.jpg', 'model.jpg', 'view.jpg', 'chess.jpg', 'webCrawler.png', 'webNet.png'
   ];
   
   project.getAll(req.params.id, function (err, projects_info) {
      console.log(projects_info); // passed from model
      res.render('index', {
         rootPath: projects_info.rootPath,
         project_nodes: projects_info.projects,
         tagline : 'hellow',
         cover_pics: cover_pics
      });
  });

router.use('/', require('./router.js'));
   // res.render('../views/index.ejs', {
   // 	projects: titles,
   // 	tagline : 'hellow',
   // });
   // res.sendFile(path.join(__dirname +  '/../views/index.html'));
});


module.exports = router;
