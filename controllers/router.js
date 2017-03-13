/*jslint node: true */
"use strict";

const express = require('express');
const router = express.Router();
const project = require('../models/project');
// const app = express();


// detailed file page
router.get("/path", function (req, res) {
	console.log('Entering filename?', req.query.filename); 
	project.getCommitsForFile(req.query.filename, function (err, revisions) {
  		console.log(revisions); // passed from model
    // res.render('projects/project', {project: project});
	  	res.render('detail', {
			filepath: req.query.filename,
			revisions: revisions
		});
  	});
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
