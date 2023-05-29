const Conversation = require('../models/Conversation');

const addConversation = async (req, res) => {
  try {
    let conversation = new Conversation({
      members: [req.body.senderId, req.body.receiverId],
    });
    const savedConversation = conversation.save();
    return res.status(200).json(savedConversation);
  } catch (error) {
    res.status(500).json(error);
  }
};

const getConversation = async (req, res) => {
  try {
    const conversations = await Conversation.find({
      members: { $in: [req.id] },
    });
    return res.status(200).json(conversations);
  } catch (error) {
    res.status(500).json(error);
  }
};

module.exports = { addConversation, getConversation };
