import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { Tree } from './tree';
import { Node_ } from './node';

export function parseTree(nodes) {
  // parseTree :: [Node] -> Tree

  // construct the trees from the nodes
  var trees = {}; // map id to tree
  nodes.forEach(n => {
    trees[n.id] = new Tree(n.id).setPC(n.pc);
  });
  
  // look throgh the nodes again to construct the recursive tree
  nodes.forEach(n => {
    // `t` is the corresponding tree of `n`
    var t = trees[n.id];
    console.log(trees[n.parent_]);
    t.setParent(trees[n.parent_] || null);
    n.children.forEach(cn => {
      t.addChild(trees[cn])
    });
  });

  // the first tree with null parent IS the root
  var root;
  nodes.forEach(n => {
    if(!trees[n.id].parent_) return root = trees[n.id];
  });
  return root;
}

@Injectable()
export class TreeService {

  constructor(private api: ApiService) {}

  getTree(i):Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`nodes/${i}`).then(nodes => resolve(parseTree(nodes)));
    });
  }
}
