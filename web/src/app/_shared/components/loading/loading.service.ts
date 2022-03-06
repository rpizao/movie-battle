import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingComponent } from '../loading/loading.components';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  private modalRef: NgbModalRef;

  constructor(private modalService: NgbModal) {}

  public loading(){
    if(this.modalRef) this.unloading();
    this.modalRef = this.modalService.open(LoadingComponent, { size: 'lg' });
    return this.modalRef.result;
  }

  public unloading(){
    if(this.modalRef.componentInstance) this.modalRef.close();
  }

}
