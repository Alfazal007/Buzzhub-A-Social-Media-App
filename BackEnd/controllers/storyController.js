const Story = require('../models/Story');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const fs = require('fs');


const createStory = async (req, res) => {
    const userId = req.id;
    const username = req.userSearching.username;
    const profilePic = req.userSearching.img;
    try {
        let img;
        if (req.file) {
            img = fs.readFileSync(req.file.path); // read the image file as a buffer
            fs.unlinkSync(req.file.path); // delete the temporary file
            const newStory = new Story({ userId, img, username, profilePic });
            await newStory.validate();
            const savedStory = await newStory.save();
            res.status(201).json(savedStory);
        } else {
            return res.status(401).json("Please select an image to post");
        }
    } catch (error) {
        res.status(500).json(error.message);
    }
};

const getAllStories = async (req, res) => {
    try {
        const userStory = await Story.find({ userId: req.id }).select('userId username profilePic');
        const friendStories = await Promise.all(
            req.userSearching.following.map((friendId) => {
                return Story.find({ userId: friendId }).select('userId username profilePic');
            })
        );
        const allStories = userStory.concat(...friendStories);
        res.status(200).json(allStories);
    } catch (err) {
        res.status(500).json(err.message);
    }
};

const getSpecificStory = async (req, res) => {
    try {
        const specificStory = await Story.find({ userId: req.params.id }).select('_id userId username img');
        if (specificStory == null) {
            return res.status(404).json("Story not found");
        }
        res.status(200).json(specificStory);
    } catch (err) {
        res.status(500).json(err.message);
    }
};
module.exports = {
    createStory,
    getAllStories,
    getSpecificStory,
};