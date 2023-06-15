const router = require('express').Router();
const isLoggedIn = require('../middleware/isLoggedIn');
const { postMessage, getMessage } = require('../controllers/messageController');

router.get('/:id', isLoggedIn, getMessage);
router.post('/', isLoggedIn, postMessage);

module.exports = router;
