import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthService } from "src/app/_auth/services/auth.service";
import { AlertService } from "src/app/_shared/components/alert/alert.service";
import { LoadingService } from "src/app/_shared/components/loading/loading.service";
import { Battle } from "src/models/battle";
import { Question } from "src/models/question";
import { ScoreResult } from "src/models/score.result";
import { GenericHttp } from "./generic-http";

@Injectable({
  providedIn: "root"
})
export class BattleService extends GenericHttp {

  constructor(client: HttpClient, spinner: LoadingService, alert: AlertService, private authService: AuthService){
    super(client, spinner, alert);
    this.useLoading = false;
  }

  start(result: (battle: Battle) => void, error: (err:any) => void) {
    this.get("/battle/" + this.authService.getUserData().code + "/start", result, error);
  }

  next(gameCode: string, result: (battle: Battle) => void, error: (err:any) => void) {
    this.get("/battle/" + gameCode + "/next", result, error);
  }

  answer(gameCode: string, selectedPosition: number, result: (question: Question) => void, error?: (err:any) => void) {
    this.put("/battle/" + gameCode + "/answer/" + selectedPosition, {}, result, error);
  }

  finish(gameCode: string, result: (battle: Battle) => void, error: (err:any) => void) {
    this.put("/battle/" + gameCode + "/finish", {}, result, error);
  }

  listScores(result: (scores: ScoreResult[]) => void, error?: (err:any) => void) {
    this.get("/battle/scores", result, error);
  }

}
