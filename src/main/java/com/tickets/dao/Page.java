package com.tickets.dao;

import com.tickets.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 与具体ORM实现无关的分页参数及查询结果封装.<br>
 * 注意：记录序列号是从1开始。页码是从1开始
 *
 * @param <T>
 *            Page中记录的类型
 */
public class Page<T> {
    //	public
    // -- 公共变量 --//
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    // -- 分页参数 --//
    protected int pageNo = 1;
    protected int pageSize = 1;
    protected String orderBy = null;
    protected String order = null;
    protected boolean autoCount = true;
    protected long totalPage = 1;
    private Map<String,Object> selectMap = new HashMap<String,Object>();
    // -- 返回结果 --//
    protected List<T> result = Collections.emptyList();
    protected long totalCount = -1;

    // -- 构造函数 --//
    public Page() {
    }

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    public Page(List<T> result,int pageSize,long totalCount, int pageNo) {
        if (result.equals(Collections.EMPTY_LIST)) {
            this.result = result;
        }else {
            this.result = result;
            this.totalCount = totalCount;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            //页面总数
            this.totalPage = this.getTotalPages();
        }
    }

    // -- 访问查询参数函数 --//
    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }

    public long getCountPage(){
        long countPage=0;
        if(pageNo>0){
            countPage=totalCount/pageSize;
            if(totalCount%pageSize>0 ){
                countPage++;
            }
        }
        return countPage;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }
    /**
     * 设置当前页的页码, 序号从1开始,低于1时自动调整为1.
     *
     * @param pageNo
     *            页码
     * @return this
     */
    public Page<T> pageNo(final int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量,低于1时自动调整为1.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        if (pageSize < 1) {
            this.pageSize = 1;
        }
    }
    /**
     * 设置每页的记录数量, 为0表示不分页
     *
     * @param pageSize
     *            每页的记录数量
     * @return this
     */
    public Page<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    /**
     * 获得排序字段,无默认值.多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 设置排序字段.
     *
     * @param orderBy
     *            排序字段. 多个字段用","分隔
     * @return this
     */
    public Page<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     * 获得排序方向.
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式向.
     *
     * @param order
     *            可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        // 检查order字符串的合法值
        String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
        }

        this.order = StringUtils.lowerCase(order);
    }

    /**
     * 设置排序方向
     *
     * @param orderType
     *            排序方向。可选值{@link #ASC}、{@link #DESC}。多个属性用","分隔
     */
    public Page<T> order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     * 将排序属性转换为数组
     */
    public String[] toArrayFromOrderBy() {
        return (isOrderBySetted()) ? orderBy.split(",") : new String[0];
    }

    /**
     * 将排序方向转换为数组
     */
    public String[] toArrayFromOrderType() {
        return (isOrderBySetted()) ? order.split(",") : new String[0];
    }

    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数.
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    public Page<T> autoCount(final boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    // -- 访问查询结果函数 --//

    /**
     * 取得页内的记录列表.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     * 取得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPages() {
        if (totalCount < 0)
            return -1;

        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext())
            return pageNo + 1;
        else
            return pageNo;
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre())
            return pageNo - 1;
        else
            return pageNo;
    }
}

