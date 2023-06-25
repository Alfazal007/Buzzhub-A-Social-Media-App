const router = require('express').Router();
const {
  getFeed,
  createPost,
  getPost,
  deletePost,
  updatePost,
  likePost,
  unlikePost,
  getMyPosts,
} = require('../controllers/postController');
const multer = require('multer');
const sharp = require('sharp');
const fs = require('fs');
const util = require('util');
const stat = util.promisify(fs.stat);
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
const compressImageMiddleware = async (req, res, next) => {
  if (!req.file) {
    return next(); // Skip middleware if no file was uploaded
  }

  try {
    // Compress the image using sharp
    const compressedFilePath = req.file.path.replace(
      /(\.[\w\d_-]+)$/i,
      '-compressed$1'
    );

    await sharp(req.file.path)
      .jpeg({ quality: 80 }) // Set the desired JPEG quality (80% in this example)
      .toFile(compressedFilePath); // Overwrite the original file with the compressed version
    // Update req.file with the compressed file details
    fs.unlinkSync(req.file.path);
    req.file.path = compressedFilePath;
    req.file.size = (await stat(compressedFilePath)).size;
    // Get the file size using synchronous method
    const compressedFileStats = fs.statSync(compressedFilePath);
    const compressedFileSizeInKB = Math.ceil(compressedFileStats.size / 1024);

    console.log('Compressed image size:', compressedFileSizeInKB, 'KB');

    next();
  } catch (error) {
    return res.status(500).json(error.message);
  }
};

// create a new post
router.post(
  '/post',
  upload.single('img'),
  isLoggedIn,
  compressImageMiddleware,
  createPost
);

// get a post ---- specific
router.get('/:id', isLoggedIn, getPost);

// get posts in feed (yours + followings)
router.get('/feeds/all', isLoggedIn, getFeed);

// get all of my posts
router.get('/myposts/all', isLoggedIn, getMyPosts);
// delete a post
router.delete('/:id', isLoggedIn, deletePost);

// update a post
router.put(
  '/:id',
  upload.single('img'),
  isLoggedIn,
  compressImageMiddleware,
  updatePost
);

// endpoint to like a post
router.put('/:id/like', isLoggedIn, likePost);

// endpoints to dislike a liked post
router.put('/:id/dislike', isLoggedIn, unlikePost);

module.exports = router;
