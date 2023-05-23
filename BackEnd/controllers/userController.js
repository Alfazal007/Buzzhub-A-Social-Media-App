const User = require('../models/User');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Post = require('../models/Post');

class CustomError extends Error {
  constructor(message, statusCode) {
    super(message);
    this.name = 'CustomError';
    this.statusCode = statusCode;
  }
}

// middleware done
const getUserFromId = async (req, res) => {
  try {
    userFromDB = await User.find({ _id: req.params.id }).select(
      'username following followers'
    );
    res.status(200).json(userFromDB);
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const getUserFromUsername = async (req, res) => {
  try {
    userFromDB = await User.find({ username: req.params.username }).select(
      'username following followers'
    );
    if (userFromDB.length == 1) {
      res.status(200).json(userFromDB);
    } else {
      res.status(404).json('User not found');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const deleteUser = async (req, res) => {
  try {
    const postsOfTheUser = await Post.deleteMany({ userId: req.id });
    // if(postsOfTheUser.length > 1) {
    //     await postsOfTheUser.delete()
    // }
    const userToDelete = await User.findOneAndDelete({
      email: req.email,
      _id: req.id,
    });
    res.status(200).json('User has been deleted from database');
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const updateUser = async (req, res) => {
  try {
    if (req.body.password) {
      try {
        const salt = await bcrypt.genSalt(10);
        req.body.password = await bcrypt.hash(req.body.password, salt);
      } catch (err) {
        console.log(err);
        return res.status(500).json(err);
      }
    }
    const userToUpdate = await User.findOneAndUpdate(
      { email: req.email, _id: req.id },
      req.body,
      { new: true, runValidators: true }
    );
    res.status(200).json('Update done');
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const followUser = async (req, res) => {
  try {
    const user = await User.findOne({ _id: req.params.id });
    if (user._id.equals(req.userSearching._id)) {
      return res.status(401).json('You cannot follow yourself');
    }
    if (!user.followers.includes(req.userSearching._id)) {
      await user.updateOne({ $push: { followers: req.userSearching._id } });
      await req.userSearching.updateOne({
        $push: { following: req.params.id },
      });
      res.status(200).json('Following now');
    } else {
      res.status(401).json('You already follow this user');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const unfollowUser = async (req, res) => {
  try {
    const user = await User.findOne({ _id: req.params.id });
    if (user.followers.includes(req.userSearching._id)) {
      await user.updateOne({ $pull: { followers: req.userSearching._id } });
      await req.userSearching.updateOne({
        $pull: { following: req.params.id },
      });
      res.status(200).json('Unfollowed');
    } else {
      res.status(401).json('You dont follow this user');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

module.exports = {
  getUserFromId,
  getUserFromUsername,
  deleteUser,
  updateUser,
  followUser,
  unfollowUser,
};
