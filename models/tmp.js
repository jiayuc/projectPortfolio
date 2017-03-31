/*jslint node: true */
"use strict";

const db = require('./Database.js');
db.insertFileToDB("fsdf", "awe", "werw", "summary", () => {console.log('in func');});