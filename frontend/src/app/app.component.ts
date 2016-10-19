import { Component }       from '@angular/core';
import { UploadComponent } from '../upload/upload.component';
import { UPLOAD_DIRECTIVES } from 'ng2-uploader/ng2-uploader';

@Component({
  selector: 'my-app',
  styleUrls: ['src/app/app.component.css'],
  templateUrl: 'src/app/app.component.html'
})


export class AppComponent {
  title = 'VISiBLE';
  uploadFile: any;
  hasBaseDropZoneOver: boolean = false;
  options: Object = {
    url: 'http://localhost:3000'
  };

  handleUpload(data): void {
    if (data && data.response) {
      data = JSON.parse(data.response);
      this.uploadFile = data;
    }
  }

  fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }
}
