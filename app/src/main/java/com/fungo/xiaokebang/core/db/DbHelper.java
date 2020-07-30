package com.fungo.xiaokebang.core.db;


import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/20.
 */
public interface DbHelper {


    //根据标题添加一个记录
    List<PPTHistoryData> addPreDataData(String data, String title);

    void clearHistoryData();

    List<PPTHistoryData> loadAllHisPreDatas();


    //根据标题删除 记录
    List<PPTHistoryData> deleteHistoryDataByTitle(String title);

    //重命名
    List<PPTHistoryData> renameHistoryTitle(String oldTitle, String newTitle);


    PPTHistoryData loadPPTDatasByTitle(String title);

}
