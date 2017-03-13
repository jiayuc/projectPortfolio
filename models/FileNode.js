/*jslint node: true */
"use strict";

const FileNode = module.exports = function FileNode(size, type, path, parent) {
    this.path = path; //id
    this.size = size;
    if (path) {
        let pathArray = path.split("/");
        this.type = this.interpretType(type, pathArray[pathArray.length - 1]);
    } else {
        this.type = null;
    }
    this.children = [];
    this.parent = parent;

};

FileNode.prototype.isFile = function() {
    return this.type !== 'dir';
};

FileNode.prototype.addChild = function(fileNode) {
    this.children.append(fileNode);
};

FileNode.prototype.getName = function() {
    var pathArray = this.path.split('/');
    return pathArray[pathArray.length - 1];
};

FileNode.prototype.interpretType = function(type, name) {
    if (type === "dir") {
        return type;
    }

    let image = ["png", "jpeg", "jpg", "img"];
    let code = ["java", "cpp", "ruby", "class", "js", "html", "css"];
    let data = ["iml", "md", "xml", "docx", "doc"];
    let pieces = name.split(".");
    let postfix = pieces[pieces.length - 1];

    if (image.indexOf(postfix) !== -1) {
        return "image";
    } else if (code.indexOf(postfix) !== -1) {
        return "code";
    } else if (data.indexOf(postfix) !== -1) {
        return "data";
    }
    return "others";
};