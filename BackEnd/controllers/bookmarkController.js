const Post = require('../models/Post');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const Bookmark = require('../models/Bookmark');
const fs = require('fs');

const addBookmark = async (req, res) => {
    try {
        const postSearching = await Post.findOne({ _id: req.params.postId });
        if (postSearching == null) {
            return res.status(404).json("Post not found, the owner might have deleted it from the database");
        }
        let userBookmarks = await Bookmark.findOne({ userId: req.id });
        const userId = req.id;
        const username = req.userSearching.username;
        if (userBookmarks == null) {
            userBookmarks = new Bookmark({ userId, username });
            await userBookmarks.validate();
            userBookmarks = await userBookmarks.save();
        }
        if (userBookmarks.postId.includes(req.params.postId)) {
            return res.status(200).json("The post is already saved in your bookmarks list");
        } else {
            const afterUpdate = await Bookmark.findOneAndUpdate({ userId: req.id }, { $push: { postId: req.params.postId } }, { new: true, runValidators: true });
            return res.status(200).json(afterUpdate);
        }
    } catch (err) {
        res.status(500).json(err.message);
    }
};

const removeBookmark = async (req, res) => {
    try {
        const postSearching = await Post.findOne({ _id: req.params.postId });
        if (postSearching == null) {
            return res.status(404).json("Post not found, the owner might have deleted it from the database then it also got deleted from your bookmarks");
        }
        let userBookmarks = await Bookmark.findOne({ userId: req.id });
        const userId = req.id;
        const username = req.userSearching.username;
        if (userBookmarks == null) {
            return res.status(404).json("You dont have this post in your bookmarks");
        }
        if (userBookmarks.postId.includes(req.params.postId)) {
            const afterUpdate = await Bookmark.findOneAndUpdate({ userId: req.id }, { $pull: { postId: req.params.postId } }, { new: true, runValidators: true });
            return res.status(200).json(afterUpdate);
        } else {
            return res.status(200).json("The post is not saved in your bookmarks list");
        }
    } catch (err) {
        res.status(500).json(err.message);
    }
};


const getBookmark = async (req, res) => {
    try {
        const userBookmarks = await Bookmark.findOne({ userId: req.id });
        if (userBookmarks == null || userBookmarks.postId.length < 1) {
            return res.status(200).json("No saved posts in bookmark list");
        }
        const posts = userBookmarks.postId;
        const bookmarksObj = await Promise.all(
            posts.map((post) => {
                const singlePost = Post.findById(post);
                return singlePost;
            })
        );
        res.status(200).json(bookmarksObj);
    } catch (err) {
        res.status(500).json(err.message);
    }
};

module.exports = {
    addBookmark,
    removeBookmark,
    getBookmark,
};