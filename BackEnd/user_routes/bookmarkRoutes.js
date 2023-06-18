const express = require('express');
const router = express.Router();
const { addBookmark, removeBookmark, getBookmark } = require("../controllers/bookmarkController");


const isLoggedIn = require('../middleware/isLoggedIn');


router.get('/', isLoggedIn, getBookmark);
router.put('/add/:postId', isLoggedIn, addBookmark);
router.put('/remove/:postId', isLoggedIn, removeBookmark);




module.exports = router;
