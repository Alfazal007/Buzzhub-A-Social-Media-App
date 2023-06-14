const router = require('express').Router();
const multer = require('multer');
const { createStory } = require("../controllers/storyController");

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
router.post('/newStory', upload.single('img'), createStory);


module.exports = router;
