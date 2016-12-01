var nodes = require('./nodes');

exports.getNode = (id) => {
  if(id >= nodes.length) throw "No such node";

  var target = nodes.filter(n => n.id == id)[0];
  var children = nodes.filter(n => target.children.indexOf(n.id) >= 0);
  return [target].concat(children);
};
