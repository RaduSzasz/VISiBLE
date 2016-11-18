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
    return 1 + this.children.reduce((acc, c) => acc + c.getSize(), 0);
  }
}
