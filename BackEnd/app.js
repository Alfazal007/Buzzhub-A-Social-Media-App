const express = require('express');
const app = express();
const userRouter = require('./user_routes/userRoutes');
require('dotenv').config();
const connect = require('./db/connect');
const bodyParser = require('body-parser');
const postRouter = require('./user_routes/postRoutes');
const authRoutes = require('./user_routes/authRoutes');

app.use(bodyParser.json());

app.use('/api/auth', authRoutes);
app.use('/api/users', userRouter);
app.use('/api/posts', postRouter);

const port = process.env.PORT || 8800;

const start = async () => {
  try {
    app.listen(port, async () => {
      await connect(process.env.MONGO_URI);
      console.log(`Server is listening on port ${port}...`);
    });
  } catch (error) {
    console.log(error);
  }
};

start();
