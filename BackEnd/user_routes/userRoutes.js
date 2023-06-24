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
  updateNormalInfo
);
router.put('/remove-bg-img', isLoggedIn, removeBgIMG);

module.exports = router;
