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
  console.log("Hi")
  try {
    const userFromDB = await User.find({ _id: req.params.id }).select(
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
    // this is to change email and password(not forgot password)
    if (req.body.old_password) {
      const isValidPassword = await bcrypt.compare(req.body.old_password, userSearching.password);
      console.log(isValidPassword)
      if (isValidPassword) {
        if (req.body.email) {
          const userToUpdate = await User.findOneAndUpdate(
            { email: req.email, _id: req.id },
            { email: req.body.email },
            { new: true, runValidators: true }
          );
          console.log(userToUpdate)
          if (userToUpdate === null) {
            res.status(403).json("Not found the user, relogin")
          }
          else {
            res.status(200).json('Update done');
          }
        } else if (req.body.new_password) {
          const salt = await bcrypt.genSalt(10);
          const new_password = await bcrypt.hash(req.body.new_password, salt);
          const userToUpdate = await User.findOneAndUpdate(
            { email: req.email, _id: req.id },
            { password: new_password },
            { new: true, runValidators: true }
          );
          if (userToUpdate === null) {
            res.status(403).json("Not found the user, relogin")
          }
          else {
            res.status(200).json('Update done');
          }
        } else {
          res.status(403).json("wrong password or bad request")
        }
      } else {
        res.status(403).json("wrong password")
      }
    } else {
      // here forgot password
      res.status(201).send("Send old password")
    }
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


const forgotpassword = async (req, res) => {
  try {
    if(req.body.email) {
      if(req.body.new_password) {
        // find the user with the email
        const userWithEmail = await User.findOne({email : req.body.email})
        if(userWithEmail != null) {
          const salt = await bcrypt.genSalt(10)
          const hashedPassword = await bcrypt.hash(req.body.new_password, salt);
          try {
            const afterUpdate = await User.findOneAndUpdate({email : req.body.email} , {password : hashedPassword}, {new:true, runValidators : true})
            return res.status(200).json("New password has been set")
          } catch(err) {
            return res.status(401).json(err)
          }
        } else {
          return res.status(404).json("User with this email not found")
        }
      } else {
        res.status(404).json("You need to provide new valid password")
      }
    } else {
      res.status(404).json("You need to provide user email id")
    }
  } catch(err) {
    res.status(500).json(err)
  }
};



module.exports = {
  getUserFromId,
  getUserFromUsername,
  deleteUser,
  updateUser,
  followUser,
  unfollowUser,
  forgotpassword
};
