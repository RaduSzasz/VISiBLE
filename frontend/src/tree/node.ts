export class Node_ {
  private children: [Node_, Node_];

  constructor(private id: Number,
              private parent_: Number,
              private pc: String,
              private ifPC?: String,
              private elsePC?: String) {
    if (ifPC && elsePC) {
      // Then this is an actual node and will have
      // two virtual nodes as children
      this.children = [new Node_(-id, id, ifPC), new Node_(-id, id, elsePC)];
    } else {
      // Then this is a virtual node and it has
      // no children
      this.children = undefined;
    }
  }

  public addLeftChild = (leftNode: Node_) => {
    const [_, rightNode] = this.children;
    this.children = [leftNode, rightNode];
  };

  public addRightChild = (rightNode: Node_) => {
    const [leftNode, _] = this.children;
    this.children = [leftNode, rightNode];
  };

  public getSize = () => {
    return 1 + this.children.map(child => child.getSize()).reduce((a, b) => a + b, 0);
  }
}

