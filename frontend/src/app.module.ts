import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }   from '@angular/http';

import { AppComponent }        from './app/app.component';
import { UploadComponent }     from './upload/upload.component';
import { TreeComponent }     from './tree/tree.component';

import { UPLOAD_DIRECTIVES } from 'ng2-uploader/ng2-uploader';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  declarations: [
    AppComponent,
    UploadComponent,
    TreeComponent,
    UPLOAD_DIRECTIVES
  ],
  providers: [ ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
