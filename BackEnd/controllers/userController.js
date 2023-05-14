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

register = async (req, res) => {
  try {
    const salt = await bcrypt.genSalt(10);
    if (req.body.password.length < 8) {
      return res
        .status(401)
        .json('Password length should be atleast 8 characters long');
    }
    const hashedPassword = await bcrypt.hash(req.body.password, salt);
    const newUser = new User({
      username: req.body.username,
      email: req.body.email,
      password: hashedPassword,
    });
    await newUser.validate();
    const user = await newUser.save();
    res.status(201).json(user);
  } catch (err) {
    console.log(err);
    res.status(500).json(err);
  }
};

login = async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password) {
      throw new CustomError('Please provide both email and password', 403);
    }

    const user = await User.findOne({ email: email });
    const validPassword = await bcrypt.compare(password, user.password);
    if (!validPassword) {
      return res.status(400).json('Wrong password');
    }
    const id = user._id;
    const token = jwt.sign({ id, email }, process.env.JWT_SECRET, {
      expiresIn: '30d',
    });
    // this token should be stored in the client as it is used for authentication and authorization in the later requests
    res.status(200).json({ msg: 'Login successful', token });
  } catch (err) {
    res.status(404).json('User not found');
  }
};

getUserFromId = async (req, res) => {
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

getUserFromUsername = async (req, res) => {
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

deleteUser = async (req, res) => {
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

updateUser = async (req, res) => {
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

followUser = async (req, res) => {
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

unfollowUser = async (req, res) => {
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
  register,
  login,
  getUserFromId,
  getUserFromUsername,
  deleteUser,
  updateUser,
  followUser,
  unfollowUser,
};
