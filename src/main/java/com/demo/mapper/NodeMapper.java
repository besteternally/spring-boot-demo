package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.demo.bean.Node;

public interface NodeMapper {

    //@Select("select node_id,node_level,node_name,node_name as title,node_state,node_desc,node_pid from node WHERE node_pid =0")
    @Select("select * from node WHERE node_pid =0")
    List<Node> selectNodeAll();

    @Select("select * from node WHERE  node_pid =0 and node_name like '%${nodename}%'")
    List<Node> selectName(@Param("nodename") String nodename);

    @Select("SELECT count(node_level) from node where node_level not like '%.%'")
    String countByLevel();

    @Insert("insert into node values (null, #{count}, #{node_name}, #{node_desc},'使用中',0)")
    int addNode(@Param("node_name") String node_name, @Param("node_desc") String node_desc, @Param("count") String count);

    //	查询语句相关
    @Select("select node_level from node where node_name like '%${node_name}%' and node_level rlike '^${node_level}.*'")
    List<String> selectLevelsByName(@Param("node_name") String node_name,
                                    @Param("node_level") String node_level);

    @Select("select node_level from node where node_pid in (select node_id from node where node_name like '%${node_name}%' and node_level rlike '^${node_level}.*')")
    List<String> selectChildLevelsByName(@Param("node_name") String node_name,
                                         @Param("node_level") String node_level);

    @Select("select node_id, node_level, node_name, node_state, node_desc, node_pid from node where node_level = #{node_level}")
    Node selectNodeByLevel(@Param("node_level") String node_level);

    //	点击一次查询一次子结点
    @Select("select node_id, node_level,node_name, node_state, node_desc, node_pid from node where node_pid =#{node_id} or node_id =#{node_id}")
    List<Node> selectChildNodesById(@Param("node_id") Integer node_id);

    //	新增语句相关
    @Select("select node_level from node where node_pid = #{node_id}")
    List<String> selectChildLevelsById(@Param("node_id") Integer id);

    @Insert("insert into node values (null, #{node_level}, #{node_name}, #{node_desc},'使用中', #{node_pid})")
    void insertChildNode(@Param("node_level") String node_level, @Param("node_name") String node_name,
                         @Param("node_desc") String node_desc, @Param("node_pid") Integer node_pid);

    @Select("select node_id from node where node_name=#{node_name}")
    List<Integer> selectIdByName(@Param("node_name") String node_name);

    @Select("select node_level from node where node_id = #{node_id}")
    String selectLevelById(@Param("node_id") Integer node_id);

    //	修改语句相关
    @Update("update node set node_name = #{node_name}, node_desc = #{node_desc} where node_id = #{node_id}")
    void updateNodeById(@Param("node_name") String node_name, @Param("node_desc") String node_desc,
                        @Param("node_id") Integer node_id);

    //	删除语句相关
    @Delete("delete from node where node_id = #{node_id}")
    void deleteNodeById(@Param("node_id") Integer node_id);

    @Update(("update node set node_state = '已删除' where node_id = #{node_id}"))
    void updateStateById(@Param("node_id") Integer node_id);

    @Select("select node_id, node_level,node_name, node_state, node_desc, node_pid from node where node_pid = #{node_id}")
    List<Node> selectChildNodesById1(Integer node_id);
}
