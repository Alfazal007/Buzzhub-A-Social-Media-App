const router = require("express").Router()
const Post = require("../models/Post")
const jwt = require("jsonwebtoken")
const User = require("../models/User")
const multer = require('multer');
const fs = require('fs');


// Set up multer storage engine
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + file.originalname);
    }
});

// Create multer upload middleware
const upload = multer({ storage: storage });


router.post("/", upload.single('img'), async (req, res) => {
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const userId = decoded.id
            if (decoded.exp <= Date.now() / 1000) {
                return res.status(401).json({ error: "Token has expired" });
            }
            const userMakingPosts = await User.findById(userId)
            if (!userMakingPosts) {
                return res.status(404).json("Invalid token")
            }
            const { description } = req.body;
            let img;
            if (req.file) {
                img = fs.readFileSync(req.file.path); // read the image file as a buffer
                fs.unlinkSync(req.file.path); // delete the temporary file
            }
            const newPost = new Post({ userId, description, img })
            const savedPost = await newPost.save()
            res.status(201).json(savedPost)
        } catch (error) {
            res.status(403).json(error.message)
        }
    } catch (err) {
        res.status(500).json(err)
    }
})


// get a post ---- specific
router.get("/:id", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const id = decoded.id
            if (decoded.exp <= Date.now() / 1000) {
                return res.status(401).json({ error: "Token has expired" });
            }
            userSearching = await User.findById(id)
            if(userSearching) {
                postSearching = await Post.findById(req.params.id)
                if(postSearching) {
                    const { userId, description, img, likes } = postSearching
                    const imgBase64 = img ? img.toString('base64') : null
                    return res.status(200).json({ userId, description, img: imgBase64, likes })
                    res.status(200).send(postFound)
                } else {
                    res.status(404).json('Post not found')
                }
            } else {
                res.status(403).json('Create an account to see the post')
            }
        } catch (error) {
            res.status(403).json('Not authorized to access this route')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})

module.exports = router
