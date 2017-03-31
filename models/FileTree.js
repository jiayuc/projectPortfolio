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
    const path_arr = path.split('/');

    var curr = this.root;
    for (let i = 1; i < path_arr.length; i++) {
        var target = path_arr.slice(0, i + 1).join('/');
        var children = curr.children;
        // try to find the node
        var found = false;
        for (let j = 0; j < children.length; j++) {
            if (children[j].path == target) {
                curr = children[j];
                found = true;
            }
        }
        if (!found && i != path_arr.length - 1) {
            // handle directory 
            var newNode = new FileNode(-1, 'dir', target);
            curr.children.push(newNode);
            curr = newNode;
        } else if (!found) {
            // handle file
            let newNode = new FileNode(size, type, path);
            curr.children.push(newNode);
        }

    }
};

module.exports = FileTree;
