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

const getUserFromId = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      userSearching = await User.findById(id);
      if (userSearching) {
        userFromDB = await User.find({ _id: req.params.id }).select(
          'username following followers'
        );
        if (userFromDB.length == 1) {
          res.status(200).json(userFromDB);
        } else {
          res.status(404).json('User not found');
        }
      } else {
        res.status(404).json('Invalid token');
      }
    } catch (error) {
      res.status(403).json('Not authorized to access this route');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

const getUserFromUsername = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      userSearching = await User.findById(id);
      if (userSearching) {
        userFromDB = await User.find({ username: req.params.username }).select(
          'username following followers'
        );
        if (userFromDB.length == 1) {
          res.status(200).json(userFromDB);
        } else {
          res.status(404).json('User not found');
        }
      } else {
        res.status(404).json('Invalid token');
      }
    } catch (error) {
      res.status(403).json(error);
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

const deleteUser = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const email = decoded.email;
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      const postsOfTheUser = await Post.deleteMany({ userId: id });
      // if(postsOfTheUser.length > 1) {
      //     await postsOfTheUser.delete()
      // }
      const userToDelete = await User.findOneAndDelete({
        email: email,
        _id: id,
      });
      if (userToDelete) {
        res.status(200).json('User has been deleted from database');
      } else {
        res.status(404).json('No user with this token');
      }
    } catch (error) {
      res.status(403).json('Not authorized to perform this action');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

const updateUser = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const email = decoded.email;
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
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
        { email: email, _id: id },
        req.body,
        { new: true, runValidators: true }
      );
      if (!userToUpdate) {
        return res.status(404).json('No user with this token');
      }
      res.status(200).json('Update done');
    } catch (error) {
      res.status(403).json(error);
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

const followUser = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      userSearching = await User.findById(id);
      if (userSearching) {
        const user = await User.findOne({ _id: req.params.id });
        if (user._id.equals(userSearching._id)) {
          return res.status(401).json('You cannot follow yourself');
        }
        if (!user.followers.includes(userSearching._id)) {
          await user.updateOne({ $push: { followers: userSearching._id } });
          await userSearching.updateOne({
            $push: { following: req.params.id },
          });
          res.status(200).json('Following now');
        } else {
          res.status(401).json('You already follow this user');
        }
      } else {
        res.status(404).json('Invalid token');
      }
    } catch (error) {
      res.status(403).json('Not authorized to access this route');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

const unfollowUser = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      userSearching = await User.findById(id);
      if (userSearching) {
        const user = await User.findOne({ _id: req.params.id });
        if (user.followers.includes(userSearching._id)) {
          await user.updateOne({ $pull: { followers: userSearching._id } });
          await userSearching.updateOne({
            $pull: { following: req.params.id },
          });
          res.status(200).json('Unfollowed');
        } else {
          res.status(401).json('You dont follow this user');
        }
      } else {
        res.status(404).json('Invalid token');
      }
    } catch (error) {
      res.status(403).json('Not authorized to access this route');
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
