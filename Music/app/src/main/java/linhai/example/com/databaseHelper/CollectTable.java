package linhai.example.com.databaseHelper;

import org.litepal.crud.DataSupport;

import linhai.example.com.audio.AudioInfo;

/**
 * Created by linhai on 15/9/14.
 */
public class CollectTable extends DataSupport{
    /***song name*/
    private String name;

    /***position of song listview*/
    private int pos;

    /***song path*/
    private String path;

    /*** belong to mainTable */
    private AudioInfo mainTable;

    public String getName(){
        return name;
    }

    public void setName(String _name){
        name = _name;
    }

    public int getPos(){
        return pos;
    }

    public void setPos(int _pos){
        pos = _pos;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String _path){
        path = _path;
    }

    public void setMainTable(AudioInfo _mainTable){
        mainTable = _mainTable;
    }

    public AudioInfo getMainTable(){
        return mainTable;
    }
}
