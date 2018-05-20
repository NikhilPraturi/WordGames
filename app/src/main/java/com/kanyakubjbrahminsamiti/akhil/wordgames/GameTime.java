package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.os.CountDownTimer;

/**
 * Created by akhil on 24-01-2018.
 */

public class GameTime extends CountDownTimer {

private ChatActivity chatActivity;
    public GameTime(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setSourceActivity(ChatActivity chatActivity)
    {
        this.chatActivity=chatActivity;
    }

    @Override
    public void onTick(long l) {
if(this.chatActivity!=null)
{
    this.chatActivity.onCountDownTimerTickEvent(l);
}
    }

    @Override
    public void onFinish() {
if(this.chatActivity!=null)
{
    this.chatActivity.onCountDownTimerFinishEvent();
}
    }
}
