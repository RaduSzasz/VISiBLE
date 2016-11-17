var express = require('express');
var app = express();

var nodes = require('./nodes.json');

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
  var 
});

app.get('/', (req, res) => {
  // Some code to put the file to jpf
  res.send('Home page')
})

app.listen(5000, () => {
  console.log('SERVER RUNNING ON PORT 5000') 
})
