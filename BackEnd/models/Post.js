const mongoose = require("mongoose");
const User = require("./User");


const PostSchema = new mongoose.Schema(
    {
        userId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: true,
        },
        description: {
            type: String,
            required: true,
            maxlength: 500
        },
        img: {
            type: Buffer,
            default: null
        },
        likes: {
            type: Array,
            default: []
        },
        username: {
            type: String,
            ref: 'User',
            required: true
        },
    }, { timestamps: true }
);


module.exports = mongoose.model("Post", PostSchema);