/*jslint node: true */
"use strict";

const RevisionNode = module.exports = function(number, author, message, date){
  this.number = number;
  this.author = author;
  this.message = message;
  this.date = new Date(date);
  this.files = [];
}

RevisionNode.prototype.addFile = function(path){
  this.files.push(path);
}

RevisionNode.prototype.containsFile = function(filePath){
  return this.files.indexOf(filePath) !== -1
}