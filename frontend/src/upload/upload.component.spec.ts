import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { By }              from '@angular/platform-browser';
import { DebugElement, EventEmitter }    from '@angular/core';

import { UploadComponent } from './upload.component';

import { TreeService }   from '../tree/tree.service';

import { Tree } from '../tree/tree'
import { Node_ } from '../tree/node'

describe('UploadComponent', () => {

  let comp:    UploadComponent;
  let fixture: ComponentFixture<UploadComponent>;
  let de:      DebugElement;
  let el:      HTMLElement;
  let tree = new Tree(new Node_(1, null, 'test', 3, 'hihi', 'test'));
  let treeServiceStub = {
    drawTree: (jar, symbolicMethod, isSymb) => Promise.resolve(tree)
  };

  beforeEach(async() => {
    TestBed.configureTestingModule({
      declarations: [ UploadComponent ], // declare the test component
      providers: [ {provide: TreeService, useValue: treeServiceStub} ]
    });

    fixture = TestBed.createComponent(UploadComponent);
    TestBed.compileComponents();

    comp = fixture.componentInstance; // AppComponent test instance
  });

	it('setInitialTree should emit the tree returned by drawTree promise', async() => {
		fixture.detectChanges();
    comp.onUpload = new EventEmitter();
    comp.onUpload.subscribe((actual) => {
      expect(actual).toContain(tree);
    });
    comp.setInitialTree();
	});
});
