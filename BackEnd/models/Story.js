const mongoose = require("mongoose");
const User = require("./User");

const StorySchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
        required: true,
    },
    img: {
        type: Buffer,
    },
    createdAt: {
        type: Date,
        default: Date.now,
    },
});
StorySchema.index({ createdAt: 1 }, { expireAfterSeconds: 120 }); // Set TTL index on createdAt field with 2 minutes expiration


const Story = mongoose.model("Story", StorySchema);
module.exports = Story;