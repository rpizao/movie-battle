import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoadingComponent } from '../loading/loading.components';
import { MessageDialogComponent } from './message-dialog.component';

@Injectable()
export class MessageDialogService {

  constructor(private modalService: NgbModal) { }

  public confirm(
    title: string,
    message: string,
    btnOkText: string = 'OK',
    btnCancelText: string = 'Cancelar',
    dialogSize: 'sm'|'lg' = 'sm'): Promise<boolean> {

    const modalRef = this.modalService.open(MessageDialogComponent, { size: dialogSize });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnOkText = btnOkText;
    modalRef.componentInstance.btnCancelText = btnCancelText;
    return modalRef.result;
  }

}
