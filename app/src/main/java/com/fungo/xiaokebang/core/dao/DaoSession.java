package com.fungo.xiaokebang.core.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.fungo.xiaokebang.core.dao.PPTHistoryData;

import com.fungo.xiaokebang.core.dao.PPTHistoryDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig pPTHistoryDataDaoConfig;

    private final PPTHistoryDataDao pPTHistoryDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        pPTHistoryDataDaoConfig = daoConfigMap.get(PPTHistoryDataDao.class).clone();
        pPTHistoryDataDaoConfig.initIdentityScope(type);

        pPTHistoryDataDao = new PPTHistoryDataDao(pPTHistoryDataDaoConfig, this);

        registerDao(PPTHistoryData.class, pPTHistoryDataDao);
    }
    
    public void clear() {
        pPTHistoryDataDaoConfig.clearIdentityScope();
    }

    public PPTHistoryDataDao getPPTHistoryDataDao() {
        return pPTHistoryDataDao;
    }

}
