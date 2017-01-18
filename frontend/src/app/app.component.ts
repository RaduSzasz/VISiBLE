import { Component, OnInit }     from '@angular/core';

import { Tree } from '../tree/tree';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})

export class AppComponent implements OnInit{
  public initialTree = null;
  title = 'VISiBLE';

  constructor(){ }
  
  ngOnInit() {
    document.body.className += ' fade-out';
  }

  updateTree(tree) {
    console.log(tree);
    this.initialTree = tree;
  }
  
}
