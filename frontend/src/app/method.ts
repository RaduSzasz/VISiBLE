export class Method {
  name: string;
  args: Arg[];

  constructor(name: string, args: Arg[]) {
    this.name = name;
    this.args = args;
  }

  toString(): string {
    let str = this.name + "(";
    this.args.forEach((arg) => {
      str += arg.toString() + ", "  
    }); 
    // Remove ending ", "
    if (this.args.length > 0) {
      str = str.slice(0, -2);
    }
    return str + ")"; 
  }
}

export class Arg {
  argName: string;
  argType: string; 

  constructor(argType: string, argName: string) {
    this.argType = argType;
    this.argName = argName;
  }

  toString(): string {
    return this.argType;
  }
}

