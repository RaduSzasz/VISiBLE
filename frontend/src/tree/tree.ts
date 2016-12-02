export class Tree {
  id: Number;
  parent_: Tree;
  children: Tree[];
  pc: String;

  constructor(id: Number) {
    this.id = id;
    this.children = [];
  }

  public setParent(p: Tree) {
    this.parent_ = p;
    return this;
  }

  public addChild(c: Tree) {
    if(!this.children) this.children = [];
    if(!!c) this.children.push(c);
    return this;
  }

  public getLeft() {
    return this.children[0].id < this.children[1].id? this.children[0]: this.children[1];
  }

  public getRight() {
    return this.children[0].id > this.children[1].id? this.children[0]: this.children[1];
  }

  public setPC(pc: String) {
    this.pc = pc;
    return this;
  }

  public findSubTree(index: Number){
    if(this.id == index) return this;
    return this.children.map(t => t.findSubTree(index))
                        .filter(t => !!t)[0] || null;
  }

  public getSize(){
    if(!this.children) this.children = [];
    return 1 + this.children.reduce((acc, c) => acc + c.getSize(), 0);
  }
}
