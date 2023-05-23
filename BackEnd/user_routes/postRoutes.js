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
router.post('/', upload.single('img'), createPost);

// get a post ---- specific
router.get('/:id', getPost);

// get posts in feed (yours + followings)
router.get('/feeds/all', getFeed);

// delete a post
router.delete('/:id', deletePost);

// update a post
router.put('/:id', upload.single('img'), updatePost);

// endpoint to like a post
router.put('/:id/like', likePost);

// endpoints to dislike a liked post
router.put('/:id/dislike', unlikePost);

module.exports = router;
