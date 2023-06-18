const mongoose = require("mongoose");
const User = require("./User");
const Post = require("./Post");

const BookmarkSchema = new mongoose.Schema(
    {
        userId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: true,
        },
        username: {
            type: String,
            required: true,
        },
        postId: {
            type: Array,
            default: []
        },
    }, { timestamps: true }
);


module.exports = mongoose.model("Bookmark", BookmarkSchema);