const router = require("express").Router()
const Post = require("../models/Post")



router.get("/", async(req, res)=>{
    res.send("This is post routes")
})

module.exports = router