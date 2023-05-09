const express = require("express")
const router = express.Router()
const User = require("../models/User")
const bcrypt = require("bcrypt")
const jwt = require("jsonwebtoken")


class CustomError extends Error {
    constructor(message, statusCode) {
        super(message);
        this.name = "CustomError";
        this.statusCode = statusCode;
    }
    }

// create a new user
router.post("/register", async(req, res)=>{
    try {
        const salt = await bcrypt.genSalt(10)
        const hashedPassword = await bcrypt.hash(req.body.password, salt);
        const newUser = new User({
            username: req.body.username,
            email: req.body.email,
            password: hashedPassword
        })
        const user = await newUser.save()
        res.status(201).json(user)
    } catch(err) {
        console.log(err)
        res.status(500).json(err)
    }
})

// login an existing user
router.post("/login", async(req, res)=>{
    try {
        const {email, password} = req.body
        if(!email || !password) {
            throw new CustomError("Please provide both email and password", 403)
        }
        
        const user = await User.findOne({email:email})
        const validPassword = await bcrypt.compare(password, user.password);
        if(!validPassword) {
            return res.status(400).json("Wrong password");
        }
        const id = user._id
        const token = jwt.sign({id, email}, process.env.JWT_SECRET, {
            expiresIn: "1d",
        })
        // this token should be stored in the client as it is used for authentication and authorization in the later requests
        res.status(200).json({msg: "Login successful", token})
    } catch(err) {
        res.status(404).json("User not found")
    }
})


// get a user
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
            userSearching = await User.findById(id)
            if(userSearching) {
                userFromDB = await User.find({_id:req.params.id}).select('username following followers')
                if(userFromDB.length == 1) {
                    res.status(200).json(userFromDB)
                } else {
                    res.status(404).json("User not found")
                }
            } else {
                res.status(404).json("Invalid token")
            }
        } catch (error) {
            res.status(403).json('Not authorized to access this route')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})


// delete a user
router.delete("/delete", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const email = decoded.email
            const id = decoded.id
            const userToDelete = await User.findOneAndDelete({email: email, _id: id})
            if(userToDelete) {
                res.status(200).json("User has been deleted from database")
            } else {
                res.status(404).json('No user with this token')
            }
        } catch (error) {
            res.status(403).json('Not authorized to perform this action')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})


// update a user


//  NOTE : WHILE DEVELOPING FRONT END IF A USER CHANGES EMAIL THEN HE SHOULD RE-LOGIN SHOW THIS NOTE IN GUI AND REDIRECT USER TO LOGIN PAGE
router.put("/update", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const email = decoded.email
            const id = decoded.id
            if(req.body.password) {
                try {
                    const salt = await bcrypt.genSalt(10)
                    req.body.password = await bcrypt.hash(req.body.password, salt)
                } catch(err) {
                    console.log(err)
                    return res.status(500).json(err)
                }
            }
            const userToUpdate = await User.findOneAndUpdate({email: email, _id: id}, req.body, {new : true, runValidators: true})
            if(!userToUpdate) {
                res.status(404).json('No user with this token')
            }
            res.status(200).json("Update done")
        } catch (error) {
            res.status(403).json(error)
        }
    } catch(err) {
        res.status(500).json(err)
    }
})


// follow a user
router.put("/:id/follow", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const id = decoded.id
            userSearching = await User.findById(id)
            if(userSearching) {
                const user = await User.findOne({_id: req.params.id})
                if(!user.followers.includes(userSearching._id)) {
                    await user.updateOne({$push: { followers: userSearching._id}})
                    await userSearching.updateOne({$push: { following: req.params.id}})
                    res.status(200).json("Following now")
                } else {
                    res.status(401).json("You already follow this user")
                }
            } else {
                res.status(404).json("Invalid token")
            }
        } catch (error) {
            res.status(403).json('Not authorized to access this route')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})

// unfollow a user
router.put("/:id/unfollow", async(req, res)=>{
    try {
        const authHeader = req.headers.authorization
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(403).json('No token provided')
        }
        const token = authHeader.split(' ')[1]
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET)
            const id = decoded.id
            userSearching = await User.findById(id)
            if(userSearching) {
                const user = await User.findOne({_id: req.params.id})
                if(user.followers.includes(userSearching._id)) {
                    await user.updateOne({$pull: { followers: userSearching._id}})
                    await userSearching.updateOne({$pull: { following: req.params.id}})
                    res.status(200).json("Unfollowed")
                } else {
                    res.status(401).json("You dont follow this user")
                }
            } else {
                res.status(404).json("Invalid token")
            }
        } catch (error) {
            res.status(403).json('Not authorized to access this route')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})

module.exports = router