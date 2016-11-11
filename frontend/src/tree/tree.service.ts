import { Injectable } from '@angular/core';

import { ApiService } from '../shared/api.service';

import { TREE } from './mocktree';
import { Tree } from './tree';

@Injectable()
export class TreeService {

  constructor(api: ApiService) {}

  getTree():Promise<Tree> {
    return Promise.resolve(TREE);
  }
}
