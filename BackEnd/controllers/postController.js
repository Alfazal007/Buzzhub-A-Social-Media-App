const Post = require('../models/Post');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const fs = require('fs');

// Set up multer storage engine

createPost = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }
    const token = authHeader.split(' ')[1];
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const userId = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      const userMakingPosts = await User.findById(userId);
      if (!userMakingPosts) {
        return res.status(404).json('Invalid token');
      }
      // await Post.collection.dropIndex('username_1');
      // await Post.collection.dropIndex('email_1');
      // console.log('Index dropped successfully');
      const { description } = req.body;
      let img;
      if (req.file) {
        img = fs.readFileSync(req.file.path); // read the image file as a buffer
        fs.unlinkSync(req.file.path); // delete the temporary file
        const newPost = new Post({ userId, description, img });
        await newPost.validate();
        const savedPost = await newPost.save();
        res.status(201).json(savedPost);
      }
    } catch (error) {
      res.status(403).json(error.message);
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

getFeed = async (req, res) => {
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
        postSearching = await Post.findById(req.params.id);

        if (postSearching) {
          const { userId, description, img, likes } = postSearching;
          const imgBase64 = img ? img.toString('base64') : null;
          return res
            .status(200)
            .json({ userId, description, img: imgBase64, likes });
        } else {
          res.status(404).json('Post not found');
        }
      } else {
        res.status(403).json('Create an account to see the post');
      }
    } catch (error) {
      res.status(403).json('Not authorized to access this route');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

getPost = async (req, res) => {
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
        postSearching = await Post.findById(req.params.id);

        if (postSearching) {
          const { userId, description, img, likes } = postSearching;
          const imgBase64 = img ? img.toString('base64') : null;
          return res
            .status(200)
            .json({ userId, description, img: imgBase64, likes });
        } else {
          res.status(404).json('Post not found');
        }
      } else {
        res.status(403).json('Create an account to see the post');
      }
    } catch (error) {
      res.status(403).json('Not authorized to access this route');
    }
  } catch (err) {
    res.status(500).json(err);
  }
};

deletePost = async (req, res) => {
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
      const user = await User.findById(id);
      if (user) {
        const postToDelete = await Post.findById(req.params.id).select(
          'userId description'
        );
        if (postToDelete) {
          if (user._id.equals(postToDelete.userId)) {
            await Post.findOneAndDelete({ _id: req.params.id });
            return res.status(200).json('Deleted successfully');
          } else {
            return res.status(403).json('Delete your own posts');
          }
        } else {
          return res.status(404).json('No post with this id');
        }
      } else {
        return res.status(403).json('Login to delete post');
      }
    } catch (err) {
      res.status(401).json(err.message);
    }
  } catch (err) {
    res.json(err);
  }
};

updatePost = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token or invalid token');
    }
    try {
      const token = authHeader.split(' ')[1];
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      const user = await User.findById(id);
      if (user) {
        const post = await Post.findById(req.params.id);
        if (user._id.equals(post.userId)) {
          let changeObject = {};
          let img;
          if (req.file) {
            img = fs.readFileSync(req.file.path); // read the image file as a buffer
            fs.unlinkSync(req.file.path); // delete the temporary file
            changeObject.img = img;
          }
          if (req.body.description) {
            changeObject.description = req.body.description;
          }
          const updatedPost = await Post.findOneAndUpdate(
            { _id: req.params.id },
            changeObject,
            { new: true, runValidators: true }
          );
          res.status(200).json(updatedPost);
        } else {
          return res
            .status(404)
            .json('Login again with valid credentials to edit post');
        }
      } else {
        return res
          .status(404)
          .json('Login again with valid credentials to edit post');
      }
    } catch (err) {
      return res.status(401).json(err.message);
    }
  } catch (err) {
    return res.status(401).json(err);
  }
};

likePost = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token or invalid token');
    }
    try {
      const token = authHeader.split(' ')[1];
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      const user = await User.findById(id);
      if (user) {
        const userIdLikingThePost = user._id;
        const post = await Post.findById(req.params.id);
        if (post) {
          if (!post.likes.includes(userIdLikingThePost)) {
            const updatedPost = await Post.findOneAndUpdate(
              { _id: req.params.id },
              { $push: { likes: userIdLikingThePost } }
            );
            res.status(200).json('You liked the post');
          } else {
            return res.status(401).json('Already liked the post');
          }
        }
      } else {
        return res
          .status(404)
          .json('Login again with valid credentials to edit post');
      }
    } catch (err) {
      return res.status(401).json(err.message);
    }
  } catch (err) {
    return res.status(401).json(err);
  }
};

unlikePost = async (req, res) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token or invalid token');
    }
    try {
      const token = authHeader.split(' ')[1];
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const id = decoded.id;
      if (decoded.exp <= Date.now() / 1000) {
        return res.status(401).json({ error: 'Token has expired' });
      }
      const user = await User.findById(id);
      if (user) {
        const userIdDislikingThePost = user._id;
        const post = await Post.findById(req.params.id);
        if (post) {
          if (post.likes.includes(userIdDislikingThePost)) {
            const updatedPost = await Post.findOneAndUpdate(
              { _id: req.params.id },
              { $pull: { likes: userIdDislikingThePost } }
            );
            res.status(200).json('You disliked the post');
          } else {
            return res.status(401).json('Did not like in the first place');
          }
        }
      } else {
        return res
          .status(404)
          .json('Login again with valid credentials to edit post');
      }
    } catch (err) {
      return res.status(401).json(err.message);
    }
  } catch (err) {
    return res.status(401).json(err);
  }
};

module.exports = {
  createPost,
  getPost,
  getFeed,
  deletePost,
  updatePost,
  likePost,
  unlikePost,
};
