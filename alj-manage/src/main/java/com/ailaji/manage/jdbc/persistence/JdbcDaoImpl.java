package com.ailaji.manage.jdbc.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.ailaji.manage.jdbc.persistence.utils.ClassUtils;
import com.ailaji.manage.jdbc.persistence.utils.NameUtils;


/**
 * jdbc操作dao
 */
@SuppressWarnings("unchecked")
public class JdbcDaoImpl implements JdbcDao {

	private static final Logger LOG    = Logger.getLogger(JdbcDaoImpl.class);
	
    /** spring jdbcTemplate 对象 */
	@Autowired
    protected JdbcOperations jdbcTemplate;

    /** 名称处理器，为空按默认执行 */
    protected NameHandler    nameHandler;

    /** rowMapper，为空按默认执行 */
    protected String         rowMapperClass;

    /** 数据库方言 */
    protected String         dialect;

    /**
     * 插入数据
     *
     * @param entity the entity
     * @param criteria the criteria
     * @return long long
     */
    private Long insert(Object entity, Criteria criteria) {
        Class<?> entityClass = SqlUtils.getEntityClass(entity, criteria);
        NameHandler handler = this.getNameHandler();
        String pkValue = handler.getPKValue(entityClass, this.dialect);
        if (StringUtils.isNotBlank(pkValue)) {
            String primaryName = handler.getPKName(entityClass);
            if (criteria == null) {
                criteria = Criteria.create(entityClass);
            }
            criteria.setPKValueName(NameUtils.getCamelName(primaryName), pkValue);
        }
        final BoundSql boundSql = SqlUtils.buildInsertSql(entity, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(boundSql.getSql(),
                    new String[] { boundSql.getPrimaryKey() });
                int index = 0;
                for (Object param : boundSql.getParams()) {
                    index++;
                    ps.setObject(index, param);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Long insert(Object entity) {
        return this.insert(entity, null);
    }

    public Long insert(Criteria criteria) {
        return this.insert(null, criteria);
    }

    public void save(Object entity) {
        final BoundSql boundSql = SqlUtils.buildInsertSql(entity, null, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }
    
    public void save(Object entity, String tableFix) {
    	final BoundSql boundSql = SqlUtils.buildInsertSql(entity, null, this.getNameHandler());
    	if(LOG.isDebugEnabled())
    	{
    		LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
    	}
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(entity.getClass(), null);
    		executeSql = executeSql.replace(String.format("INSERT INTO %s", tableName), String.format("INSERT INTO %s_%s", tableName, tableFix));
    	}
    	jdbcTemplate.update(executeSql, boundSql.getParams().toArray());
    }

    public void save(Criteria criteria) {
        final BoundSql boundSql = SqlUtils.buildInsertSql(null, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    public int update(Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildUpdateSql(null, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }
    
    public int update(Criteria criteria, String tableFix) {
    	BoundSql boundSql = SqlUtils.buildUpdateSql(null, criteria, this.getNameHandler());
    	if(LOG.isDebugEnabled())
    	{
    		LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
    	}
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(criteria.getEntityClass(), null);
    		executeSql = executeSql.replace(String.format("UPDATE %s", tableName), String.format("UPDATE %s_%s", tableName, tableFix));
    	}
    	return jdbcTemplate.update(executeSql, boundSql.getParams().toArray());
    }

    public int update(Object entity) {
        BoundSql boundSql = SqlUtils.buildUpdateSql(entity, null, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

    public void delete(Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildDeleteSql(null, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }

   /* public void delete(Object entity) {
        BoundSql boundSql = SqlUtils.buildDeleteSql(entity, null, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }*/

    public void delete(Class<?> clazz, Long id) {
        BoundSql boundSql = SqlUtils.buildDeleteSql(clazz, id, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams().toArray());
    }


	public <T> List<T> queryList(Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }
	
	public <T> List<T> queryList(Criteria criteria, String tableFix) {
		BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
		if(LOG.isDebugEnabled())
		{
			LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
		}
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(criteria.getEntityClass(), null);
    		executeSql = executeSql.replace(String.format("FROM %s", tableName), String.format("FROM %s_%s", tableName, tableFix));
    	}
		List<?> list = jdbcTemplate.query(executeSql, boundSql.getParams().toArray(),
				this.getRowMapper(criteria.getEntityClass()));
		return (List<T>) list;
	}
	
	public <T> List<T> queryListLimit(Criteria criteria,int index,int size) {
        BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
        String sql=String.format("%s limit %s,%s", boundSql.getSql(), index,size);
        boundSql.setSql(sql);
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        return (List<T>) list;
    }
	
	public <T> List<T> queryListLimit(Criteria criteria, String tableFix, int index,int size) {
		BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
		String sql=String.format("%s limit %s,%s", boundSql.getSql(), index,size);
		boundSql.setSql(sql);
		if(LOG.isDebugEnabled())
		{
			LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
		}
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(criteria.getEntityClass(), null);
    		executeSql = executeSql.replace(String.format("FROM %s", tableName), String.format("FROM %s_%s", tableName, tableFix));
    	}
		List<?> list = jdbcTemplate.query(executeSql, boundSql.getParams().toArray(),
				this.getRowMapper(criteria.getEntityClass()));
		return (List<T>) list;
	}

    public <T> List<T> queryList(Class<?> clazz) {
        BoundSql boundSql = SqlUtils.buildListSql(null, Criteria.create(clazz),
            this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(clazz));
        return (List<T>) list;
    }

   /* public <T> List<T> queryList(T entity) {
        BoundSql boundSql = SqlUtils.buildListSql(entity, null, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }*/

   /* public <T> List<T> queryList(T entity, Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildListSql(entity, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(entity.getClass()));
        return (List<T>) list;
    }*/
    

    /*public int queryCount(Object entity, Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildCountSql(entity, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }

    public int queryCount(Object entity) {
        BoundSql boundSql = SqlUtils.buildCountSql(entity, null, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }*/

    public int queryCount(Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildCountSql(null, criteria, this.getNameHandler());
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        return jdbcTemplate.queryForInt(boundSql.getSql(), boundSql.getParams().toArray());
    }
    public int queryCount(Criteria criteria, String tableFix) {
    	BoundSql boundSql = SqlUtils.buildCountSql(null, criteria, this.getNameHandler());
    	if(LOG.isDebugEnabled())
    	{
    		LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
    	}
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(criteria.getEntityClass(), null);
    		executeSql = executeSql.replace(String.format("FROM %s", tableName), String.format("FROM %s_%s", tableName, tableFix));
    	}
    	return jdbcTemplate.queryForInt(executeSql, boundSql.getParams().toArray());
    }

    public <T> T get(Class<T> clazz, Long id) {
        BoundSql boundSql = SqlUtils.buildByIdSql(clazz, id, null, this.getNameHandler());

        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = jdbcTemplate.query(boundSql.getSql(), this.getRowMapper(clazz), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T get(Criteria criteria, Long id) {
        BoundSql boundSql = SqlUtils.buildByIdSql(null, id, criteria, this.getNameHandler());

        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<T> list = (List<T>) jdbcTemplate.query(boundSql.getSql(),
            this.getRowMapper(criteria.getEntityClass()), id);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.iterator().next();
    }

    public <T> T querySingleResult(T entity) {
        BoundSql boundSql = SqlUtils.buildListSql(entity, null, this.getNameHandler());
        String sql=String.format("%s limit 1", boundSql.getSql());
        boundSql.setSql(sql);
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(entity.getClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }

    public <T> T querySingleResult(Criteria criteria) {
        BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
        String sql=String.format("%s limit 1", boundSql.getSql());
        boundSql.setSql(sql);
        if(LOG.isDebugEnabled())
        {
        	LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
        }
        //采用list方式查询，当记录不存在时返回null而不会抛出异常
        List<?> list = jdbcTemplate.query(boundSql.getSql(), boundSql.getParams().toArray(),
            this.getRowMapper(criteria.getEntityClass()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (T) list.iterator().next();
    }
    
    public <T> T querySingleResult(Criteria criteria, String tableFix) {
    	BoundSql boundSql = SqlUtils.buildListSql(null, criteria, this.getNameHandler());
    	String sql=String.format("%s limit 1", boundSql.getSql());
    	boundSql.setSql(sql);
    	if(LOG.isDebugEnabled())
    	{
    		LOG.debug(String.format("sql:%s, params:%s ", boundSql.getSql(),boundSql.getParams().toArray()));
    	}
    	//采用list方式查询，当记录不存在时返回null而不会抛出异常
    	String executeSql = boundSql.getSql();
    	if(StringUtils.isNotBlank(tableFix)) {
    		String tableName = this.getNameHandler().getTableName(criteria.getEntityClass(), null);
    		executeSql = executeSql.replace(String.format("FROM %s", tableName), String.format("FROM %s_%s", tableName, tableFix));
    	}
    	List<?> list = jdbcTemplate.query(executeSql, boundSql.getParams().toArray(),
    			this.getRowMapper(criteria.getEntityClass()));
    	if (CollectionUtils.isEmpty(list)) {
    		return null;
    	}
    	return (T) list.iterator().next();
    }

    public byte[] getBlobValue(Class<?> clazz, String fieldName, Long id) {
        String primaryName = nameHandler.getPKName(clazz);
        String columnName = nameHandler.getColumnName(fieldName);
        String tableName = nameHandler.getTableName(clazz, null);
        String tmp_sql = "select t.%s from %s t where t.%s = ?";
        String sql = String.format(tmp_sql, columnName, tableName, primaryName);
        
        return jdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<byte[]>() {
            public byte[] extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getBytes(1);
                }
                return null;
            }
        });
    }

    /**
     * 获取rowMapper对象
     *
     * @param clazz
     * @return
     */
    protected <T> RowMapper<T> getRowMapper(Class<T> clazz) {

        if (StringUtils.isBlank(rowMapperClass)) {
            return BeanPropertyRowMapper.newInstance(clazz);
        } else {
            return (RowMapper<T>) ClassUtils.newInstance(rowMapperClass);
        }
    }

    /**
     * 获取名称处理器
     *
     * @return
     */
    protected NameHandler getNameHandler() {

        if (this.nameHandler == null) {
            this.nameHandler = new DefaultNameHandler();
        }
        return this.nameHandler;
    }

    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setNameHandler(NameHandler nameHandler) {
        this.nameHandler = nameHandler;
    }

    public void setRowMapperClass(String rowMapperClass) {
        this.rowMapperClass = rowMapperClass;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
