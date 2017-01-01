export class Method {
  constructor(public methodName: string, public args: Arg[]) { }
}

export class Arg {
  constructor(public argType: string, public argName: string) { }
}

