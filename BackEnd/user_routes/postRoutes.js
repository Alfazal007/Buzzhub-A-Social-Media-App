const router = require("express").Router()
const Post = require("../models/Post")
const jwt = require("jsonwebtoken")
const User = require("../models/User")


// create a new post

// still to add image accetance
router.post("/", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const id = decoded.id
            userMakingPosts = await User.findById(id)
            if(userMakingPosts) {
                const newPost = await Post.create({ userId: id,description: req.body.description, ...req.body });
                console.log(newPost)
                res.status(201).json(newPost)
            } else {
                res.status(404).json("Invalid token")
            }
        } catch (error) {
            res.status(403).json(error)
        }
    } catch(err) {
        res.status(500).json(err)
    }
})


module.exports = router