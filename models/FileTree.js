/*jslint node: true */
"use strict";

const FileNode = require('./FileNode');
const util = require('util');

const FileTree = module.exports = function(projectName) {
    this.projectName = projectName;
    this.root = new FileNode(-1, 'dir', projectName, null);
};

FileTree.prototype.add = function(size, type, path) {
    const pathArray = path.split('/');

    if (this.root.path !== pathArray[0]) {
        return console.error("this file(directory) doesn't belong to this project");
    }

    var cur = this.root;
    var parent = this.root.path;
    for (let i = 1; i < pathArray.length; i++) {
        let target = pathArray.slice(0, i + 1).join('/');
        let found = false;
        let children = cur.children;
        //try find the node in the children
        for (let j = 0; j < children.length; j++) {
            if (children[j].path == target) {
                cur = children[j];
                found = true;
            }
        }
        if (!found && i != pathArray.length - 1) { //it's a directory
            let newNode = new FileNode(-1, 'dir', target, parent);
            cur.children.push(newNode);
            cur = newNode;
        } else if (!found) { //it's the file
            let newNode = new FileNode(size, type, path, parent); //path == target at this point
            cur.children.push(newNode);
            //return console.info("successifully added the new file: %s\n", path);
            //for detailed test: return console.info("successifully added the new file %s:\n%s", path, util.inspect(this.root, false, null));
        }
        parent = target;
    }
    //return console.info("%s already exists in the tree", path);
    return null;
};

FileTree.prototype.find = function(path) {
    const pathArray = path.split('/');
    if (this.root.path !== pathArray[0]) {
        return null;
    }
    if (pathArray.length == 1) {
        return this.root;
    }
    var cur = this.root;
    for (let i = 1; i < pathArray.length; i++) {
        let target = pathArray.slice(0, i + 1).join('/');
        let children = cur.children;
        let found = false;
        for (let j = 0; j < children.length; j++) {
            if (children[j].path == target) {
                cur = children[j];
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        if (i == pathArray.length - 1) {
            return cur;
        }
        //cur =children[index];
    }
    return null;
};