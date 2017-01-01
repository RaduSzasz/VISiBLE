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
  symbolicMethod : string = null;
  methods:Array<Object>; 
  constructor(private treeService: TreeService){ }

  selectSymbolic(data) {
    if (data && data.response) {
      this.methods = [ {num: 0, name: "method1", args: [{type: "int", name: "num"}]},
        {num: 1, name: "method2", args: [{type: "string", name: "letter"}]} ];
      //this.methods = JSON.parse(data.response);
    }
  }

  setInitialTree(){
    let steer_promise : Promise<Tree> = this.treeService.drawTree(this.symbolicMethod);
    steer_promise.then(tree => {
      this.initialTree = tree;
    });
  }
}
