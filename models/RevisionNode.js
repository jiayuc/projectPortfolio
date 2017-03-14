/*jslint node: true */
"use strict";

/**
 * constructor
 * @param  {number} number - version number
 * @param  {[type]} author  - user who performed the revision
 * @param  {[type]} message - commit message
 * @param  {[type]} date - date of this revision
 */
const RevisionNode = function(number, author, message, date) {
    this.number = number;
    this.author = author;
    this.message = message;
    this.date = new Date(date);
    this.files = [];
};

/**
 * add file to revision node
 * @param {string} path of the new file
 */
RevisionNode.prototype.addFile = function(path) {
    this.files.push(path);
};

/**
 * determine whether has the given file in this revision
 * @param  {string} filePath - filepath of the file to find
 * @return {bool} whether this revision contains the given filepath
 */
RevisionNode.prototype.containsFile = function(filePath) {
    return this.files.indexOf(filePath) !== -1;
};

module.exports = RevisionNode;
