const express = require('express');
const router = express.Router();

const { login, register } = require('../controllers/authController');

// create a new user
router.post('/register', register);

// login an existing user
router.post('/login', login);

module.exports = router;
