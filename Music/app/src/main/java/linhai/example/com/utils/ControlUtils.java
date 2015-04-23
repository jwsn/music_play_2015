package linhai.example.com.utils;

import android.util.Log;

import java.util.List;
import java.util.Random;

import linhai.example.com.audio.AudioInfo;
import linhai.example.com.constant.GlobalConstant;

/**
 * Created by linhai on 15/4/23.
 */
public class ControlUtils {
    private static final String TAG = "ControlUtils";

    private static ControlUtils instance = new ControlUtils();

    /*** control flag ***/
    public static boolean bPlayingFlag = false;
    public static boolean bPauseFlag = true;
    public static boolean bFirstTimePlayFlag = true;
    public static int playMode = GlobalConstant.NORMAL_PLAY_MODE;

    public static int curMusicPos = 0;
    public static int curCollPos = 0;
    public static int curHisPos = 0;


    private ControlUtils(){}

    public static ControlUtils getInstance(){
        return instance;
    }

    /*** set the pos when play prev ***/
    public int setCurrentPlayPositionWhenPlayPre(int curPos, List<AudioInfo> audioInfoList){
        Log.d(TAG, "setCurrentPlayPositionWhenPlayPre");

        switch(ControlUtils.playMode){
            case GlobalConstant.NORMAL_PLAY_MODE:{
                curPos--;
            }
            break;
            case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
                curPos--;
                if(curPos < 0){
                    curPos = audioInfoList.size() - 1;
                }
            }
            break;
            case GlobalConstant.RANDOM_PLAY_MODE:{
                Random rand = new Random();
                int randPos = rand.nextInt(audioInfoList.size());
                curPos = randPos % (audioInfoList.size());
            }
            break;
            case GlobalConstant.REPEAT_ONE_PLAY_MODE:
            default:
            break;
        }
        return curPos;
    }

    /*** set the pos when play next ***/
    public int setCurrentPlayPositionWhenPlayNext(int curPos, List<AudioInfo> audioInfoList){
        Log.d(TAG, "setCurrentPlayPositionWhenPlayNext");

        switch(ControlUtils.playMode){
            case GlobalConstant.NORMAL_PLAY_MODE:{
                curPos++;
            }
            break;
            case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
                curPos++;
                if(curPos > audioInfoList.size() - 1){
                    curPos = 0;
                }
            }
            break;
            case GlobalConstant.RANDOM_PLAY_MODE:{
                Random rand = new Random();
                int randPos = rand.nextInt(audioInfoList.size());
                curPos = randPos % (audioInfoList.size());
            }
            break;
            case GlobalConstant.REPEAT_ONE_PLAY_MODE:
            default:
                break;
        }
        return curPos;
    }
}
