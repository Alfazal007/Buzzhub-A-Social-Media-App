const Story = require('../models/Story');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const fs = require('fs');


const createStory = async (req, res) => {
    try {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided');
        }
        const token = authHeader.split(' ')[1];
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET);
            const userId = decoded.id;
            if (decoded.exp <= Date.now() / 1000) {
                return res.status(401).json({ error: 'Token has expired' });
            }
            const userMakingPosts = await User.findById(userId);
            if (!userMakingPosts) {
                return res.status(404).json('Invalid token');
            }
            let img;
            if (req.file) {
                img = fs.readFileSync(req.file.path); // read the image file as a buffer
                fs.unlinkSync(req.file.path); // delete the temporary file
                const newStory = new Story({ userId, img });
                await newStory.validate();
                const savedStory = await newStory.save();
                res.status(201).json(savedStory);
            } else {
                return res.status(401).json("Please select an image to post");
            }
        } catch (error) {
            res.status(403).json(error.message);
        }
    } catch (err) {
        res.status(500).json(err);
    }
};

const getStory = async (req, res) => {
    try {

    } catch (err) {
        res.status(500).json("There was an error getting the posts", err);
    }
};
module.exports = {
    createStory
};