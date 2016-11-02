import { Component } from '@angular/core';
import * as d3 from 'd3';
import * as Moment from 'moment';
import { TreeService } from './tree.service';
import { TREE } from './mocktree';

@Component({
  selector: 'tree-container',
  styleUrls: ['src/tree/tree.component.css'],
  templateUrl: 'src/tree/tree.component.html',
  providers: [TreeService]
})

export class TreeComponent {
  constructor(private treeService: TreeService) { }

  getTree() {
    console.log('Get tree function called.');
    this.drawTree();
  }

  drawTree() {
    var root = TREE
    var margin = {top: 20, right: 120, bottom: 20, left: 120},
      width = 960 - margin.right - margin.left,
      height = 500 - margin.top - margin.bottom;

    console.log(d3);
    var tree = d3.layout.tree().size([height, width]);

    var diagonal = d3.svg.diagonal()
      .projection(function(d) { return [d.y, d.x]; });

    var svg = d3.select("body").append("svg")
      .attr("width", width + margin.right + margin.left)
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
  }
}
