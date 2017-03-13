/*jslint node: true */
"use strict";

const ProjectNode = module.exports = function(name, date, version, summary, tree) {
    this.name = name; //id
    this.date = new Date(date);
    this.version = version;
    if (summary) {
        this.summary = '(no commit message)';
    } else {
        this.summary = summary;
    }

    this.fileTree = tree;

};

ProjectNode.prototype.setSummary = function(summary) {
    this.summary = summary;
};