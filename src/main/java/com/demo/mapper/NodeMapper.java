package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.demo.bean.Node;

/**
 * @ClassName NodeMapper
 * @Description 节点mapper
 * @Author hyj
 * @Date 2019/4/8 10:39
 * @Version 1.0
 */
public interface NodeMapper {


    /**
     * 查询所有的二级节点
     * @return 搜索到的节点表
     */
    @Select("select * from node WHERE node_pid =0")
    List<Node> selectNodeAll();

    /**
     * 模糊查询二级根节点
     * @param nodename 节点名字
     * @return 搜索到的节点表
     */
    @Select("select * from node WHERE  node_pid =0 and (node_name like '%${nodename}%' or node_desc like '%${nodename}%')")
    List<Node> selectName(@Param("nodename") String nodename);

    /**
     * 查询二级节点的数量
     * @return 搜索到的节点表
     */
    @Select("SELECT count(node_level) from node where node_level not like '%.%'")
    String countByLevel();

    /**
     * 插入二级节点
     * @param node_name 节点名称
     * @param node_desc 节点描述
     * @param count 根节点的level
     * @return 返回数量
     */
    @Insert("insert into node values (null, #{count}, #{node_name}, #{node_desc},'使用中',0)")
    int addNode(@Param("node_name") String node_name, @Param("node_desc") String node_desc, @Param("count") int count);

    /**通过name和level模糊查询level
     * @param node_name 节点名字
     * @param node_level 节点层级
     * @return 搜索到的level列表
     */
    @Select("select node_level from node where (node_name like '%${node_name}%' or node_desc like '%${node_name}%') and node_level rlike '^${node_level}.*'")
    List<String> selectLevelsByName(@Param("node_name") String node_name,
                                    @Param("node_level") String node_level);

    /**通过名字查询节点的pid
     * @param node_name 节点姓名
     * @return  得到的节点的pid的链表
     */
    @Select("select pid from node where node_name=#{node_name}")
    List<Integer> selectPidByName(@Param("node_name") String node_name);

    /**查询pid相同的节点
     * @param node_pid 节点pid
     * @param node_name 节点名称
     * @return  查询到的节点列表
     */
    @Select("select * from node where node_pid=#{node_pid} and node_name !=#{node_name}")
    List<Node> selectNodeByPid(@Param("node_pid")Integer node_pid,@Param("node_name") String node_name);

    /**
     * 通过名称查询子节点的level
     * @param node_name 名称
     * @param node_level   层级
     * @return  查询到的的子节点的level表
     */
    @Select("select node_level from node where node_pid in (select node_id from node where node_name like '%${node_name}%' and node_level rlike '^${node_level}.*')")
    List<String> selectChildLevelsByName(@Param("node_name") String node_name,
                                         @Param("node_level") String node_level);

    /**
     * 通过节点的level查询节点
     * @param node_level 节点的层级
     * @return 返回查询到的节点
     */
    @Select("select node_id, node_level, node_name, node_state, node_desc, node_pid from node where node_level = #{node_level}")
    Node selectNodeByLevel(@Param("node_level") String node_level);

    /**点击一次查询一次子结点
     * @param node_id 节点id
     * @return 返回查询到的三层节点
     */
    @Select("select node_id, node_level,node_name, node_state, node_desc, node_pid from node where node_pid =#{node_id}")
    List<Node> selectChildNodesById(@Param("node_id") Integer node_id);

    /**
     * 通过id查询节点的子节点的level
     * @param id 节点的id
     * @return 查询到的节点的level
     */
    @Select("select node_level from node where node_pid = #{node_id}")
    List<String> selectChildLevelsById(@Param("node_id") Integer id);

    /**
     * 插入节点
     * @param node_level 节点的层级
     * @param node_name 节点的名称
     * @param node_desc 节点的描述
     * @param node_pid  节点的pid
     */
    @Insert("insert into node values (null, #{node_level}, #{node_name}, #{node_desc},'使用中', #{node_pid})")
    void insertChildNode(@Param("node_level") String node_level, @Param("node_name") String node_name,
                         @Param("node_desc") String node_desc, @Param("node_pid") Integer node_pid);

    /**
     * 通过名称查找id
     * @param node_name 节点的名称
     * @return 查询道的节点的id的
     */
    @Select("select node_id from node where node_name=#{node_name}")
    List<Integer> selectIdByName(@Param("node_name") String node_name);

    /**
     * 通过id查找level
     * @param node_id 节点的id
     * @return 通过id查询到的节点的level
     */
    @Select("select node_level from node where node_id = #{node_id}")
    String selectLevelById(@Param("node_id") Integer node_id);

    /**
     * 修改节点的信息
     * @param node_name 节点名称
     * @param node_desc 节点描述
     * @param node_id 节点的id
     */
    @Update("update node set node_name = #{node_name}, node_desc = #{node_desc} where node_id = #{node_id}")
    void updateNodeById(@Param("node_name") String node_name, @Param("node_desc") String node_desc,
                        @Param("node_id") Integer node_id);

    /**
     * 删除节点
     * @param node_id 要删除的节点的id
     */
    @Delete("delete from node where node_id = #{node_id}")
    void deleteNodeById(@Param("node_id") Integer node_id);

    /**
     * 修改根节点的状态
     * @param node_id 根节点的id
     */
    @Update(("update node set node_state = '已删除' where node_id = #{node_id}"))
    void updateStateById(@Param("node_id") Integer node_id);

    /**
     * 通过pid查找该节点的2层子节点
     * @param node_id 节点的id
     * @return 查询到的两层子节点
     */
    @Select("select node_id, node_level,node_name, node_state, node_desc, node_pid from node where node_pid = #{node_id}")
    List<Node> selectChildNodesById1(Integer node_id);

    /**
     * 通过id查询节点
     * @param node_id 节点的id
     * @return 查询到的节点的id
     */
    @Select("select * from node where node_id=#{node_id}")
    Node selectNodeById(@Param("node_id") Integer node_id);
}
