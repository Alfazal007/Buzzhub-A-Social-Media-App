const User = require('../models/User');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Post = require('../models/Post');
const nodemailer = require("nodemailer");
const path = require("path");
const Story = require("../models/Story");
const fs = require("fs");
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
    const userFromDB = await User.find({ _id: req.id }).select(
      'username following followers bio img'
    );
    const posts = await Post.find({ userId: req.id });
    const followersLength = userFromDB[0].followers.length;
    const followingLength = userFromDB[0].following.length;
    const postsLength = posts.length;
    if (userFromDB.length == 1) {
      const userObject = {
        username: userFromDB[0].username,
        following: followingLength,
        followers: followersLength,
        bio: userFromDB[0].bio,
        img: userFromDB[0].img,
        posts: postsLength
      };
      return res.status(200).json(userObject);
    }
    return res.status(404).json('User not found');
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const getUserFromUsername = async (req, res) => {
  try {
    const userFromDB = await User.find({ username: req.params.username }).select(
      'username following followers'
    );
    const posts = await Post.find({ username: req.params.username });
    if (userFromDB.length == 1) {
      const userObject = {
        username: userFromDB[0].username,
        following: userFromDB[0].following,
        followers: userFromDB[0].followers,
        posts: posts
      };
      res.status(200).json(userObject);
    } else {
      res.status(401).json("User not found");
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

// middleware done
const deleteUser = async (req, res) => {
  try {
    const postsOfTheUser = await Post.deleteMany({ userId: req.id });
    const storyOfTheUser = await Story.deleteMany({ userId: req.id });
    const deletedUser = await User.findById(req.id);
    console.log(deletedUser);
    console.log(deletedUser.following);
    const following = deletedUser.following;
    // await Promise.all(deletedUser.following.map(async()))
    await Promise.all(following.map(async (userToBeUpdated) => {
      const user = await User.findOne({ _id: userToBeUpdated });
      const doesInclude = user.followers;
      console.log(doesInclude);
      const index = doesInclude.indexOf(req.id);
      if (index > -1) {
        doesInclude.splice(index, 1);
      }
      console.log(doesInclude);
      user.followers = doesInclude;
      user.save();
    }));
    const followers = deletedUser.followers;
    await Promise.all(followers.map(async (userToBeUpdated) => {
      const user = await User.findOne({ _id: userToBeUpdated });
      const doesInclude = user.following;
      console.log(doesInclude);
      const index = doesInclude.indexOf(req.id);
      if (index > -1) {
        doesInclude.splice(index, 1);
      }
      console.log(doesInclude);
      user.following = doesInclude;
      user.save();
    }));
    await User.findByIdAndDelete({ _id: req.id });
    res.status(200).json("User has been successfully deleted and the database has been updated.");

  } catch (err) {
    console.log("In catch section of top try block");
    console.log(err);
    res.status(500).json("Hi");
  }
};

// middleware done
// this is only for email and password
const updateUser = async (req, res) => {
  try {
    // this is to change email and password(not forgot password)
    if (req.body.old_password) {
      const isValidPassword = await bcrypt.compare(req.body.old_password, req.userSearching.password);
      console.log(isValidPassword);
      if (isValidPassword) {
        if (req.body.email) {
          const userToUpdate = await User.findOneAndUpdate(
            { email: req.email, _id: req.id },
            { email: req.body.email },
            { new: true, runValidators: true }
          );
          console.log(userToUpdate);
          if (userToUpdate === null) {
            res.status(403).json("Not found the user, relogin");
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
            res.status(403).json("Not found the user, relogin");
          }
          else {
            res.status(200).json('Update done');
          }
        } else {
          res.status(403).json("wrong password or bad request");
        }
      } else {
        res.status(403).json("wrong password");
      }
    } else {
      // here forgot password
      res.status(201).send("Send old password");
    }
  } catch (err) {
    res.status(500).json(err.message);
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
    if (req.body.email) {
      const userWithEmail = await User.findOne({ email: req.body.email });
      if (userWithEmail != null) {
        try {
          const transporter = nodemailer.createTransport({
            host: "smtp.gmail.com",
            port: 587,
            secure: false, // true for 465, false for other ports
            auth: {
              user: process.env.EMAIL_HOST_USER, // an email address
              pass: process.env.EMAIL_HOST_PASSWORD,
            },
          });
          const expiresIn = '5m'; // Token expiration time

          const user = {
            id: userWithEmail._id, // User ID
            email: req.body.email // User email (or any other relevant data)
          };
          const token = jwt.sign(user, process.env.JWT_SECRET, { expiresIn });
          let link = "http://127.0.0.1:8800/api/users/forgot-password/id/" + userWithEmail._id + "/token/" + token;
          let info = await transporter.sendMail({
            from: process.env.EMAIL_HOST_USER, // sender address
            to: req.body.email,
            subject: "Change the password for BuzzHub", // Subject line
            text: "Click the below link to change the password (this will expire in 5 minutes) " + link
          });
          return res.status(200).json("Email sent to change the password");
        } catch (err) {
          return res.status(400).json(err.message);
        }
      } else {
        return res.status(404).json("User with this email not found");
      }
    }
    else {
      res.status(404).json("You need to provide user email id");
    }
  } catch (err) {
    res.status(500).json(err);
  }
};


const serveChangePasswordPage = async (req, res) => {
  try {
    const token = req.params.token;
    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
      if (err) {
        return res.status(400).send("The token has expired resend the request to change the password");
      } else {
        const formToServe = path.join(__dirname, '../public/form.html');
        return res.status(200).sendFile(formToServe);
      }
    });
  } catch (err) {
    res.status(500).json(err);
  }
};

const handleFormPage = async (req, res) => {
  const id = req.params.id;
  const { password1, password2 } = req.body;
  if (password1 == password2 && password1.length >= 8) {
    const salt = await bcrypt.genSalt(10);
    const new_password = await bcrypt.hash(password1, salt);
    const userToUpdate = await User.findOneAndUpdate(
      { _id: id },
      { password: new_password },
      { new: true, runValidators: true }
    );
    if (userToUpdate === null) {
      try {
        res.status(200).send("User not found");
      }
      catch (err) {
        res.status(500).send(err);
      }
    }
    else {
      const formToServe = path.join(__dirname, '../public/afterSubmit.html');
      return res.status(200).sendFile(formToServe);
    }
  } else {
    if (password1.length < 8) {
      const formToServe = path.join(__dirname, '../public/shortPassword.html');
      return res.status(401).sendFile(formToServe);
    } else {
      const formToServe = path.join(__dirname, '../public/matchPassword.html');
      return res.status(401).sendFile(formToServe);
    }
  }
};

// update more user information
const updateNormalInfo = async (req, res) => {
  try {
    let changeObject = {};
    let img;
    if (req.file) {
      img = fs.readFileSync(req.file.path); // read the image file as a buffer
      fs.unlinkSync(req.file.path); // delete the temporary file
      changeObject.img = img;
    }
    if (req.body.username) {
      changeObject.username = req.body.username;
    }
    if (req.body.bio) {
      changeObject.bio = req.body.bio;
    }
    let updatedUser;
    try {
      updatedUser = await User.findOneAndUpdate(
        { _id: req.id },
        changeObject,
        { new: true, runValidators: true }
      );
      console.log("User info updated");

    } catch (err) {
      res.status(401).json(err.message);
    }
    try {
      const posts = await Post.find({ userId: req.id });
      const afterUpdate = await Promise.all(posts.map(async (singlePost) => {
        if (req.file) {
          singlePost.profilePic = img;
        }
        if (req.body.username) {
          singlePost.username = req.body.username;
        }
        singlePost.save();
      }));
      console.log("Posts profile pic updated");
    } catch (err) {
      res.status(401).json(err.message);
    }
    res.status(200).json(updatedUser);
  } catch (err) {
    res.status(500).json(err.message);
  }
};

const removeBgIMG = async (req, res) => {
  try {
    const afterUpdate = await User.findOneAndUpdate(
      { _id: req.id },
      { img: null },
      { new: true, runValidators: true }
    );
    return res.status(200).json(afterUpdate);
  } catch (err) {
    res.status(500).json(err.message);
  }
};
module.exports = {
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
};
