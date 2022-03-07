import { Option } from "./option";

export interface Question {
  first: Option;
  second: Option;
  correct: boolean;
}
