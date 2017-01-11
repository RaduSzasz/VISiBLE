import {OnInit, OnChanges, style} from '@angular/core';
import { Component, ElementRef, Input } from '@angular/core';
import * as d3 from 'd3';
import * as Moment from 'moment';
import { TreeService } from './tree.service';

@Component({
  selector: 'tree-container',
  styleUrls: ['src/tree/tree.component.css'],
  templateUrl: 'src/tree/tree.component.html'
})

export class TreeComponent implements OnInit, OnChanges {
  private _d3_svg;
  private _d3_tree;
  private _d3_diagonal;
  private currNode;
  private rootNode;
  private COLORS = {
    node_expandable: '#fdf6e3',
    node_visited:'#b58900',
    node_unvisited:'black',
    link_connected: '#dc322f',
    link_expandable: '#fdf6e3',
    link_non_expandable: 'green',
    text_connected: '#859900',
    //text_disconnected: '#839496' 
    text_expandable: 'yellow',
    text_non_expandable: '#fdf6e3'
  }
  @Input() tree;

       

  constructor(private treeService: TreeService,
              private elemRef: ElementRef) { }

  restart() {
    //this.currNode = null;
    this.tree = null;
    this.treeService.restart().then(tree => {
      this.currNode = null;
      this.tree = tree
      this.drawTree();
    });
  }

  ngOnInit() {
    var margin = {top: 20, right: 120, bottom: 20, left: 120};
    var width = 960 - margin.right - margin.left;
    var height = 500 - margin.top - margin.bottom;
    this._d3_tree = d3.layout.tree().size([width, height]);
    console.log(d3);
    console.log(this._d3_tree);
    this._d3_diagonal = d3.svg.diagonal();

    this._d3_svg = d3.select(this.elemRef.nativeElement).select('svg')
                .attr('width', width + margin.right + margin.left)
                .attr('height', height + margin.top + margin.bottom)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
    if(this.tree) this.drawTree();
  }

  ngOnChanges(changes) {
    if(changes['tree'].currentValue){
      if(this._d3_svg) this.drawTree();
    }
  }

  drawTree() {
    console.log('drawing');
    var tree = this._d3_tree;
    var svg = this._d3_svg;
    var diagonal = this._d3_diagonal;

    svg.html('');

    console.log(this.currNode);
    this.rootNode = this.tree.getRoot();
    if(!this.currNode) this.currNode = this.rootNode;

    var isExpandableLeft = (n) => n.getID() == -2 * this.currNode.getID() - 1;
    var isExpandableRight = (n) => n.getID() == -2 * this.currNode.getID() - 2;

    // Compute the new tree layout.
    console.log(tree);
    var nodes = tree.nodes(this.rootNode);
    console.log(this.rootNode);
    console.log(this.currNode);
    console.log(nodes);
    var links = tree.links(nodes);

    // Update the nodes…
    var node = svg.selectAll('g.node').data(nodes);

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append('g');
    nodeEnter.append('circle')
      .attr('r', 10);

    // for debug purpose: displays id next to each node
    nodeEnter.append('text').attr('dx', '15px');
    node.selectAll('text').text(d => {console.log(d.getID()); return d.getID()})


    node.selectAll('circle')
      .style('fill', (d) => {
        console.log(d.getID());
        if(isExpandableLeft(d) || isExpandableRight(d)) {
          return this.COLORS.node_expandable;
        } else {
          // visited and not visited 
          if (d.getID() >= 0 ) {
            return this.COLORS.node_visited;
          } else {
            return this.COLORS.node_unvisited;
          }
        }
      });

    node.attr('class', 'node')
      .attr('transform', (d) => `translate(${d.x}, ${d.y})`)
      .style('cursor', (d) => {
        if(isExpandableLeft(d) || isExpandableRight(d)){
          return 'pointer';
        } else{
          return 'default';
        }
      })
      .on('click', (d) => {
        var steer_promise;
        
        if(d.getID() < 0 && d.getParent().getID() == this.currNode.getID()) {
          if(isExpandableLeft(d)) {
            steer_promise = this.treeService.stepLeft(this.currNode);
            steer_promise.then(res => {
              console.log("Updating current node (left)");
              this.currNode = this.currNode.getLeft();
              if(!this.currNode.isLeaf()) {
                this.drawTree();
                console.log(this.currNode);
              }
            });
          }

          if(isExpandableRight(d)) {
            steer_promise = this.treeService.stepRight(this.currNode);
            steer_promise.then(res => {
              console.log("Updating current node (right)");
              this.currNode = this.currNode.getRight();
              if(!this.currNode.isLeaf()) {
                this.drawTree();
              }
            });
          }
        }
        
      }) ;

    // Transition exiting nodes to the parent's new position.
    node.exit().remove();

    // Update the links…
    svg.selectAll('g.link').remove();
    var link = svg.selectAll('g.link').data(links);

    // Enter any new links at the parent's previous position.
    var linkEnter = link.enter().insert('g', 'g.node');
    linkEnter.append('path');
    linkEnter.append('text');

    link.selectAll('path')
        .attr('class', 'link')
        .attr('d', diagonal)
        .style('stroke', p => {
          if(p.target.getID() >= 0){
            return this.COLORS.link_connected;
            //return '#4285f4';
          } else {
            // Check if expandable
            if(isExpandableLeft(p.target) || isExpandableRight(p.target)){
              return this.COLORS.link_expandable;
            } else{
              return this.COLORS.link_non_expandable;
            }
          }
        })
        .style("stroke-dasharray", p => {
          if(p.target.getID() >= 0){
            return "0, 0";
          } else{
            return "10,3";
          }
        })
        .style('stroke-width', '1.5px')
        .style('fill', 'none');

    link.selectAll('text')
    .attr('x', d => 0.5*d.source.x + 0.5*d.target.x + ((d.target.isRight())? 20: -20))
    .attr('y', d => 0.5*d.source.y + 0.5*d.target.y)
    .attr('text-anchor', d => (d.target.isRight())? 'start' : 'end')
    .text(d => d.target.pc)
    .style('fill', link => {
      if (link.target.getID() >= 0) {
        return this.COLORS.text_connected;
        //return "#dc322f";
      } else {
        // Check if expandable
        if (isExpandableLeft(link.target) || isExpandableRight(link.target)) {
          return this.COLORS.text_expandable;
        } else {
          return this.COLORS.text_non_expandable;
        }
      }
    });

    link.attr('class', 'link');


    link.exit().remove();

  }
}
