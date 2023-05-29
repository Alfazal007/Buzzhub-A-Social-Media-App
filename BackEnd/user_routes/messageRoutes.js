const router = require('express').Router();
const isLoggedIn = require('../middleware/isLoggedIn');
const { postMessage, getMessage } = require('../controllers/messageController');

router.get('/', isLoggedIn, getMessage);
router.post('/', isLoggedIn, postMessage);

module.exports = router;
