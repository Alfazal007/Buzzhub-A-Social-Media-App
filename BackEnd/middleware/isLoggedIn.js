const jwt = require('jsonwebtoken');
const User = require('../models/User');

module.exports = async function isLoggedIn(req, res, next) {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(403).json('No token provided');
    }

    const token = authHeader.split(' ')[1];
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.id = decoded.id;
    req.email = decoded.email;
    if (decoded.exp <= Date.now() / 1000) {
      return res.status(401).json({ error: 'Token has expired' });
    }

    req.userSearching = await User.findById(req.id);
    if (!userSearching) {
      return res.status(404).json({ message: 'User not found!' });
    }
    next();
  } catch (error) {
    res.json(error);
  }
};
