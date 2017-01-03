import { Component, ViewChild }     from '@angular/core';

import { TreeService }   from '../tree/tree.service';
import { Tree } from '../tree/tree';
import { TreeComponent } from '../tree/tree.component';

import { Method, Arg } from './method';
import { ModalDirective } from 'ng2-bootstrap';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})

export class AppComponent {
  @ViewChild('staticModal') public staticModal : ModalDirective;
  public initialTree = null;
  title = 'VISiBLE';
  options: Object = {
    url: 'http://localhost:8080/upload'
  };
  symbolicMethod : Method = null;
  methods: Method[] = [];

  constructor(private treeService: TreeService){ }

  selectSymbolic(data) {
    /* TODO: Get methods from data.response */
    if (data && data.response) {
      this.methods = [
        new Method("symVis", [new Arg("int", "num"), new Arg("int", "num2"), new Arg("int", "num3"), new Arg("int", "num4")]),
        new Method("method1", [])
      ];
      //this.methods = JSON.parse(data.response);
      this.staticModal.show();
    }
  }

  setInitialTree(){
    let steer_promise : Promise<Tree> = this.treeService.drawTree(this.symbolicMethod);
    steer_promise.then(tree => {
      this.initialTree = tree;
    });
  }
  
}
