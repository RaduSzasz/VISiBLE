import { Component, ViewChild, EventEmitter, Output, OnInit }     from '@angular/core';

import { TreeService }   from '../tree/tree.service';
import { Tree } from '../tree/tree';
import { TreeComponent } from '../tree/tree.component';

import { Method } from './method';
import { ModalDirective } from 'ng2-bootstrap';
import {Http} from "@angular/http";

@Component({
  selector: 'vis-upload',
  templateUrl: 'src/upload/upload.component.html'
})

export class UploadComponent implements OnInit {
  @Output() onUpload = new EventEmitter();
  @ViewChild('staticModal') public staticModal : ModalDirective;

  ngOnInit() {
    const port = window.location.port? `:${window.location.port}/`: `/`;
    this.uploadUrl = `http://${window.location.hostname}` + port + `upload`;
    console.log("Upload directing queries to " + this.uploadUrl);
    this.options = {
      url: this.uploadUrl
    }
    this.resetComponent();
  }

  private uploadUrl;

  public options;
  public symbolicMethod : Method;
  public jar;
  public methods;
  public isSymb;
  public isLoading;

  constructor(private treeService: TreeService){ }

  selectSymbolic(data) {
    if(!data || !data.response) return;
    let res = JSON.parse(data.response);

    if(res.error) throw res.error;
    this.jar = res.jar;
    let flatten = (arr) => [].concat.apply([], arr);
    this.methods = flatten(res.data.map((cls) => {
      return cls.methods.map((method) => {
        return new Method(cls.class,
                          method.name,
                          method.numArgs,
                          method.signature);
      });
    }));
    this.staticModal.show();
  }

  isSymbSetup() {
    if(!this.symbolicMethod) return;
    this.isSymb = [];
    for(let i = 0; i < this.symbolicMethod.numArgs; i++){
      this.isSymb.push({value: true});
    };
  }

  setInitialTree(){
    this.isLoading = true;
    let steer_promise: Promise<Tree> = this.treeService.drawTree(
      this.jar,
      this.symbolicMethod,
      this.isSymb.map(s => s.value)
    );
    steer_promise.then(tree => {
      this.resetComponent();
      this.staticModal.hide();
      this.onUpload.emit(tree);
    });
  }

  resetComponent(){
    this.symbolicMethod = null;

    this.jar = null;
    this.methods = null;
    this.isSymb = null;

    this.isLoading = false;
  }
  
}
