package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.os.CountDownTimer;

/**
 * Created by akhil on 19-02-2018.
 */

public class GameTimeFive extends CountDownTimer{
    private ChatMplyTwoPlayers fllMultiPlay;
    public GameTimeFive(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setSourceActivity(ChatMplyTwoPlayers activity)
    {
        this.fllMultiPlay=activity;
    }

    @Override
    public void onTick(long l) {
        if(this.fllMultiPlay!=null)
        {
            this.fllMultiPlay.onCountDownTimerTickEvent(l);
        }
    }

    @Override
    public void onFinish() {
        if(this.fllMultiPlay!=null)
        {
            this.fllMultiPlay.onCountDownTimerFinishEvent();
        }
    }

}
