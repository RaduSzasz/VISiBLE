import { Node_ } from './node'
export class Tree {
  constructor(private root: Node_) {}

  getRoot = () => {
    return this.root;
  }
}
