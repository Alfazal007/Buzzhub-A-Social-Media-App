const router = require('express').Router();
const {
  getFeed,
  createPost,
  getPost,
  deletePost,
  updatePost,
  likePost,
  unlikePost,
} = require('../controllers/postController');
const multer = require('multer');
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

// create a new post
router.post('/', upload.single('img'), isLoggedIn, createPost);

// get a post ---- specific
router.get('/:id', isLoggedIn, getPost);

// get posts in feed (yours + followings)
router.get('/feeds/all', isLoggedIn, getFeed);

// delete a post
router.delete('/:id', isLoggedIn, deletePost);

// update a post
router.put('/:id', upload.single('img'), isLoggedIn, updatePost);

// endpoint to like a post
router.put('/:id/like', isLoggedIn, likePost);

// endpoints to dislike a liked post
router.put('/:id/dislike', isLoggedIn, unlikePost);

module.exports = router;
