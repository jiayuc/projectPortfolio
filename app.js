/*jslint node: true */
"use strict";
var express = require('express'),
 	app = express(),
 	// middleware for logging
 	morgan = require('morgan'),
 	path = require("path");

// config logging
app.use(morgan('dev'));
app.set('views', __dirname + '/views');
// app.set('view engine', 'html');
app.set('view engine', 'ejs');

// enable middleware to serve static file
app.use(express.static(path.join(__dirname, 'public')));
// app.use(bodyParser.json())
// app.use(bodyParser.urlencoded({extended: true}))
app.use(require('./controllers'));

/**
 * Listen on specified port and print related info
 * @param {requestCallback} callback function to print host and port info
 */
var server = app.listen(8080, function () {
   var host = server.address().address;
   var port = server.address().port;
   console.log("Portfolio app listening at http://%s:%s", host, port);
});