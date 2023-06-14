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
  handleFormPage
} = require('../controllers/userController');
const isLoggedIn = require('../middleware/isLoggedIn');

// get a user through id
router.get('/:id', isLoggedIn, getUserFromId);
// get a user using username
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



module.exports = router;
