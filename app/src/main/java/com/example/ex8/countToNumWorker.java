package com.example.ex8;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static java.lang.Math.sqrt;

public class countToNumWorker extends Worker{
    private static final String PROGRESS = "PROGRESS";
    private static final long DELAY = 100L;
    Data.Builder d = new Data.Builder();
    public countToNumWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
    }
    @NonNull
    @Override
    public Result doWork() {
        long num = getInputData().getLong("number",0);
        long sqr = 0;
        if (num!=0){
            sqr = (long) sqrt(num);
        }
        int prev_calc = RootApp.getInstance().loadFromSp(String.valueOf(num));
        for (int i=prev_calc; i<sqr+1;i++){
            try {
                setProgressAsync(d.putInt(PROGRESS, i).build());
                Thread.sleep(DELAY);
                if (i%100000 == 0){
                    RootApp.getInstance().saveInSp(String.valueOf(num),i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (num%i ==0){
                Data data=new Data.Builder().putLong("root1",i)
                .putLong("root2",num/i).build();
                setProgressAsync(d.putInt(PROGRESS, (int)num).build());
                return Result.success(data);
            }
        }
        setProgressAsync(d.putInt(PROGRESS, (int)num).build());
        return Result.success();
    }
}
