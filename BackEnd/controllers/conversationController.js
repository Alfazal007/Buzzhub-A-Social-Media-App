const Conversation = require('../models/Conversation');

const addConversation = async (req, res) => {
  return res.json('Hi whatsup');
};

const getConversation = async (req, res) => {
  return res.json('Hi whatsup');
};

module.exports = { addConversation, getConversation };
