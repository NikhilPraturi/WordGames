package com.kanyakubjbrahminsamiti.akhil.wordgames;

import android.os.CountDownTimer;

/**
 * Created by akhil on 31-01-2018.
 */

public class GameTimeTwo  extends  CountDownTimer{

    private SoloGameModuleTwo soloGameModuleTwo;
    public GameTimeTwo(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setSourceActivity(SoloGameModuleTwo activity)
    {
        this.soloGameModuleTwo=activity;
    }

    @Override
    public void onTick(long l) {
        if(this.soloGameModuleTwo!=null)
        {
            this.soloGameModuleTwo.onCountDownTimerTickEvent(l);
        }
    }

    @Override
    public void onFinish() {
        if(this.soloGameModuleTwo!=null)
        {
            this.soloGameModuleTwo.onCountDownTimerFinishEvent();
        }
    }
}
