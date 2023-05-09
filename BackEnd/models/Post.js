const mongoose = require("mongoose")
const User = require("./User")


const PostSchema = new mongoose.Schema(
    {
        userId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: true
        },
        description: {
            type: String,
            require: true,
            max: 500
        },
        img: {
            type: String
        },
        likes: {
            type: Array,
            default: []
        },
    }, { timestamps: true }
)


module.exports = mongoose.model("Post", PostSchema)