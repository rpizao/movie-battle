import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Battle } from 'src/models/battle';
import { Question } from 'src/models/question';
import { BattleService } from 'src/services/battle.service';
import { AuthService } from '../_auth/services/auth.service';

@Component({
  selector: 'app-battle',
  templateUrl: './battle.component.html',
  styleUrls: ['./battle.component.scss']
})
export class BattleComponent implements OnInit {

  private _battle: Battle;
  state: "OFF" | "INIT" | "FINISH" = "OFF";
  showAnswer: boolean = false;
  correct: boolean = false;
  private _totalErrors: number = 0;
  private _totalHits: number = 0;
  private _errorsLimit: number = 3;
  private _ended: boolean = false;

  private _position: number = 0;

  constructor(private battleService: BattleService, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void { }

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
    this.state = 'INIT';
  }

  private finishBattle(r: Battle) {
    this._battle = r;
    this.state = 'FINISH';
  }

  private clear() {
    this.state = "OFF";
    this._totalErrors = 0;
    this._totalHits = 0;
    this._ended = false;
  }

  selected(position: number){
    this._position = position;
    this.showAnswer = true;

    const correct = this.answerIsCorrect();
    if(correct) this._totalHits++;
    else this._totalErrors++;

    if(this._totalErrors == this._errorsLimit) this.finishGame();
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
    this._ended = true;
    this.battleService.finish(this._battle.gameCode, this._totalHits, r => {}, error => this.clear());
  }

  answerIsCorrect(): boolean{
    let selectedOption = this.position == 1 ? this.question.first : this.question.second;
    return this.question.first.evaluation <= selectedOption.evaluation && this.question.second.evaluation <= selectedOption.evaluation;
  }

}
