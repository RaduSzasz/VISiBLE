var express = require('express');
var app = express();

var nodesService = require('./nodes.service');

var PORT = process.env.PORT || 5000;

var curNode = 0;

app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    next();
});

// Return the first three nodes.
app.post('/upload', (req, res) => {
  // Some code to put the file to jpf
  console.log('UPLOAD');
  curNode = 0;

  res.json(nodesService.getNode(curNode));
});

// Return three nodes from the left child
app.get('/stepleft', (req, res) => {
  // Some code to put the file to jpf
  curNode = 2*curNode + 1;
  console.log('STEP LEFT');

  res.json(nodesService.getNode(curNode));
});

// Return three nodes from the right child
app.get('/stepright', (req, res) => {
  // Some code to put the file to jpf
  curNode = 2*curNode + 2;
  console.log('STEP RIGHT');

  res.json(nodesService.getNode(curNode));
});


app.listen(PORT, () => {
  console.log('SERVER RUNNING ON PORT ' + PORT) 
})
