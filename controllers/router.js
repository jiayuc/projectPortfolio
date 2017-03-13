/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router();
const project = require('../models/project');
// const app = express();


// About page route
router.get('/about', function (req, res) {
  res.send('About this PAGE in router');
});


router.get('/:name', function (req, res) {
  project.get(req.params.name, function (err, project) {
  	console.log('inspect children node: ', project.fileTree.root.children); // passed from model
    // res.render('projects/project', {project: project});
  	res.render('inner', {
		name: project.name,
		date: project.date,
		version: project.version,
		summary: project.summary,
		fileTree: project.fileTree.root.children
  	});
  });  
});


module.exports = router;
