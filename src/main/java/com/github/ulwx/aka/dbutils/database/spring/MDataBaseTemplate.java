package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.*;
import com.github.ulwx.aka.dbutils.database.MDMethods.One2ManyMapNestOptions;
import com.github.ulwx.aka.dbutils.database.MDMethods.One2OneMapNestOptions;
import com.github.ulwx.aka.dbutils.database.dialect.DBMS;
import com.github.ulwx.aka.dbutils.tool.PageBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public  class MDataBaseTemplate extends MDataBaseTemplateUnSupported implements MDataBase {
	private MDataBaseFactory dataBaseFactory;
	private final MDataBaseImpl mDataBaseProxy;

	public MDataBaseTemplate(MDataBaseFactory dataBaseFactory){
		this.dataBaseFactory=dataBaseFactory;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(MDataBaseImpl.class);
		enhancer.setCallback(new MDataBaseMethodInterceptor());
		mDataBaseProxy = (MDataBaseImpl) enhancer.create();
	}

	@Override
	public DataBaseSet queryForResultSet(String mdFullMethodName, Map<String, Object> args, int page, int perPage, PageBean pageBean, String countSqlMdFullMethodName) throws DbException {
		return mDataBaseProxy.queryForResultSet(mdFullMethodName, args, page, perPage, pageBean, countSqlMdFullMethodName);
	}

	@Override
	public DataBaseSet queryForResultSet(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.queryForResultSet(mdFullMethodName, args);
	}

	@Override
	public <T> List<T> queryList(String mdFullMethodName, Map<String, Object> args, int page, int perPage, PageBean pageBean, RowMapper<T> rowMapper, String countSqlMdFullMethodName) throws DbException {
		return mDataBaseProxy.queryList(mdFullMethodName, args, page, perPage, pageBean, rowMapper, countSqlMdFullMethodName);
	}

	@Override
	public List<Map<String, Object>> queryMap(String mdFullMethodName, Map<String, Object> args, int page, int perPage, PageBean pageBean, String countSqlMdFullMethodName) throws DbException {
		return mDataBaseProxy.queryMap(mdFullMethodName, args, page, perPage, pageBean, countSqlMdFullMethodName);
	}

	@Override
	public <T> List<T> queryList(Class<T> clazz, String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.queryList(clazz, mdFullMethodName, args);
	}

	@Override
	public <T> T queryOne(Class<T> clazz, String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.queryOne(clazz, mdFullMethodName, args);
	}

	@Override
	public <T> List<T> queryList(Class<T> clazz, String mdFullMethodName, Map<String, Object> args, int page, int perPage, PageBean pageBean, String countSqlMdFullMethodName) throws DbException {
		return mDataBaseProxy.queryList(clazz, mdFullMethodName, args, page, perPage, pageBean, countSqlMdFullMethodName);
	}
	@Override
	public <T> List<T> queryListOne2One(Class<T> clazz, String mdFullMethodName, Map<String, Object> args, One2OneMapNestOptions one2OneMapNestOptions) throws DbException {
		return mDataBaseProxy.queryListOne2One(clazz, mdFullMethodName, args, one2OneMapNestOptions);
	}
	@Override
	public <T> List<T> queryListOne2One(Class<T> clazz, String mdFullMethodName, Map<String, Object> args, One2OneMapNestOptions one2OneMapNestOptions, int page, int perPage, PageBean pageBean, String countSqlMdFullMethodName) throws DbException {
		return mDataBaseProxy.queryListOne2One(clazz, mdFullMethodName, args, one2OneMapNestOptions, page, perPage, pageBean, countSqlMdFullMethodName);
	}

	@Override
	public <T> List<T> queryListOne2Many(Class<T> clazz, String mdFullMethodName, Map<String, Object> args, One2ManyMapNestOptions one2ManyMapNestOptions) throws DbException {
		return mDataBaseProxy.queryListOne2Many(clazz, mdFullMethodName, args, one2ManyMapNestOptions);
	}

	@Override
	public <T> List<T> queryList(String mdFullMethodName, Map<String, Object> args, RowMapper<T> rowMapper) throws DbException {
		return mDataBaseProxy.queryList(mdFullMethodName, args, rowMapper);
	}

	@Override
	public List<Map<String, Object>> queryMap(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.queryMap(mdFullMethodName, args);
	}

	@Override
	public int del(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.del(mdFullMethodName, args);
	}

	@Override
	public int update(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.update(mdFullMethodName, args);
	}

	@Override
	public void callStoredPro(String mdFullMethodName, Map<String, Object> parms, Map<String, Object> outPramsValues, List<DataBaseSet> returnDataBaseSets) throws DbException {
		 mDataBaseProxy.callStoredPro(mdFullMethodName, parms, outPramsValues, returnDataBaseSets);
	}

	@Override
	public int insert(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.insert(mdFullMethodName, args);
	}

	@Override
	public long insertReturnKey(String mdFullMethodName, Map<String, Object> args) throws DbException {
		return mDataBaseProxy.insertReturnKey(mdFullMethodName, args);
	}

	@Override
	public int[] update(String[] mdFullMethodNameList, Map<String, Object>[] vParametersArray) throws DbException {
		return mDataBaseProxy.update(mdFullMethodNameList, vParametersArray);
	}

	@Override
	public int[] insert(String[] mdFullMethodNameList, Map<String, Object>[] vParametersArray) throws DbException {
		return mDataBaseProxy.insert(mdFullMethodNameList, vParametersArray);
	}

	@Override
	public int[] update(String mdFullMethodName, List<Map<String, Object>> vParametersList) throws DbException {
		return mDataBaseProxy.update(mdFullMethodName, vParametersList);
	}

	@Override
	public int[] insert(String mdFullMethodName, List<Map<String, Object>> vParametersList) throws DbException {
		return mDataBaseProxy.insert(mdFullMethodName, vParametersList);
	}

	@Override
	public int[] update(ArrayList<String> mdFullMethodNameList) throws DbException {
		return mDataBaseProxy.update(mdFullMethodNameList);
	}

	@Override
	public int[] insert(ArrayList<String> mdFullMethodNameList) throws DbException {
		return mDataBaseProxy.insert(mdFullMethodNameList);
	}



	@Override
	public <T> int insertBy(T insertObject) throws DbException {
		return mDataBaseProxy.insertBy(insertObject);
	}

	@Override
	public <T> long insertReturnKeyBy(T insertObject) throws DbException {
		return mDataBaseProxy.insertReturnKeyBy(insertObject);
	}

	@Override
	public <T> int[] insertBy(T[] objs) throws DbException {
		return mDataBaseProxy.insertBy(objs);
	}

	@Override
	public <T> List<T> queryListBy(T selectObject) throws DbException {
		return mDataBaseProxy.queryListBy(selectObject);
	}

	@Override
	public <T> List<T> queryListBy(T selectObject, int page, int perPage, PageBean pb) throws DbException {
		return mDataBaseProxy.queryListBy(selectObject, page, perPage, pb);
	}

	@Override
	public <T> T queryOneBy(T selectObject) throws DbException {
		return mDataBaseProxy.queryOneBy(selectObject);
	}


	@Override
	public String exeScriptInDir(String dirFilePath,
								 String sqlFileName,
								 boolean throwWarning,
								 boolean continueIfError,
								 String delimiters,
								 String encoding) throws DbException {

		return mDataBaseProxy.exeScriptInDir(dirFilePath, sqlFileName, throwWarning,continueIfError,
				delimiters,encoding);
	}

	@Override
	public String exeScript(String packageFullName, String sqlFileName,
							boolean throwWarning,boolean continueIfError,String delimiters,
							String encoding) throws DbException {
		return mDataBaseProxy.exeScript(packageFullName, sqlFileName, throwWarning,continueIfError,delimiters,encoding);
	}


	@Override
	public String exeScript(String mdFullMethodName,
							boolean throwWarning,String delimiters,
							Map<String, Object> args) throws DbException {
		return mDataBaseProxy.exeScript(mdFullMethodName,throwWarning, delimiters, args);
	}

	@Override
	public <T> T getMapper(Class<T> type) throws DbException {
		return mDataBaseProxy.getMapper(type);
	}

	/**
	 * 不支持的方法
	 * @throws DbException
	 */
	@Override
	public void close() {
	}

	@Override
	public <T> int insertBy(T insertObject, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertBy(insertObject, includeNull);
	}

	@Override
	public <T> int insertBy(T insertObject, Object[] insertProperties) throws DbException {
		return mDataBaseProxy.insertBy(insertObject, insertProperties);
	}

	@Override
	public <T> int insertBy(T insertObject, Object[] insertProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertBy(insertObject, insertProperties, includeNull);
	}

	@Override
	public <T> int[] insertBy(T[] objs, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertBy(objs, includeNull);
	}

	@Override
	public <T> int[] insertBy(T[] objs, Object[] insertProperties) throws DbException {
		return mDataBaseProxy.insertBy(objs, insertProperties);
	}

	@Override
	public <T> int[] insertBy(T[] objs, Object[] insertProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertBy(objs, insertProperties, includeNull);
	}

	@Override
	public <T> long insertReturnKeyBy(T insertObject, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertReturnKeyBy(insertObject, includeNull);
	}

	@Override
	public <T> long insertReturnKeyBy(T insertObject, Object[] insertProperties) throws DbException {
		return mDataBaseProxy.insertReturnKeyBy(insertObject, insertProperties);
	}

	@Override
	public <T> long insertReturnKeyBy(T insertObject, Object[] insertProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.insertReturnKeyBy(insertObject, insertProperties, includeNull);
	}

	@Override
	public <T> int updateBy(T updateObject, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.updateBy(updateObject, whereProperteis);
	}

	@Override
	public <T> int updateBy(T updateObject, Object[] whereProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.updateBy(updateObject, whereProperties, includeNull);
	}

	@Override
	public <T> int updateBy(T updateObject, Object[] whereProperteis, Object[] updateProperties) throws DbException {
		return mDataBaseProxy.updateBy(updateObject, whereProperteis, updateProperties);
	}

	@Override
	public <T> int updateBy(T updateObject, Object[] whereProperties, Object[] updateProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.updateBy(updateObject, whereProperties, updateProperties, includeNull);
	}

	@Override
	public <T> int[] updateBy(T[] updateObjects, Object[] whereProperteis, Object[] updateProperties) throws DbException {
		return mDataBaseProxy.updateBy(updateObjects, whereProperteis, updateProperties);
	}

	@Override
	public <T> int[] updateBy(T[] updateObjects, Object[] whereProperties, Object[] updateProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.updateBy(updateObjects, whereProperties, updateProperties, includeNull);
	}

	@Override
	public <T> int[] updateBy(T[] updateObjects, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.updateBy(updateObjects, whereProperteis);
	}

	@Override
	public <T> int[] updateBy(T[] updateObjects, Object[] whereProperties, boolean includeNull) throws DbException {
		return mDataBaseProxy.updateBy(updateObjects, whereProperties, includeNull);
	}

	@Override
	public <T> T queryOneBy(T selectObject, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.queryOneBy(selectObject, whereProperteis);
	}

	@Override
	public <T> List<T> queryListBy(T selectObject, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.queryListBy(selectObject, whereProperteis);
	}

	@Override
	public <T> List<T> queryListBy(T selectObject, Object[] whereProperteis, int page, int perPage, PageBean pb) throws DbException {
		return mDataBaseProxy.queryListBy(selectObject, whereProperteis, page, perPage, pb);
	}

	@Override
	public <T> int delBy(T deleteObject, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.delBy(deleteObject, whereProperteis);
	}

	@Override
	public <T> int[] delBy(T[] deleteObjects, Object[] whereProperteis) throws DbException {
		return mDataBaseProxy.delBy(deleteObjects, whereProperteis);
	}
	@Override
	public DBMS getDataBaseType() {
		return mDataBaseProxy.getDataBaseType();
	}

	private  static Set<String> notInterceptorMethods=new HashSet<>();
	static{
		for(Method method:MDataBaseTemplateUnSupported.class.getMethods()){
			if(method.getDeclaringClass()==Object.class){
				continue;
			}
			notInterceptorMethods.add(method.getName());
		}
	}
	public  class MDataBaseMethodInterceptor implements MethodInterceptor {


		@Override
		public Object intercept(Object obj,// CGLib动态生成的代理对象
				Method method,//被代理的方法
				Object[] args, //传入的参数
				MethodProxy proxy //代理方法
			) throws Throwable {

			AkaDBTemplateExecuteInfo akaDBTemplateExecuteInfo=new AkaDBTemplateExecuteInfo();
			akaDBTemplateExecuteInfo.setMethod(method);
			akaDBTemplateExecuteInfo.setTransactional(TransactionSynchronizationManager.isSynchronizationActive());
			AkaMDataBaseTemplateExecuteInfoHolder.put(akaDBTemplateExecuteInfo);

			if(method.getName().equals("getMapper")){//getDataBase()
				return MapperFactory.getMapper((Class)args[0],(MDataBase)obj);
			}
			MDataBase mDataBase=null;
			try {
				if(notInterceptorMethods.contains(method.getName())){
					throw new  UnsupportedOperationException();
				}

				DBTransInfo dbTransInfo=new  DBTransInfo();
				dbTransInfo.setmDataBaseFactory(MDataBaseTemplate.this.dataBaseFactory);
				dbTransInfo.setmDataBaseTemplate(MDataBaseTemplate.this);
				DbContext.setDbTransInfo(dbTransInfo);

				mDataBase=MDataBaseUtils.getMDataBase(MDataBaseTemplate.this.dataBaseFactory);
				//调用原始方法
				Object result =method.invoke(mDataBase, args);
				if (!MDataBaseUtils.isSqlSessionTransactional(mDataBase, MDataBaseTemplate.this.dataBaseFactory)) {
					if (!mDataBase.isColsed() && !mDataBase.getAutoCommit()) {
						mDataBase.commit();
					}
				}
				return result;

			}catch(Throwable t) {
				if(t instanceof InvocationTargetException){
					InvocationTargetException invocationTargetException=(InvocationTargetException)t;
					throw invocationTargetException.getTargetException();
				}
				throw t;
			}finally {
				if(mDataBase!=null) {
					MDataBaseUtils.closeMDataBase(mDataBase, MDataBaseTemplate.this.dataBaseFactory);
				}
				DbContext.clearDbTransInfo();
				AkaMDataBaseTemplateExecuteInfoHolder.put(null);
			}
		}
	};

}
