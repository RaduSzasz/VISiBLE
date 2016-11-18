var express = require('express');
var app = express();

var nodesService = require('./nodes.service');

var PORT = process.env.PORT || 5000;

app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    next();
});


app.post('/upload', (req, res) => {
  // Some code to put the file to jpf
  res.json({
    "upload_id": 1
  }) 
});

/*
app.get('/uploads/:uid', (req, res) => {
  var uid = req.params.uid;
  res.json({
    uid: uid
  });
});

app.get('/uploads/:uid/nodes/:nid', (req, res) => {
  var uid = req.params.uid;
  var nid = req.params.nid;
  res.json({
    uid: uid,
    nid: nid
  });
});
*/

app.get('/nodes/:nid', (req, res) => {
  var nid = req.params.nid;
  res.json(nodesService.getNode(nid));
});

app.listen(PORT, () => {
  console.log('SERVER RUNNING ON PORT ' + PORT) 
})
