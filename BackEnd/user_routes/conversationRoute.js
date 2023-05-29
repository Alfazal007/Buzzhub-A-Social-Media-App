const router = require('express').Router();
const Conversation = require('../models/Conversation');
const isLoggedIn = require('../middleware/isLoggedIn');
const {
  addConversation,
  getConversation,
} = require('../controllers/conversationController');

router
  .route('/')
  .get(isLoggedIn, addConversation)
  .post(isLoggedIn, getConversation);
