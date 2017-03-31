/*jslint node: true */
"use strict";

const FileTree = require('./FileTree');
const ProjectNode = require('./ProjectNode');
const RevisionNode = require('./RevisionNode');
const db = require('./Database.js');

const fs = require('fs'),
    xml2js = require('xml2js');

/**
 * Get the svn info for a given file
 * @param  {string}   filename - name of the file to get
 * @param  {Function} cb - callback passed in
 */
exports.get = function(filename, cb) {
    var projects = {};
    var root_path = {};
    parseSvnList('svn_list.xml', projects, root_path);

    cb(null, projects[filename]);
};


/**
 * Get the svn info for all files, store into data structure
 * @param  {string}   project_pathname - name of the path to project
 * @param  {Function} cb - call back function
 */
exports.getAll = function(project_pathname, cb) {
    var projects = {};
    var root_path = {};
    parseSvnList('svn_list.xml', projects, root_path);
    console.log("from parseSvnList: ", projects);
    cb(null, {
        projects: projects,
        root_path: root_path
    });
};


/**
 * Get the svn commits for all files
 * @param  {string}   filename - name of the file to get
 * @param  {Function} cb - callback passed in
 */
exports.getCommitsForFile = function(filePath, cb) {
    var revisions = {};
    parseSvnLog('svn_log.xml', revisions);
    // get related commits
    var revision_arr = getRevisions(filePath, revisions);
    console.log('revision arr: ', revision_arr);

    cb(null, revision_arr);
};


/**
 * read xml into file
 * @param  {[type]} filename - filename of svn file
 * @return {[type]}          - object of svn result 
 */
const readSvnFile = function(filename) {
    const parser = new xml2js.Parser();
    var data = fs.readFileSync(__dirname + '/data/' + filename);
    var res = null;
    parser.parseString(data, function(err, result) { //interesting... callback style, but not async...
        if (err) {
            console.error(err);
            return null;
        }
        res = result;
        console.log('Done reading in %s', filename);
        // writeToFile(filename.substring(0, filename.lastIndexOf(".")) + '.json', result);
    });
    return res;
};

/**
 * write svn list xml to json file on disk
 * @param  {string} filename - name of output file
 * @param  {object} data - json object to write to disk
 */
const writeToFile = function(filename, data) {
    var entries = data.lists.list;

    var json_data = JSON.stringify(entries, null, 4);
    var filepath = __dirname + '/data/' + filename;
    fs.writeFile(filepath, json_data, function(err) {
        if (err) throw err;
        console.log('complete writing to %s', filename);
    });
};

/**
 * parse svn list and obtain data structure to store
 * @param  {[type]} filename [description]
 * @param  {[type]} projects     [description]
 * @param  {[type]} root_path     [description]
 * @return {[type]}              [description]
 */
function parseSvnList(filename, projects, root_path) {

    const listObj = readSvnFile(filename).lists.list;
    root_path.path = listObj[0].$.path;
    const entries_arr = listObj[0].entry;

    for (let i = 0; i < entries_arr.length; i++) {
        var cur = entries_arr[i];
        var name = cur.name[0]; //including path info
        var nameArray = name.split('/');
        var projectName = nameArray[0];
        var date = cur.commit[0].date;
        var author = cur.commit[0].author;

        //for creating tree
        const type = cur.$.kind;
        var size = -1;
        if (type != 'dir') {
            size = cur.size[0];
        }

        //create new project
        if (!projects[projectName]) {
            var version = cur.commit[0].$.revision;
            var fileTree = new FileTree(projectName);
            projects[projectName] = new ProjectNode(name, date, version, author, fileTree); //cannot set commit message currently 
        } else {
            projects[projectName].fileTree.add(size, type, name);
        }
    }
}

/**
 * parse log file to get information
 * @param  {string} filename - filename of the svn log
 * @param  {object} revisions - will update to the revision object 
 */
function parseSvnLog(filename, revisions) {
    const logObj = readSvnFile(filename);
    const revisionArray = logObj.log.logentry;
    for (let i = 0; i < revisionArray.length; i++) {
        let revision = revisionArray[i];
        let number = revision.$.revision;
        let author = revision.author[0];
        let message = revision.msg[0];
        let date = revision.date;
        let revisionNode = new RevisionNode(number, author, message, date);

        let files = revision.paths[0].path;
        for (let j = 0; j < files.length; j++) {
            // remove netid
            let path = files[j]['_'].split('/').slice(2).join('/');
            revisionNode.addFile(path);
        }
        revisions[number] = revisionNode;
    }
}


/**
 * get revisions that contain given filename
 * @param  {string} filename - name of the file to find
 * @param  {object} revisions - dictionary that contains all revisions
 * @return {Array}             array of revision nodes
 */
function getRevisions(filename, revisions) {
    var res = [];
    for (var num in revisions) {
        var revision = revisions[num];
        if (revision.containsFile(filename)) {
            res.push(res.push(revision));
        }
    }
    return res;
}

// run svn shell script
var exec = require('child_process').exec;
var cmd = ''; //'source svn.sh';

exec(cmd, function(error, stdout, stderr) {
    if (error) {
        console.log('Err: ', error);
    } else {
        console.log('Stdout: ', stdout);
    }
});

// insert projects data into mongodb
function insertProjectDataToDB(cb) {
    var projects = {};
    var root_path = {};
    parseSvnList('svn_list.xml', projects, root_path);
    for (var project_name in projects) {
        var project = projects[project_name];
        db.insertProjectToDB(project_name, project.name, project.date, project.author, () => {console.log('in func');});
    }
    cb();
}

// call func to popuilate db
// insertProjectDataToDB(() => {console.log('Inserted project data into mongodb!');} );
