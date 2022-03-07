import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { error } from 'protractor';
import { Battle } from 'src/models/battle';
import { Option } from 'src/models/option';
import { Question } from 'src/models/question';
import { ScoreResult } from 'src/models/score.result';
import { BattleService } from 'src/services/battle.service';
import { AuthService } from '../_auth/services/auth.service';

@Component({
  selector: 'app-battle',
  templateUrl: './battle.component.html',
  styleUrls: ['./battle.component.scss']
})
export class BattleComponent implements OnInit {

  private _battle: Battle;
  private _state: "HOME" | "QUIZ" | "FINISH" = "HOME";
  showAnswer: boolean = false;
  correct: boolean = false;
  private _totalErrors: number = 0;
  private _totalHits: number = 0;
  private _errorsLimit: number = 3;
  private _ended: boolean = false;

  private _position: number = 0;

  public scores: ScoreResult[];

  constructor(private battleService: BattleService, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.listScores();
  }

  get question() {
    return this._battle?.question;
  }

  start(){
    this.searchBattle();
    this.battleService.start(r => {
      this.finishBattle(r);
    }, error => this.clear())
  }

  next(){
    this.searchBattle();
    this.battleService.next(this._battle.gameCode, r => {
      this.finishBattle(r);
    }, error => this.clear())
  }

  restart(){
    this.clear();
    this.start();
  }

  private searchBattle() {
    this._position = 0;
    this.showAnswer = false;
    this._state = 'QUIZ';
  }

  private finishBattle(r: Battle) {
    this._battle = r;
    this._state = 'FINISH';
  }

  private clear() {
    this._state = "HOME";
    this._totalErrors = 0;
    this._totalHits = 0;
    this._ended = false;
  }

  selectedAnswer(position: number){
    if(this.showAnswer) return;
    this._position = position;

    this.battleService.answer(this._battle.gameCode, position, questionUpdated => {
      this._battle.question = questionUpdated;
      this.showAnswer = true;

      const correct = this.answerIsCorrect();
      if(correct) this._totalHits++;
      else this._totalErrors++;

      if(this._totalErrors == this._errorsLimit) this.finishGame();
    }, error => this._position = 0);
  }

  get position() {
    return this._position;
  }

  get ended() {
    return this._ended;
  }

  get errorsLimit() {
    return this._errorsLimit;
  }

  get totalErrors() {
    return this._totalErrors;
  }

  get totalHits() {
    return this._totalHits;
  }

  private finishGame(){
    this.battleService.finish(this._battle.gameCode, this._totalHits, r => {
      this._ended = true;
      this._state = 'FINISH';
    }, error => this.clear());
  }

  answerIsCorrect(): boolean{
    return this._battle.question.correct;
  }

  backToHome(){
    this.clear();
    this.listScores();
  }

  private listScores() {
    this.battleService.listScores(r => this.scores = r);
  }

  get stateHome(){
    return !this.ended && this._state == 'HOME';
  }

  get stateQuizInit(){
    return !this.ended && this._state == 'QUIZ';
  }

  get stateFinish(){
    return this._state == 'FINISH';
  }

  showCssQuestionFirst() {
    if(!this.showAnswer || this._position != 1) return "";
    return this.answerIsCorrect() ? 'selected' : 'selected-wrong';
  }

  showCssQuestionSecond() {
    if(!this.showAnswer || this._position != 2) return "";
    return this.answerIsCorrect() ? 'selected' : 'selected-wrong';
  }

  showPictureIfAvaiable(first: Option) {
    if(first?.picture && first?.picture != "N/A") return first.picture;
    return "../assets/no-picture.png";
  }

}
