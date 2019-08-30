package ${package.Service};

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.*;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;

import ${package.parent}.domain.PrimaryKey;
import ${package.Service}.mapper.EntityMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
* Contract for a common service interface.
*
*/
public interface BaseService<D, E extends PrimaryKey> {

    D save(D var1);

    @Transactional(
       rollbackFor = {Exception.class}
    )
    default List<D> saveBatch(List<D> entityList) {
        return this.saveBatch(entityList, 1000);
    }

    List<D> saveBatch(List<D> var1, int var2);

    @Transactional(
        rollbackFor = {Exception.class}
    )
    default List<D> saveOrUpdateBatch(List<D> entityList) {
        return this.saveOrUpdateBatch(entityList, 1000);
    }

    List<D> saveOrUpdateBatch(List<D> var1, int var2);

    boolean removeById(Serializable var1);

    boolean removeByMap(Map<String, Object> var1);

    boolean remove(Wrapper<D> var1);

    boolean removeByIds(List<? extends Serializable> var1);


    D updateById(D var1);

    boolean update(D var1, Wrapper<D> var2);

    default boolean update(Wrapper<D> updateWrapper) {
        return this.update((D) null, updateWrapper);
    }

    @Transactional(
       rollbackFor = {Exception.class}
    )
    default boolean updateBatchById(List<D> entityList) {
        return this.updateBatchById(entityList, 1000);
    }

    boolean updateBatchById(List<D> var1, int var2);

    D saveOrUpdate(D var1);

    D getById(Serializable var1);

    List<D> listByIds(List<? extends Serializable> var1);

    List<D> listByMap(Map<String, Object> var1);

    default D getOne(Wrapper<D> queryWrapper) {
        return this.getOne(queryWrapper, true);
    }

    D getOne(Wrapper<D> var1, boolean var2);

    Map<String, Object> getMap(Wrapper<D> var1);

    <V> V getObj(Wrapper<D> var1, Function<? super Object, V> var2);

    int count(Wrapper<D> var1);

    default int count() {
        return this.count(Wrappers.emptyWrapper());
    }

    List<D> list(Wrapper<D> var1);

    default List<D> list() {
        return this.list(Wrappers.emptyWrapper());
    }

    IPage<D> page(Pageable pageable, Wrapper<D> var2);

    default IPage<D> page(Pageable pageable) {
        return this.page(pageable, Wrappers.emptyWrapper());
    }

    List<Map<String, Object>> listMaps(Wrapper<D> var1);

    default List<Map<String, Object>> listMaps() {
        return this.listMaps(Wrappers.emptyWrapper());
    }

    default List<Object> listObjs() {
        return this.listObjs(Function.identity());
    }

    default <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return this.listObjs(Wrappers.emptyWrapper(), mapper);
    }

    default List<Object> listObjs(Wrapper<D> queryWrapper) {
        return this.listObjs(queryWrapper, Function.identity());
    }

    <V> List<V> listObjs(Wrapper<D> var1, Function<? super Object, V> var2);

    IPage<Map<String, Object>> pageMaps(IPage<D> var1, Wrapper<D> var2);

    default IPage<Map<String, Object>> pageMaps(IPage<D> page) {
        return this.pageMaps(page, Wrappers.emptyWrapper());
    }

    BaseMapper<E> getBaseMapper();

    EntityMapper<D, E> getEntityMapper();

    default QueryChainWrapper<D> query() {
        return new QueryChainWrapper(this.getBaseMapper());
    }

    default LambdaQueryChainWrapper<D> lambdaQuery() {
        return new LambdaQueryChainWrapper(this.getBaseMapper());
    }

    default UpdateChainWrapper<D> update() {
        return new UpdateChainWrapper(this.getBaseMapper());
    }

    default LambdaUpdateChainWrapper<D> lambdaUpdate() {
        return new LambdaUpdateChainWrapper(this.getBaseMapper());
    }
}