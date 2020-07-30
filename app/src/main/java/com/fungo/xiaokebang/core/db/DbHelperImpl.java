package com.fungo.xiaokebang.core.db;



import com.fungo.xiaokebang.app.ItTechApplication;
import com.fungo.xiaokebang.core.dao.DaoSession;
import com.fungo.xiaokebang.core.dao.PPTHistoryData;
import com.fungo.xiaokebang.core.dao.PPTHistoryDataDao;

import java.util.List;

import javax.inject.Inject;



/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/20.
 */
public class DbHelperImpl implements DbHelper {

    private static final int HISTORY_LIST_SIZE = 10;
    private DaoSession daoSession;
    private List<PPTHistoryData> historyDataList;
    private String data;  //每次添加的数据
    private PPTHistoryData historyData;
    private String title;

    @Inject
    public DbHelperImpl() {
        daoSession = ItTechApplication.getInstance().getDaoSession();
    }


    //保存时循环添加
    @Override
    public List<PPTHistoryData> addPreDataData(String data, String title) {
        this.data = data;
        this.title = title;
        createPreData();
        getHisDao().insert(historyData);
        getAllPreData();  //
        return historyDataList;
    }

    //加入的数据
    private void createPreData() {
        historyData = new PPTHistoryData();
        historyData.setDate(System.currentTimeMillis());//添加时间
        historyData.setData(data); //添加单个predata
        historyData.setTitle(title);
    }

    private void getAllPreData() {
        historyDataList = getHisDao().loadAll();
    }

    @Override
    public void clearHistoryData() {
        daoSession.getPPTHistoryDataDao().deleteAll();
    }


    @Override
    public List<PPTHistoryData> loadAllHisPreDatas() {
        return daoSession.getPPTHistoryDataDao().loadAll();
    }

    @Override
    public List<PPTHistoryData> deleteHistoryDataByTitle(String title) {
        List<PPTHistoryData> pptHistoryData = loadAllHisPreDatas();
        for (int i = 0; i < pptHistoryData.size(); i++) {
            if (pptHistoryData.get(i).getTitle().equals(title)) {

                //删除记录
                daoSession.getPPTHistoryDataDao().delete(pptHistoryData.get(i));
            }
        }
        return daoSession.getPPTHistoryDataDao().loadAll();
    }

    @Override
    public List<PPTHistoryData> renameHistoryTitle(String oldTitle, String newTitle) {
        List<PPTHistoryData> pptHistoryData = loadAllHisPreDatas();
        for (int i = 0; i < pptHistoryData.size(); i++) {
            if (pptHistoryData.get(i).getTitle().equals(oldTitle)) {
                //删除记录
                PPTHistoryData renamePPT = pptHistoryData.get(i);
                renamePPT.setTitle(newTitle);
                daoSession.getPPTHistoryDataDao().update(renamePPT);
            }
        }
        return daoSession.getPPTHistoryDataDao().loadAll();
    }

    //加载单个ppt 通过标题
    @Override
    public PPTHistoryData loadPPTDatasByTitle(String title) {
        List<PPTHistoryData> pptHistoryDataList = loadAllHisPreDatas();
        for (PPTHistoryData pptHistoryData : pptHistoryDataList) {
            if (pptHistoryData.getTitle().equals(title)) {
                //
                return pptHistoryData;
            }
        }
        return null;
    }



    private PPTHistoryDataDao getHisDao(){
        return daoSession.getPPTHistoryDataDao();
    }

}
