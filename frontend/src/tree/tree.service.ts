import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { Tree } from './tree';
import { Node_ } from './node';

@Injectable()
export class TreeService {

  constructor(private api: ApiService) {}

  stepLeft(): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepleft`).then(nodes => resolve(this.parseTree(nodes)));
    });
  }

  stepRight(): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepright`).then(nodes => resolve(this.parseTree(nodes)));
    });
  }

  parseTree(n) {
    // parseTree :: Node] -> Tree

    // construct the trees from the node
    return new Tree(new Node_(n.id, n.parent_, n.IfPC, n.ElsePC));
  }
}
