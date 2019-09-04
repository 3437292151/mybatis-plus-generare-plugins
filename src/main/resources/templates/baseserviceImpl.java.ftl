package ${package.ServiceImpl};

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import ${package.BaseEntity}.${BaseEntity};
import ${package.BaseService}.${BaseService};
import ${package.EntityMapper}.${BaseEntityMapper};
import ${package.Controller}.util.PageUtil;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseServiceImpl<DAO extends BaseMapper<E>, Mapper extends EntityMapper<D, E>, D, E extends PrimaryKey> implements BaseService<D, E> {
    protected Log log = LogFactory.getLog(this.getClass());

    @Autowired
    protected DAO baseMapper;

    @Autowired
    protected Mapper entityMapper;
    private Wrapper<D> queryWrapper;
    private Function<? super Object, Object> mapper;

    public BaseServiceImpl() {
    }

    public DAO getBaseMapper() {
        return this.baseMapper;
    }

    public Mapper getEntityMapper() {
        return this.entityMapper;
    }

    protected boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<E> currentModelClass() {
        return (Class<E>) ReflectionKit.getSuperClassGenericType(this.getClass(), 2);
    }

    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(this.currentModelClass());
    }

    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(this.currentModelClass()));
    }

    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(this.currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    public D save(D dto) {
        E entity = this.entityMapper.toEntity(dto);
        entity.setUUID().setTime();
        this.baseMapper.insert(entity);
        return this.entityMapper.toDto(entity);
    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public List<D> saveBatch(List<D> dtoList, int batchSize) {
        String sqlStatement = this.sqlStatement(SqlMethod.INSERT_ONE);
        List<E> entityList = this.entityMapper.toEntity(dtoList);
        entityList.forEach(e -> e.setUUID().setTime());
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (E anEntityList : entityList) {
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return this.entityMapper.toDto(entityList);
    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public D saveOrUpdate(D dto) {
        if(null == dto) {
            return dto;
        } else {
            E entity = this.entityMapper.toEntity(dto);
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", new Object[0]);
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!", new Object[0]);
            Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
            return !StringUtils.checkValNull(idVal) && !Objects.isNull(this.getById((Serializable)idVal))?this.updateById(dto):this.save(dto);
        }
    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public List<D> saveOrUpdateBatch(List<D> dtoList, int batchSize) {
        Assert.notEmpty(dtoList, "error: dtoList must not be empty", new Object[0]);
        Class<?> cls = this.currentModelClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", new Object[0]);
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!", new Object[0]);

        List<E> entityList = this.entityMapper.toEntity(dtoList);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (E entity : entityList) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    entity.setUUID().setTime();
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                } else {
                    ParamMap<E> param = new ParamMap<>();
                    entity.setUpdDt(LocalDateTime.now());
                    param.put(Constants.ENTITY, entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                }
                // 不知道以后会不会有人说更新失败了还要执行插入 😂😂😂
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return this.entityMapper.toDto(entityList);
    }

    public boolean removeById(Serializable id) {
        return SqlHelper.retBool(Integer.valueOf(this.baseMapper.deleteById(id)));
    }

    public boolean removeByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty", new Object[0]);
        return SqlHelper.retBool(Integer.valueOf(this.baseMapper.deleteByMap(columnMap)));
    }

    public boolean remove(D dto) {
        E entity = this.entityMapper.toEntity(dto);
        Wrapper<E> eWrapper = Wrappers.update(entity);
        return SqlHelper.retBool(Integer.valueOf(this.baseMapper.delete(eWrapper)));
    }

    public boolean removeByIds(List<? extends Serializable> idList) {
        return SqlHelper.retBool(Integer.valueOf(this.baseMapper.deleteBatchIds(idList)));
    }

    public D updateById(D dto) {
        E entity = this.entityMapper.toEntity(dto);
        entity.setUpdDt(LocalDateTime.now());
        this.baseMapper.updateById(entity);
        return dto;
    }

    public boolean update(D entity, Wrapper<D> updateWrapper) {
        E e = this.entityMapper.toEntity(entity);
        Wrapper<E> wrapper = Wrappers.update(this.entityMapper.toEntity(updateWrapper.getEntity()));
        return this.retBool(Integer.valueOf(this.baseMapper.update(e, wrapper)));
    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public boolean updateBatchById(List<D> dtoList, int batchSize) {
        Assert.notEmpty(dtoList, "error: entityList must not be empty", new Object[0]);
        String sqlStatement = this.sqlStatement(SqlMethod.UPDATE_BY_ID);
        List<E> entityList = this.entityMapper.toEntity(dtoList);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (E anEntityList : entityList) {
                ParamMap<E> param = new ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    public D getById(Serializable id) {
        return this.entityMapper.toDto(this.baseMapper.selectById(id));
    }

    public List<D> listByIds(List<? extends Serializable> idList) {
        return this.entityMapper.toDto(this.baseMapper.selectBatchIds(idList));
    }

    public List<D> listByMap(Map<String, Object> columnMap) {
        return this.entityMapper.toDto(this.baseMapper.selectByMap(columnMap));
    }

    public D getOne(D dto, boolean throwEx) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return throwEx?this.entityMapper.toDto(this.baseMapper.selectOne(wrapper)):SqlHelper.getObject(this.log, this.entityMapper.toDto(this.baseMapper.selectList(wrapper)));
    }

    public Map<String, Object> getMap(D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return (Map)SqlHelper.getObject(this.log, this.baseMapper.selectMaps(wrapper));
    }



    public int count(D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return SqlHelper.retCount(this.baseMapper.selectCount(wrapper));
    }

    public List<D> list(D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return this.entityMapper.toDto(this.baseMapper.selectList(wrapper));
    }

    public IPage<D> page(Pageable pageable, D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        Page<E> eiPage = PageUtil.pageableToPage(pageable);
        IPage<E> eiPage1 = this.baseMapper.selectPage(eiPage, wrapper);
        Page<D> page = new Page(pageable.getPageNumber(), pageable.getPageSize());
        page.setTotal(eiPage1.getTotal());
        page.setPages(eiPage1.getPages());
        page.setRecords(this.entityMapper.toDto(eiPage1.getRecords()));

        return page;
    }

    public List<Map<String, Object>> listMaps(D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return this.baseMapper.selectMaps(wrapper);
    }

    public <V> List<V> listObjs(D dto, Function<? super Object, V> mapper) {

        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        return this.baseMapper.selectObjs(wrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    public IPage<Map<String, Object>> pageMaps(IPage<D> page, D dto) {
        Wrapper<E> wrapper = Wrappers.query(this.entityMapper.toEntity(dto));
        Page<E> eiPage = new Page<E>();
        eiPage.setCurrent(page.getCurrent());
        eiPage.setSize(page.getPages());
        eiPage.setOrders(page.orders());
        return this.baseMapper.selectMapsPage(eiPage, wrapper);
    }

    public <V> V getObj(D dto, Function<? super Object, V> mapper) {
        return SqlHelper.getObject(this.log, this.listObjs(dto, mapper));
    }

}
