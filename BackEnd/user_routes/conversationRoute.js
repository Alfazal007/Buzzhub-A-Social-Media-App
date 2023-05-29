const router = require('express').Router();
const isLoggedIn = require('../middleware/isLoggedIn');
const {
  addConversation,
  getConversation,
} = require('../controllers/conversationController');

router
  .route('/')
  .get(isLoggedIn, getConversation)
  .post(isLoggedIn, addConversation);

module.exports = router;
