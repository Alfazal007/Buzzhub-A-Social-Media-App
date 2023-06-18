const router = require('express').Router();
const multer = require('multer');
const { createStory, getAllStories, getSpecificStory, deleteSpecificStory } = require("../controllers/storyController");
const isLoggedIn = require('../middleware/isLoggedIn');

// Set up multer storage engine
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + file.originalname);
    },
});

// Create multer upload middleware
const upload = multer({ storage: storage });

// create a new story
router.post('/newStory', upload.single('img'), isLoggedIn, createStory);

router.get('/getStories', isLoggedIn, getAllStories);

router.get('/get-single-story/:id', isLoggedIn, getSpecificStory);
router.delete('/delete/:id', isLoggedIn, deleteSpecificStory);

module.exports = router;
