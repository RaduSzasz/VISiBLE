import { Component }     from '@angular/core';

import { TreeService }   from '../tree/tree.service';
import { Tree } from '../tree/tree';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})

export class AppComponent {
  public initialTree = null;
  title = 'VISiBLE';

  constructor(private treeService: TreeService){ }

  updateTree(tree) {
    console.log(tree);
    this.initialTree = tree;
  }
  
}
