export class Node_ {
  private children: [Node_, Node_];

  constructor(private id: number,
              private parent_: number,
              private pc: string,
              private ifPC?: string,
              private elsePC?: string) {
    if (ifPC && elsePC) {
      // Then this is an actual node and will have
      // two virtual nodes as children
      this.children = [new Node_((-2) * id - 1, id, ifPC), 
                       new Node_((-2) * id - 2, id, elsePC)];
    } else {
      // Then this is a virtual node and it has
      // no children
      this.children = undefined;
    }
  }

  public addLeft = (leftNode: Node_) => {
    const [_, rightNode] = this.children;
    this.children = [leftNode, rightNode];
  };

  public addRight = (rightNode: Node_) => {
    const [leftNode, _] = this.children;
    this.children = [leftNode, rightNode];
  };

  public getLeft = () => {
    const [left, _] = this.children;
    return left;
  }

  public getRight = () => {
    const [_, right] = this.children;
    return right;
  }

  public getID = () => {
    return this.id;
  }

  public getIfPC = () => {
    return this.ifPC;
  }

  public getElsePC = () => {
    return this.elsePC;
  }

  public getParent = () => {
    return this.parent_;
  }

  public getSize = () => {
    if (this.children == undefined) {
      return 0;
    }
    return 1 + this.children.map(child => child.getSize()).reduce((a, b) => a + b, 0);
  }
}

