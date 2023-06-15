const io = require('socket.io')(8900);

io.on('connection', (socket) => {
  console.log('Hello');
});
