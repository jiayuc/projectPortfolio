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
	   db.collection('files').save( {
	      "name":name,
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
	   db.collection('projects').save( {
	      "name":name,
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
	   db.collection('comments').find( {
	      "section_pathname": pathname
	   }).toArray((err, items) => {
	   	// console.log(items);
	   	if (callback)
	   		return callback(items); //!!!
	   	return items;
	   });
	};
	return connectToDB(cb);	
};

function init_comments_from_dummies(dummy_filename) {
	const dummy_data = require(dummy_filename);
	var cb = (err, db) => {
		if (err) throw err;
		// var ret = createCollectionsInDB(db);
	   db.collection('comments').insert( dummy_data.data);
		// insertDocument(db, ()=>{console.log('inserted file');});
	};
	connectToDB(cb);	
}


// init_comments_from_dummies('./data/dummy_data.js');
// getCommentsByPathname("Assignment1/ChessGame/.idea/description.html");
