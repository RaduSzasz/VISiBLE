import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }   from '@angular/http';

import { AppComponent }        from './app/app.component';
import { TreeComponent }     from './tree/tree.component';

import { TreeService } from './tree/tree.service';
import { ApiService } from './shared/api.service';

import { Ng2UploaderModule } from 'ng2-uploader/ng2-uploader';
import { ModalModule } from 'ng2-bootstrap';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    Ng2UploaderModule,
    ModalModule.forRoot()
  ],
  declarations: [
    AppComponent,
    TreeComponent,
  ],
  providers: [ 
    TreeService,
    ApiService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
