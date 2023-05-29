const Message = require('../models/Messages');

const postMessage = async (req, res) => {
  return res.json({ message: 'Hi' });
};

const getMessage = async (req, res) => {
  return res.json({ message: 'Hi' });
};

module.exports = { postMessage, getMessage };
