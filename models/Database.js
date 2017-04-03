/*jslint node: true */
"use strict";

const LINK_TO_MGLAB = 'mongodb://jiayuc:uiuc@ds143340.mlab.com:43340/portfolio';
var ObjectId = require('mongodb').ObjectID;
// MongoClient.connect('mongodb://jiayuc:uiuc@ds143340.mlab.com:43340/portfolio', (err, db) => {
// 	if (err) throw err;
// 	db.collection('mammals').find().toArray( (err, result) => {
// 		if (err) throw err;
// 		console.log(result);
// 	});
// 	var ret = createCollectionsInDB(db);
// 	insertDocument(db, ()=>{console.log('inserted file');});
// 	console.log('ret: ', ret);
// });

/**
 * connect to mongoLab and perform the callback
 * @param  {Function} callback - callback to pass to mongo lab
 */
function connectToDB(callback) {
    var MongoClient = require('mongodb').MongoClient;
    MongoClient.connect(LINK_TO_MGLAB, callback);
}

exports.insertFileToDB = (name, date, version, summary, callback) => {
    var cb = (err, db) => {
        if (err) throw err;
        // var ret = createCollectionsInDB(db);
        db.collection('files').save({
            "name": name,
            "date": date,
            "version": version,
            "summary": summary,
            "_id": name + date
        }, (err, result) => {
            console.log("Inserted a document into the projects collection.", result);
            if (callback)
                callback();
        });

        // insertDocument(db, ()=>{console.log('inserted file');});
    };
    connectToDB(cb);
};

exports.insertProjectToDB = (name, date, version, author, callback) => {
    var cb = (err, db) => {
        if (err) throw err;
        // var ret = createCollectionsInDB(db);
        db.collection('projects').save({
            "name": name,
            "date": date,
            "version": version,
            "author": author,
            "_id": name + date
        }, (err, result) => {
            console.log("Inserted a document into the projects collection.", result);
            if (callback)
                callback();
        });

        // insertDocument(db, ()=>{console.log('inserted file');});
    };
    connectToDB(cb);
};

/**
 * create empty collections in db
 * @param  {object} db - mongoDB instance
 */
exports.createCollectionInDB = (collection_name) => {
    var cb = (err, db) => {
        if (err) throw err;
        db.createCollection(collection_name);
        db.collection(collection_name).remove({});
    };
    connectToDB(cb);
};

// get comments from db of the given pathname, sort by given order
exports.getCommentsByPathname = (pathname, sort_by, callback) => {
    var cb = (err, db) => {
        if (err) throw err;
        if (sort_by === 'pubdate' ) {
            console.log('In pubdate\n\n');
            db.collection('comments').find({
                "discussion_id": pathname
            }).toArray((err, items) => {
                items = construct_nested_comments(items);
                console.log("items under pathname ", pathname, ": ",  items);
                if (callback)
                    return callback(items); //!!!
            });

        } else if (sort_by =='number_replies') {
            db.collection('comments').find({
                "discussion_id": pathname
            }).sort({ sort_by: 1 }).toArray((err, items) => {
                items = construct_nested_comments(items);
                console.log("items under pathname ", pathname, ": ",  items);
                if (callback)
                    return callback(items); //!!!
            });

        } else { // sort by votes
            db.collection('comments').find({
                "discussion_id": pathname
            }).sort( 'votes', -1 ).toArray((err, items) => {

                console.log("items under pathname ", pathname, ": ",  items);
                if (callback)
                    return callback(items); //!!!
            });

        }
    };
    return connectToDB(cb);
};



// exports.getCommentsByPathname = (pathname, sort_by, callback) => {
//     var cb = (err, db) => {
//         if (err) throw err;
//         // var ret = createCollectionsInDB(db);
//         db.collection('comments').find({
//             "discussion_id": pathname
//         }).sort({ sort_by: 1 }).toArray((err, items) => {
//             items = construct_nested_comments(items);
//             console.log("items under pathname ", pathname, ": ",  items);
//             if (callback)
//                 return callback(items); //!!!
//         });
//     };
//     return connectToDB(cb);
// };




// sort by date
function construct_nested_comments_timer(comments_arr) {
    comments_arr.sort(function(a,b){
        return timewise_smaller(a.pubdate, b.pubdate);
    });
    return comments_arr;
}

function time_parser(datestring1) {
    var monthmap = {
        'Jan':1, 'Feb':2, 'Mar':3, 'Apr':4, 'May':5,
        'Jun':6, 'Jul':7, 'Aug':8, 'Sep':9, 'Oct':10,
        'Nov':11, 'Dec':12
    };

    var split1 = datestring1.split(' ');

    return {year:split1[3], month: monthmap[split1[1]], 
        date: split1[2], time: split1[4]};
}

function timewise_smaller(datestring1, datestring2) {
    var time1 = time_parser(datestring1);
    var time2 = time_parser(datestring2);
    if(time1.year < time2.year || time1.month<time2.month || time1.date<time2.date || time1.time<time2.time){
        return 1;
    }
    else{
        return -1;
    }
}
/**
 * return array of sub-roots and map from id to childrenID
 * @param  {Array} comment_arr - array of raw comments
 * @return {Object}            - object of extracted data info
 */
function preprocess(comment_arr) {
    var roots = [];
    var idToChildrenID = {};
    var idToJson = {};

    comment_arr.forEach((comment) => {
        idToChildrenID[comment._id] = [];
        idToJson[comment._id] = JSON.parse(JSON.stringify(comment)); // deep copy
    });
    // find roots
    comment_arr.forEach((comment) => {
        if (!(comment.parent_id in idToChildrenID)) {
            roots.push(comment._id);
        } else {
            idToChildrenID[comment.parent_id].push(comment._id);
        }
    });
    // console.log('Roots: ', roots, " idToChildrenID: ", idToChildrenID, "idToJson: ", idToJson);
    return { roots: roots, idToChildrenID: idToChildrenID, idToJson: idToJson };
}

/**
 * Construct nested comments from raw data arr from db
 * @param  {Array} comments_arr  - array of comments
 * @return {Array}              - array of nested comments
 */
function construct_nested_comments(comments_arr) {
    console.log('in construct_nested_comments');
    var trees = []; // nested tree to return 
    var ret = preprocess(comments_arr);
    var roots = ret.roots;
    var idToChildrenID = ret.idToChildrenID;
    var idToJson = ret.idToJson;

    function buildtree(root_id) {
        var tree = JSON.parse(JSON.stringify(idToJson[root_id]));
        tree.children = [];
        idToChildrenID[root_id].forEach((children_id) => {
            tree.children.push(buildtree(children_id));
        });
        return tree;
    }

    for (let i = 0; i < roots.length; ++i) {
        var root_id = roots[i];
        trees.push(buildtree(root_id));
    }

    return trees;
}

/**
 * Helper function to populate db with dummy data
 * @param  {String} dummy_filename - filepath to dunmmy file
 */
function init_comments_from_dummies(dummy_filename) {
    const dummy_data = require(dummy_filename);
    var cb = (err, db) => {
        if (err) throw err;
        db.collection('comments').insert(dummy_data.data);
        // insertDocument(db, ()=>{console.log('inserted file');});
    };
    connectToDB(cb);
}

/**
 * insert comment to databse
 * @param  {[type]}   comment_json - in form of
 *  var data = {
       discussion_id: parent.attr('discussion_id'),
       parent_slug: parent.attr('slug'),
       date: new Date($.now()),
       author: 'new writrer',
       content: 'something for now:)'
   };

 * @param  {Function} callback     - to be call after the insert
 */
exports.insert_comment = (comment_json, callback) => {
    console.log("[insert comment]", comment_json);
    var cb = (err, db) => {
        if (err) throw err;

        // generate the unique portions of the slug and full_slug
        var slug_part = generate_pseudorandom_slug();
        var full_slug_part = comment_json.pubdate + ':' + slug_part; //.strftime('%Y.%m.%d.%H.%M.%S') 
        var slug, full_slug, parent_id = null;
        var insert_to_db = () => {
            db.collection('comments')
                .insert({
                    'discussion_id': comment_json.discussion_id,
                    'parent_id': parent_id,
                    'slug': slug,
                    'full_slug': full_slug,
                    'pubdate': comment_json.pubdate,
                    'author': comment_json.comment.author,
                    'content': comment_json.comment.content,
                    'votes': 0
                }, (err, data) => {
                	if (err) throw(err);
                    console.log('insered comment');
                    if (callback) callback(data);
                });
        };

        // load the parent comment (if any)
        if (comment_json.parent_id) {
            db.collection('comments').findOne({
                    "_id": new ObjectId(comment_json.parent_id)
                },
                (err, parent) => {
                    console.log('Found parent', comment_json.parent_id, ' in db?', parent ? parent.content : 'npt found');
                    slug = parent.slug + '/' + slug_part;
                    full_slug = parent.full_slug + '/' + full_slug_part;
                    parent_id = parent._id;
                    insert_to_db();
                }
            );
        } else {
            slug = slug_part;
            full_slug = full_slug_part;
            insert_to_db();
        }


    };
    connectToDB(cb);

};

exports.updateVotes = (comment_info, callback) => {
    console.log("[db: updateVotes]", comment_info);
    var cb = (err, db) => {
        if (err) throw err;
        var change = comment_info.add=='true' ? 1: -1;
        console.log('increment by ', change);
        // retrieve the comment in db
        db.collection('comments').findAndModify(
		    { '_id': new ObjectId(comment_info._id) },
		    [],
		    { $inc: { 'votes':  change } }, 
		    {},
		    (err, result)=> {
		    			  if (err) throw(err);
		    			  console.log('result: ', result);
		                  if (callback) callback({votes: result.value? result.value.votes + change: comment_info.votes}); }
		 );
    };
    connectToDB(cb);

};

function generate_pseudorandom_slug() {
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    var text;

    for( var i=0; i < 15; i++ ) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
}
// init_comments_from_dummies('./data/dummy_data.js');
