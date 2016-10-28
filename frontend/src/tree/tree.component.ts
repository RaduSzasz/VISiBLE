import { Component } from '@angular/core';
import * as d3 from 'd3';
import * as Moment from 'moment';
import { TreeService } from './tree.service';
import { Tree } from './tree';

@Component({
  selector: 'tree-container',
  styleUrls: ['src/tree/tree.component.css'],
  templateUrl: 'src/tree/tree.component.html',
  providers: [TreeService]
})

export class TreeComponent {
  tree: Tree;
  constructor(private treeService: TreeService) { }

  getTree() {
    console.log('Get tree function called.');
  }

  drawTree() {
    //var tree = d3.layout.tree();
  }
}
