package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.os.CountDownTimer;

/**
 * Created by akhil on 08-02-2018.
 */

public class GameTimeThree extends CountDownTimer {

    private ActivityGameTwo multiplayGame;
    public GameTimeThree(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setSourceActivity(ActivityGameTwo activity)
    {
        this.multiplayGame=activity;
    }

    @Override
    public void onTick(long l) {
        if(this.multiplayGame!=null)
        {
         this.multiplayGame.onCountDownTimerTickEvent(l);
        }
    }

    @Override
    public void onFinish() {
        if(this.multiplayGame!=null)
        {
          this.multiplayGame.onCountDownTimerFinishEvent();
        }
    }
}
