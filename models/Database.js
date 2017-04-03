/*jslint node: true */
"use strict";

const LINK_TO_MGLAB = 'mongodb://jiayuc:uiuc@ds143340.mlab.com:43340/portfolio';

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

exports.getCommentsByPathname = (pathname, callback) => {
    var cb = (err, db) => {
        if (err) throw err;
        // var ret = createCollectionsInDB(db);
        db.collection('comments').find({
            "discussion_id": pathname
        }).sort({full_slug: 1}).toArray((err, items) => {
            items = construct_nested_comments(items);
            console.log("items: ", items);
            if (callback)
                return callback(items); //!!!
        });
    };
    return connectToDB(cb);
};

/**
* return array of sub-roots and map from id to childrenID
**/
function preprocess(comment_arr) {
    var roots = [];
    var idToChildrenID = {};
    var idToJson = {};
    
    comment_arr.forEach( (comment) => {
       idToChildrenID[comment._id] = [];
       idToJson[comment._id] = JSON.parse(JSON.stringify(comment)); // deep copy
    }); 
    // find roots
    comment_arr.forEach( (comment) => {
       if (!(comment.parent_id in idToChildrenID)) {
           roots.push(comment._id);
       } else {
           idToChildrenID[comment.parent_id].push(comment._id);
       }
    }); 
    console.log('Roots: ', roots, " idToChildrenID: ", idToChildrenID, "idToJson: ", idToJson);
    return {roots: roots, idToChildrenID: idToChildrenID, idToJson: idToJson};
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
          var tree =  JSON.parse(JSON.stringify(idToJson[root_id]));
          tree.children = [];
          console.log('idToChildrenID[', root_id, ']: ', idToChildrenID[root_id]);
          idToChildrenID[root_id].forEach( (children_id) => {
              tree.children.push(buildtree(children_id));
          });
          return tree;
      }

      for (let i=0; i<roots.length; ++i) {
          var root_id = roots[i];
          console.log('Subroot id, lets build root tree: ', root_id);
          trees.push(buildtree(root_id));
      }
    
      return trees;
}

function init_comments_from_dummies(dummy_filename) {
    const dummy_data = require(dummy_filename);
    var cb = (err, db) => {
        if (err) throw err;
        // var ret = createCollectionsInDB(db);
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
                }, () => {
                	console.log('insered comment');
                    if (callback) callback();
                });
        };

        // load the parent comment (if any)
        if (comment_json.parent_slug) {
            db.collection('comments').findOne({
                    'discussion_id': comment_json.discussion_id,
                    'slug': comment_json.parent_slug
                },
                (err, parent) => {
                    console.log('Found parent in db?', parent);
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

function generate_pseudorandom_slug() {
    return 'hello';
}
// init_comments_from_dummies('./data/dummy_data.js');
// getCommentsByPathname("Assignment1/ChessGame/.idea/description.html");
