import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { Tree } from './tree';
import { Node_ } from './node';
import { Method, Arg } from '../app/method';

@Injectable()
export class TreeService {

  constructor(private api: ApiService) {}
  drawTree(symbolicMethod : Method) : Promise<Tree> {
    let json : Object = {name: symbolicMethod.name, no_args: 4};
    return new Promise((resolve, reject) => {
      this.api.post(`symbolicmethod`, null, json).then(node => resolve(this.parseTree(node)));
    });
  }

  stepLeft(currNode : Node_): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepleft`).then(node => resolve(this.addNewLeft(currNode, node)));
    });
  }

  stepRight(currNode : Node_): Promise<Tree> {
    return new Promise((resolve, reject) => {
      this.api.get(`stepright`).then(node => resolve(this.addNewRight(currNode, node)));
    });
  }

  parseTree(n) {
    // parseTree :: Node] -> Tree

    // construct the trees from the node
    return new Tree(new Node_(n.id, n.parent_, null, n.IfPC, n.ElsePC));
  }

  addNewLeft(currNode, node) {
    console.log("Adding new left");
    node = new Node_(node.id,
                    currNode,
                    currNode.getIfPC(),
                    node.IfPC,
                    node.ElsePC);
    console.log(node);
    currNode.addLeft(node);
    console.log(currNode);
    return currNode;
  }

  addNewRight(currNode, node) {
    console.log("Adding new right");
    node = new Node_(node.id,
                    currNode,
                    currNode.getElsePC(),
                    node.IfPC,
                    node.ElsePC);
    currNode.addRight(node);
    return currNode;
  }
}
