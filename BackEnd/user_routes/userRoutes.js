const express = require('express');
const router = express.Router();
const {
  getUserFromId,
  getUserFromUsername,
  deleteUser,
  updateUser,
  followUser,
  unfollowUser,
  forgotpassword,
  serveChangePasswordPage,
  handleFormPage,
  updateNormalInfo,
  removeBgIMG,
} = require('../controllers/userController');
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

// get a user through id
router.get('/profile', isLoggedIn, getUserFromId);
// get a user using username also get the posts of this user
router.get('/username/:username', isLoggedIn, getUserFromUsername);

// delete a user
router.delete('/delete', isLoggedIn, deleteUser);

// update a user
//  NOTE : WHILE DEVELOPING FRONT END IF A USER CHANGES EMAIL THEN HE SHOULD RE-LOGIN SHOW THIS NOTE IN GUI AND REDIRECT USER TO LOGIN PAGE
router.put('/update', isLoggedIn, updateUser);
router.post('/forgotpassword', forgotpassword);

// follow a user
router.put('/:id/follow', isLoggedIn, followUser);

// unfollow a user
router.put('/:id/unfollow', isLoggedIn, unfollowUser);

router.get('/forgot-password/id/:id/token/:token', serveChangePasswordPage);
router.post('/forgot-password/id/:id/token/:token', handleFormPage);

router.put(
  '/update-normal',
  upload.single('img'),
  isLoggedIn,
  compressImageMiddleware,
  updateNormalInfo
);
router.put('/remove-bg-img', isLoggedIn, removeBgIMG);

module.exports = router;
