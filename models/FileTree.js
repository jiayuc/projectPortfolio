/*jslint node: true */
"use strict";

const FileNode = require('./FileNode');

/**
 * constructor 
 */
const FileTree = function(projectName) {
    this.projectName = projectName;
    this.root = new FileNode(-1, 'dir', projectName);
};

/**
 * add file/dir node to the file tree
 * @param {number} size size of file
 * @param {string} type type of file
 * @param {string} path pathe to file
 */
FileTree.prototype.add = function(size, type, path) {
    const pathArray = path.split('/');

    var cur = this.root;
    for (let i = 1; i < pathArray.length; i++) {
        var target = pathArray.slice(0, i + 1).join('/');
        var children = cur.children;
        // try to find the node
        var found = false;
        for (let j = 0; j < children.length; j++) {
            if (children[j].path == target) {
                cur = children[j];
                found = true;
            }
        }
        if (!found && i != pathArray.length - 1) {
            // handle directory 
            var newNode = new FileNode(-1, 'dir', target);
            cur.children.push(newNode);
            cur = newNode;
        } else if (!found) {
            // handle file
            let newNode = new FileNode(size, type, path);
            cur.children.push(newNode);
        }

    }
};

module.exports = FileTree;
