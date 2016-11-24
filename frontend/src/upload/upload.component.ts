import { Component } from '@angular/core';
import { UPLOAD_DIRECTIVES } from 'ng2-uploader/ng2-uploader';

const URL = 'localhost:5000/upload';

@Component({
  selector: 'upload',
  styleUrls: ['src/upload/upload.component.css'],
  templateUrl: 'src/upload/upload.component.html'
})
export class UploadComponent {
  uploadFile: any;
  hasBaseDropZoneOver: boolean = false;
  filename: string;
  uploadSuccess: boolean = false;
  response: string;
  options: Object = {
    url: 'http://localhost:5000/upload'
  };

  handleUpload(data): void {
    if (data && data.response) {
      this.filename = data.originalName;
      data = JSON.parse(data.response);
      this.response = data.code.replace(/\n/g, "<br>");
      this.uploadSuccess = true;
      this.uploadFile = data;
    }
  }

  fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }
}
