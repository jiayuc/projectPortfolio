/*jslint node: true */
"use strict";

exports.data = [
	{
	    _id: 0,
	    children: [{
	        _id: 1,
	        children: [{
	            _id: 5,
	            children: [],
	            author: "Two level nested Replier",
	            pubdate: "some date5",
	            mediaSrc: null,
	            content: "okok",
	            votes: -3,
	            section_pathname: "Assignment1/ChessGame/.idea/description.html"
	        }],
	        author: "replier1",
	        pubdate: "some date2",
	        mediaSrc: "https://www.mememaker.net/static/images/memes/4457123.jpg",
	        content: "I agree it is a great start :)",
	        votes: 3,
	        section_pathname: "Assignment1/ChessGame/.idea/description.html"

	    }, 
	    {
	        _id: 2,
	        children: [],
	        author: "replier2",
	        pubdate: "some date3",
	        mediaSrc: null,
	        content: "Ahhh I miss spring break tho..Sadness.",
	        votes: 4,
	        section_pathname: "Assignment1/ChessGame/.idea/description.html"
	    }],
	    author: "jiayuc",
	    pubdate: "some date1",
	    mediaSrc: null,
	    content: "very first comment: cheers!",
	    votes: 14,
	    section_pathname: "Assignment1/ChessGame/.idea/description.html"
	}, 

	{
	    _id: 4,
	    children: [],
	    author: "Nested Replier",
	    pubdate: "some date4",
	    mediaSrc: null,
	    content: "I agree with you",
	    votes: 1,
	    section_pathname: "Assignment1/ChessGame/.idea/description.html"
	}
	
];