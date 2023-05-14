const express = require('express');
const router = express.Router();
const {
  register,
  login,
  getUserFromId,
  getUserFromUsername,
  deleteUser,
  updateUser,
  followUser,
  unfollowUser,
} = require('../controllers/userController');

// create a new user
router.post('/register', register);

// login an existing user
router.post('/login', login);

// get a user through id
router.get('/:id', getUserFromId);
// get a user using username
router.get('/username/:username', getUserFromUsername);

// delete a user
router.delete('/delete', deleteUser);

// update a user

//  NOTE : WHILE DEVELOPING FRONT END IF A USER CHANGES EMAIL THEN HE SHOULD RE-LOGIN SHOW THIS NOTE IN GUI AND REDIRECT USER TO LOGIN PAGE
router.put('/update', updateUser);

// follow a user
router.put('/:id/follow', followUser);

// unfollow a user
router.put('/:id/unfollow', unfollowUser);

module.exports = router;
