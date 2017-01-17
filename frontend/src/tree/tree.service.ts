import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { Tree } from './tree';
import { Node_ } from './node';
import { Method } from '../upload/method';

@Injectable()
export class TreeService {

  constructor(private api: ApiService) {}
  private last_symbolicmethod = null;

  drawTree(jar, method : Method, isSymb) : Promise<Tree> {
    this.last_symbolicmethod = {
      jar_name: jar,
      class_name: method.class_name,
      method_name: method.name,
      no_args: method.numArgs,
      is_symb: isSymb
    };
    return this.restart();
  }

  restart(): Promise<Tree> {
    console.log(this.last_symbolicmethod);
    return new Promise((resolve, reject) => {
      // note that the json is sent as a query, i.e. in the url
      this.api.post(`symbolicmethod`, this.last_symbolicmethod, null).then(node => resolve(this.parseTree(node)));
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
    // parseTree :: [Node] -> Tree

    // construct the trees from the node
    return new Tree(new Node_(n.id, n.parent_, null, n.concreteValues, n.ifPC, n.elsePC));
  }

  addNewLeft(currNode, node) {
    console.log("Adding new left");
    node = new Node_(node.id,
                    currNode,
                    currNode.getIfPC(),
                    node.concreteValues,
                    node.ifPC,
                    node.elsePC);
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
                    node.concreteValues,
                    node.ifPC,
                    node.elsePC);
    currNode.addRight(node);
    return currNode;
  }
}
