import { Component }     from '@angular/core';

import { TreeService }   from '../tree/tree.service';
import { Tree } from '../tree/tree';
import { TreeComponent } from '../tree/tree.component';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})

export class AppComponent {
  public initialTree = null;
  title = 'VISiBLE';
  options: Object = {
    url: 'http://localhost:8080/upload'
  };

  constructor(private treeService: TreeService){ }

  setInitialTree(data){
    if (data && data.response) {
      // this.filename = data.originalName;
      // this.uploadSuccess = true;
      this.initialTree = this.treeService.parseTree(JSON.parse(data.response));
    }
  }
}
