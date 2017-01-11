var path = require('path');

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
	step_no = 0;
  setTimeout(() => res.json(dummy['symbolicmethod']), 2000);
});

app.get(/\/step(left|right)/, (req, res) => {
  res.json(dummy['step' + step_no]);
  stepup();
});

app.get('*', (req, res) => {
	res.sendFile(path.resolve('../frontend/index.html'));
});

app.listen(8080);
