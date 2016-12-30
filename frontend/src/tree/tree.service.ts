import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { Tree } from './tree';
import { Node_ } from './node';

@Injectable()
export class TreeService {

  constructor(private api: ApiService) {}

  stepLeft(currNode): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepleft`).then(node => resolve(this.addNewLeft(currNode, node)));
    });
  }

  stepRight(currNode): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepright`).then(node => resolve(this.addNewRight(currNode, node)));
    });
  }

  parseTree(n) {
    // parseTree :: Node] -> Tree

    // construct the trees from the node
    return new Tree(new Node_(n.id, n.parent_, n.IfPC, n.ElsePC));
  }

  addNewLeft(currNode, node) {
    currNode.addLeft(node);
    return currNode;
  }

  addNewRight(currNode, node) {
    currNode.addRight(node);
    return currNode;
  }
}
