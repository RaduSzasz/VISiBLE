import { Injectable } from '@angular/core';

import { TREE } from './mocktree';
import { Tree } from './tree';

@Injectable()
export class TreeService {

  getTree():Promise<Tree> {
    return Promise.resolve(TREE);
  }

}
