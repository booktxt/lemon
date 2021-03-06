package com.lemon.dao;


import com.lemon.domain.impl.BaseDomain;
import com.lemon.query.BaseQuery;

import java.util.List;

/**
 * Created by simpletour_java on 2015/9/8.
 */
public interface IBaseDao<T extends BaseDomain,M extends BaseQuery> {

    /**
     * 添加
     * @param object
     * @return
     */
    Integer insert(T object);

    /**
     * 根据条件删除
     * @param object
     */
    Integer delete(M object);

    /**
     * 根据id删除
     * @param id
     */
    Integer delete(Long id);

    /**
     * 更新
     * @param object
     * @return
     */
    Integer update(T object);

    /**
     * 查找
     * @param id
     * @return
     */
    T find(Long id);

    /**
     * 分页查询
     * @param object
     * @return
     */
    List<T> findByPage(M object);


    /**
     * 根据条件查询多条记录
     * @param object
     * @return
     */
    List<T> findEntities(M object);

    /**
     * 用于分页查询统计总数
     * @param object
     * @return
     */
    Integer count(M object);

}
