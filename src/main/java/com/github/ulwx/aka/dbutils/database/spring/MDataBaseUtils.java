package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.DbContext;
import com.github.ulwx.aka.dbutils.database.DbException;
import com.github.ulwx.aka.dbutils.database.MDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.util.Assert.notNull;

public class MDataBaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(MDataBaseUtils.class);

    public static void closeMDataBase(MDataBase mDataBase, MDataBaseFactory mDataBaseFactory) {
        notNull(mDataBase, "NO MDataBase SPECIFIED");
        notNull(mDataBaseFactory, "NO MDataBaseFactory SPECIFIED");
        DbContext.clearDbTransInfo();
        MDataBaseHolder holder = (MDataBaseHolder) TransactionSynchronizationManager.getResource(mDataBaseFactory);
        if ((holder != null) && (holder.getMDataBase() == mDataBase)) {
            logger.debug( "Releasing transactional MDataBase [" + mDataBase + "]");
            holder.released();

        } else {
            logger.debug( "Closing non transactional MDataBase [" + mDataBase + "]");
            mDataBase.close();
        }


    }

    public static MDataBase getMDataBase(MDataBaseTemplate mDataBaseTemplate, MDataBaseFactory mDataBaseFactory) throws SQLException{

        DBTransInfo dbTransInfo=new  DBTransInfo();
        dbTransInfo.setmDataBaseFactory(mDataBaseFactory);
        dbTransInfo.setmDataBaseTemplate(mDataBaseTemplate);
        DbContext.setDbTransInfo(dbTransInfo);

        MDataBaseHolder holder = (MDataBaseHolder) TransactionSynchronizationManager.getResource(mDataBaseFactory);
        MDataBase mDataBase = mDataBaseHolder(holder);
        if (mDataBase != null) {
            logger.debug("fetch a MDataBase from MDataBaseHolder");
            return mDataBase;
        }

        if(TransactionSynchronizationManager.isSynchronizationActive()){
            logger.debug("Creating a new MDataBase and " + "Currently in a transaction!");
        }else{
            logger.debug("Creating a new MDataBase");
        }
        Connection connection = DataSourceUtils.getConnection(mDataBaseFactory.getDataSource());
        boolean autoCommit = connection.getAutoCommit();
        boolean externalContralConClose=false;
        boolean isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection,
                                 mDataBaseFactory.getDataSource());
        if(isConnectionTransactional){
            externalContralConClose=true;
        }
        logger.debug("a JDBC Connection [" + connection + "] will"
                + (isConnectionTransactional ? " " : " not ") + "be managed by Spring");
        mDataBase=mDataBaseFactory.getDatabase(connection,autoCommit,externalContralConClose);
        registerMDataBaseHolder(mDataBaseFactory, mDataBase);
        return mDataBase;
    }
    public static boolean isSqlSessionTransactional(MDataBase mDataBase, MDataBaseFactory mDataBaseFactory) {
        notNull(mDataBase, "NO MDataBase specified!");
        notNull(mDataBaseFactory, "NO MDataBaseFactory specified!");

        MDataBaseHolder holder = (MDataBaseHolder) TransactionSynchronizationManager.getResource(mDataBaseFactory);

        return (holder != null) && (holder.getMDataBase()== mDataBase);
    }
    private static MDataBase mDataBaseHolder(MDataBaseHolder holder) {
        MDataBase mDataBase = null;
        if (holder != null && holder.isSynchronizedWithTransaction()) {
            holder.requested();
            mDataBase = holder.getMDataBase();
        }
        return mDataBase;
    }

    private static void registerMDataBaseHolder(MDataBaseFactory mDataBaseFactory, MDataBase mDataBase ) {
        MDataBaseHolder holder;
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            holder = new MDataBaseHolder(mDataBase);
            TransactionSynchronizationManager.bindResource(mDataBaseFactory, holder);
            TransactionSynchronizationManager
                    .registerSynchronization(new MDataBaseSynchronization(holder, mDataBaseFactory));
            holder.setSynchronizedWithTransaction(true);
            holder.requested();

        } else {
            logger.debug("MDataBase [" + mDataBase
                    + "] was not registered for synchronization because synchronization is not active");
        }


    }



    private static final class MDataBaseSynchronization implements TransactionSynchronization {
        private static final Logger logger = LoggerFactory.getLogger(MDataBaseSynchronization.class);
        private final MDataBaseHolder holder;

        private final MDataBaseFactory mDataBaseFactory;

        private boolean holderActive = true;

        public MDataBaseSynchronization(MDataBaseHolder holder, MDataBaseFactory mDataBaseFactory) {
            notNull(holder, "Parameter 'holder' must be not null");
            notNull(mDataBaseFactory, "Parameter 'mDataBaseFactory' must be not null");

            this.holder = holder;
            this.mDataBaseFactory = mDataBaseFactory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getOrder() {
            // order right before any Connection synchronization
            return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void suspend() {
            if (this.holderActive) {
                logger.debug( "Transaction synchronization suspending MDataBase [" + this.holder.getMDataBase() + "]");
                TransactionSynchronizationManager.unbindResource(this.mDataBaseFactory);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resume() {
            if (this.holderActive) {
                logger.debug( "Transaction synchronization resuming MDataBase [" + this.holder.getMDataBase() + "]");
                TransactionSynchronizationManager.bindResource(this.mDataBaseFactory, this.holder);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void beforeCommit(boolean readOnly) {
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                try {
                    logger.debug( "Transaction synchronization beforeCommit committing- MDataBase [" + this.holder.getMDataBase() + "]");
                    /////
                } catch (Exception p) {
                    if(p instanceof DbException){
                        DbException dbe=(DbException) p;
                        SQLException se=((DbException) p).getSQLException();
                        if(se!=null){
                            SQLErrorCodeSQLExceptionTranslator ser=new SQLErrorCodeSQLExceptionTranslator(mDataBaseFactory.getDataSource());
                            DataAccessException dae =ser.translate(dbe.getMessage(),null,se);
                            if(dae!=null){
                                throw dae;
                            }else{
                                throw new UncategorizedSQLException(p.getMessage(), null, se);
                            }
                        }
                    }else{
                        throw new TransientDataAccessResourceException("MDataBase commit error!!",p);
                    }


                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void beforeCompletion() {
            // because afterCompletion may be called from a different thread
            if (!this.holder.isOpen()) {
                logger.debug("Transaction synchronization: deregistering MDataBase [" + this.holder.getMDataBase() + "]");
                TransactionSynchronizationManager.unbindResource(mDataBaseFactory);
                this.holderActive = false;
                logger.debug( "Transaction synchronization: release the connection in MDataBase [" + this.holder.getMDataBase() + "]");

                DataSourceUtils.releaseConnection(this.holder.getMDataBase().getConnection(false),
                        mDataBaseFactory.getDataSource());
                boolean conClosed= false;
                try {
                    conClosed = this.holder.getMDataBase().getConnection(false).isClosed();
                } catch (SQLException exception) {

                }
                if(conClosed){
                    logger.debug("the connection in MDataBase is closed");
                }else{
                }

            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void afterCompletion(int status) {
            if (this.holderActive) {
                // afterCompletion may have been called from a different thread
                // so avoid failing if there is nothing in this one
                logger.debug("Transaction synchronization deregistering MDataBase [" + this.holder.getMDataBase() + "]");
                TransactionSynchronizationManager.unbindResourceIfPossible(mDataBaseFactory);
                this.holderActive = false;
                logger.debug( "Transaction synchronization closing MDataBase connection[" + this.holder.getMDataBase() + "]");
                DataSourceUtils.releaseConnection(this.holder.getMDataBase().getConnection(false),
                        mDataBaseFactory.getDataSource());

                boolean conClosed= false;
                try {
                    conClosed = this.holder.getMDataBase().getConnection(false).isClosed();
                } catch (SQLException exception) {

                }
                if(conClosed){
                    logger.debug("the connection in MDataBase is closed");
                }else{
                }
            }
            this.holder.reset();
        }
    }
}
