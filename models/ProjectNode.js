/*jslint node: true */
"use strict";

/**
 * Constructor
 * @param {number} name - name of file
 * @param {number} size - size of file
 * @param {string} date - date of file
 * @param {string} version - version of file
 * @param {string} author - author of file
 * @param {string} tree - the file tree under this project
 */
const ProjectNode = function(name, date, version, author, tree) {
    this.name = name;
    this.date = new Date(date);
    this.version = version;
    this.author = author;

    this.fileTree = tree;
};

module.exports = ProjectNode;
