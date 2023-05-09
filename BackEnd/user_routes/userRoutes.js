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
            const email = decoded.email
            const id = decoded.id
            const userExists = await User.find({email: email, _id: id})
                userFromDB = await User.find({_id:req.params.id})
                if(userFromDB.length == 1) {
                    res.status(200).json(userFromDB)
                } else {
                    res.status(404).json("User not found")
                }
        } catch (error) {
            res.status(403).json('Not authorized to access this route')
        }
    } catch(err) {
        res.status(500).json(err)
    }
})


// delete a user
// update a user




router.get("/", (req, res)=>{
    res.send("This is user routes")
})


module.exports = router