/*jslint node: true */
"use strict";

/**
 * Constructor
 * @param {number} size - size of file
 * @param {string} type - type of file
 * @param {string} path - pathe to file
 * @param {string} path - pathe to file
 */
const FileNode = function FileNode(size, type, path) {
    this.path = path; //id
    this.size = size;
    if (path) {
        let pathArray = path.split("/");
        this.type = this.getType(type, pathArray[pathArray.length - 1]);
    } else {
        this.type = null;
    }
    this.children = [];
};


/**
 * Get the file type
 * @param  {string} type - it has value 'dir' if it is a directory
 * @param  {name} string - name of the file 
 */
FileNode.prototype.getType = function(type, name) {
    if (type === "dir") {
        return type;
    }

    var code = ["java", "cpp", "ruby", "class", "js", "html", "css"];
    var data = ["iml", "md", "xml", "docx", "doc"];
    var image = ["png", "jpeg", "jpg", "img"];
    var suffix = name.split(".").slice(-1).pop();

    if (image.indexOf(suffix) !== -1) {
        return "image";

    } else if (code.indexOf(suffix) !== -1) {
        return "code";

    } else if (data.indexOf(suffix) !== -1) {
        return "data";
    }

    return "other type";
};

module.exports = FileNode;
