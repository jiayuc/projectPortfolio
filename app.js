/*jslint node: true */
"use strict";

var express = require('express'),
 	app = express(),
 	// middleware for logging
 	morgan = require('morgan'),
 	path = require("path");

// config logging
app.use(morgan('dev'));
// set view engine
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

// enable middleware to serve static file
app.use(express.static(path.join(__dirname, 'public')));
app.use(require('./controllers'));

// require('./models/Database.js');
/**
 * Listen on specified port and print related info
 * @param {requestCallback} callback function to print host and port info
 */
var server = app.listen(8081, function () {
   var host = server.address().address;
   var port = server.address().port;
   console.log("Portfolio app listening at http://%s:%s", host, port);
});