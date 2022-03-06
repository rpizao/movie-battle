import { FormControl, AbstractControl, FormGroup, FormArray } from '@angular/forms';

export function CheckRequiredField(field: AbstractControl): boolean {
    return (!field.valid && (field.dirty || field.touched));
}

export class FormHelper {

  static isValid(form: FormGroup): boolean {
    form.markAllAsTouched();
    this.validateAllFields(form);
    form.updateValueAndValidity({ onlySelf: true });
    return form.valid;
  }

  private static validateAllFields(formGroup: FormGroup | FormArray) {
    Object.keys(formGroup.controls).forEach(field => {
        const control = formGroup.get(field);
        if (control instanceof FormGroup || control instanceof FormArray) {
            this.validateAllFields(control);
        }
        control.updateValueAndValidity({ onlySelf: true });
    });
  }
}
