import { DateTime } from 'luxon';

export class DateHelper {

  static readonly DATE_FORMAT = "yyyy-MM-dd HH:mm";

  static now(): Date {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return today;
  }

  static parse(value: string): Date {
    const valueDate = new Date(value + "T00:00:00");
    valueDate.setHours(0, 0, 0, 0);
    return valueDate;
  }

  static nowString(): string {
    const today = DateHelper.now();
    return today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + (today.getDate() < 10 ? ("0" + today.getDate()) : today.getDate());
  }

  static isDatePass(date: string, min: number): boolean {
    const dateReference = DateTime.fromFormat(date, DateHelper.DATE_FORMAT).plus({minutes: min});
    const now = DateTime.now();
    return dateReference < now;
  }
}
