import { Component, ElementRef, OnInit } from '@angular/core';
import * as d3 from 'd3';
import * as Moment from 'moment';
import { TreeService } from './tree.service';

@Component({
  selector: 'tree-container',
  styleUrls: ['src/tree/tree.component.css'],
  templateUrl: 'src/tree/tree.component.html',
  providers: [TreeService]
})

export class TreeComponent implements OnInit {
  private _d3_svg;
  private _d3_tree;
  private _d3_diagonal;
  public tree;

  constructor(private treeService: TreeService,
              private elemRef: ElementRef) { }

  ngOnInit() {
    var margin = {top: 20, right: 120, bottom: 20, left: 120};
    var width = 960 - margin.right - margin.left;
    var height = 500 - margin.top - margin.bottom;
    this._d3_tree = d3.layout.tree().size([width, height]);
    this._d3_diagonal = d3.svg.diagonal();

    this._d3_svg = d3.select(this.elemRef.nativeElement).select('svg')
                .attr('width', width + margin.right + margin.left)
                .attr('height', height + margin.top + margin.bottom)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
  }

  getTree() {
    this.treeService.getTree(0).then(t => {
      this.tree = t;
      this.drawTree();
    });
  }

  drawTree() {
    var tree = this._d3_tree;
    var svg = this._d3_svg;
    var diagonal = this._d3_diagonal;

    var root = this.tree;

    // Compute the new tree layout.
    var nodes = tree.nodes(root);
    console.log(root);
    console.log(nodes);
    var links = tree.links(nodes);

    // Update the nodes…
    var node = svg.selectAll('g.node').data(nodes);

    // Update text at nodes
    node.select('text').text((d) => d.data);

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append('g')
    .attr('class', 'node')
    .attr('transform', (d) => `translate(${d.x}, ${d.y})`)
    .on('click', function(d, i){
      console.log('hi');
      d3.select(this).style("fill", "red");
    }) ;

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