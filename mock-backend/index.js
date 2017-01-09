var express = require('express')
var app = express()

var dummy = require('./dummy');

var step_no = 0;
var stepup = () => step_no = (step_no + 1) % 3;

app.use(express.static('../frontend'));

app.post('/upload', (req, res) => {
	step_no = 0;
  res.json(dummy['upload']);
});

app.post('/symbolicmethod', (req, res) => {
	res.json(dummy['symbolicmethod']);
});

app.get(/\/step(left|right)/, (req, res) => {
  res.json(dummy['step' + step_no]);
  stepup();
});

app.get('*', (req, res) => {
	res.sendFile('../frontend/index.html');
});

app.listen(8080);
