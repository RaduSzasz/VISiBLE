import { Component, ElementRef, OnInit } from '@angular/core';
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

export class TreeComponent implements OnInit {
  private _svg;
  private _tree;
  private _diagonal;
  public TREE_STRING = JSON.stringify(TREE, undefined, 4);

  constructor(private treeService: TreeService,
              private elemRef: ElementRef) { }

  ngOnInit() {
    var margin = {top: 20, right: 120, bottom: 20, left: 120};
    var width = 960 - margin.right - margin.left;
    var height = 500 - margin.top - margin.bottom;
    this._tree = d3.layout.tree().size([height, width]);
    this._diagonal = d3.svg.diagonal().projection(d => [d.y, d.x]);

    this._svg = d3.select(this.elemRef.nativeElement).select('svg')
                .attr('width', width + margin.right + margin.left)
                .attr('height', height + margin.top + margin.bottom)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
  }

  getTree() {
    console.log('Get tree function called.');
    this.treeService.getTree().then()
    this.drawTree();
  }

  drawTree() {
    var tree = this._tree;
    var svg = this._svg;
    var diagonal = this._diagonal;

    var root = JSON.parse(this.TREE_STRING);

    // Compute the new tree layout.
    var nodes = tree.nodes(root);
    console.log(root);
    console.log(nodes);
    var links = tree.links(nodes);

    // Update the nodes…
    var node = svg.selectAll('g.node').data(nodes);

    // Update text at nodes
    node.select('text').text((d:any) => d.data);

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append('g')
    .attr('class', 'node')
    .attr('transform', (d) => `translate(${d.y}, ${d.x})`);

    nodeEnter.append('circle')
    .attr('r', 10)
    .style('fill', d => 'lightsteelblue');

    nodeEnter.append('text')
    .attr('dx', d => d.children? -13 : 13)
    .attr('dy', '.35em')
    .attr('text-anchor', d => d.children? 'end' : 'start')
    .text((d:any) => d.data);

    // Transition exiting nodes to the parent's new position.
    node.exit().remove();

    // Update the links…
    var link = svg.selectAll('path.link').data(links);

    // Enter any new links at the parent's previous position.
    link.enter().insert('path', 'g')
    .attr('class', 'link')
    .attr('d', diagonal);

    link.exit().remove();

  }
}
