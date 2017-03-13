/*jslint node: true */
"use strict";

var express = require('express'),
    router = express.Router(),
    project = require('../models/project');

router.get('/:id', function(req, res) {
  console.log('RECEIVED ID!!!');


  project.get(req.params.id, function (err, project) {
  	console.log(project); // passed from model
    res.render('projects/project', {project: project});
  });
});

module.exports = router;