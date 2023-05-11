const mongoose = require("mongoose")

const UserSchema = new mongoose.Schema(
    {
        username: {
            type: String,
            required: true,
            unique: true,
            max: 20
        },
        password: {
            type: String,
            required: true,
            min: 8,
        },
        email: {
            type: String,
            required: true,
            unique: true,
            trim: true,
            lowercase: true,
            validate: {
                validator: function (email) {
                    return /\S+@\S+\.\S+/.test(email);
                },
                message: 'Please enter a valid email address'
            }
        },
        following: {
            type: Array,
            default: []
        },
        followers: {
            type: Array,
            default: []
        }
    }, { timestamps: true }
)


module.exports = mongoose.model("User", UserSchema)