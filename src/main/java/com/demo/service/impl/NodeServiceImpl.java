package com.demo.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.bean.Node;
import com.demo.mapper.NodeMapper;
import com.demo.service.NodeService;

/**
 * @ClassName NodeServiceImpl
 * @Description Service层实现类
 * @Author hyj
 * @Date 2019/4/8 10:40
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeMapper nodeMapper;

    /**
     * 查询所有的根节点
     *
     * @return 查询到的节点列表
     */
    @Override
    public List<Node> findNodeAll() {
        return this.nodeMapper.selectNodeAll();
    }

    /**
     * 用过名称查询所有的节点
     *
     * @param name 名称
     * @return 查询到的节点列表
     */
    @Override
    public List<Node> selectByName(String name) {
        System.out.println("service 层 " + name);
        return this.nodeMapper.selectName(name);
    }

    /**
     * 增加节点
     *
     * @param node_name 节点名称
     * @param node_desc 节点描述
     * @return 增加的结果
     */
    @Override
    public int addNode(String node_name, String node_desc) {
        int count = Integer.parseInt(this.nodeMapper.countByLevel());
        this.nodeMapper.addNode(node_name, node_desc, count + 1);
        List<Integer> list = nodeMapper.selectIdByName(node_name);
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                nodeMapper.deleteNodeById(list.get(i));
            }
            return -1;
        } else {
            return list.get(0);
        }
    }


    /**
     * 点击一次 查询一次该节点以及它的两层子节点
     *
     * @param node_id 节点的id
     * @return 节点列表
     */
    @Override
    public List<Node> selectChildNodesById(Integer node_id) {
        List<Node> childList = nodeMapper.selectChildNodesById(node_id);
        List<Node> grandsonList = new ArrayList<>();
        List<Node> result = new ArrayList<>();
        for (Node child : childList) {
            grandsonList.addAll(nodeMapper.selectChildNodesById(child.getNode_id()));
        }
        result.addAll(childList);
        result.addAll(grandsonList);
        result.add(nodeMapper.selectNodeById(node_id));
        return result;
    }

    /**
     * 点击一次，查询一次该节点的两层子节点
     *
     * @param node_id 节点的id
     * @return 查询到的两层子节点
     */
    @Override
    public List<Node> selectChildNodesById1(Integer node_id) {
        List<Node> childList = nodeMapper.selectChildNodesById(node_id);
        List<Node> grandsonList = new ArrayList<>();
        List<Node> result = new ArrayList<>();
        for (Node child : childList) {
            grandsonList.addAll(nodeMapper.selectChildNodesById(child.getNode_id()));
        }
        result.addAll(childList);
        result.addAll(grandsonList);
        return result;
    }

    /**
     * 通过名称和个节点的id查询节点
     *
     * @param node_name 查询的名称
     * @param node_id   所在根节点的id
     * @return 查询到的节点列表
     */
    @Override
    public List<Node> findNodesByName(String node_name, Integer node_id) {
        String node_level = nodeMapper.selectLevelById(node_id);
        //levelList: 存放所有通过node_name查询到的数据的node_level levelSet: 存放所查到的若干子结点到根结点的路径
        List<String> levelList = nodeMapper.selectLevelsByName(node_name, node_level);
        levelList.addAll(nodeMapper.selectChildLevelsByName(node_name, node_level));
        Set<String> levelSet = new HashSet<>();
        List<Node> resultList = new ArrayList<>();
        String separate = ".";
        for (String level : levelList) {
            String str = level;
            levelSet.add(str);
            while (str.contains(separate)) {
                str = str.substring(0, str.lastIndexOf("."));
                levelSet.add(str);
            }
        }
        for (String level : levelSet) {
            resultList.add(nodeMapper.selectNodeByLevel(level));
        }
        return resultList;
    }

    /**
     * 插入子节点
     *
     * @param node_id   节点的id
     * @param node_name 节点的名称
     * @param node_desc 节点的描述
     * @return 插入的结果
     */
    @Override
    public Integer insertChildNode(Integer node_id, String node_name, String node_desc) {
        List<String> levelList = nodeMapper.selectChildLevelsById(node_id);
        int end;
        if (levelList.isEmpty()) {
            end = 1;
        } else {
            List<Integer> endList = new ArrayList<>();
            for (String level : levelList) {
                endList.add(Integer.parseInt(level.substring(level.lastIndexOf(".") + 1)));
            }
            Collections.sort(endList);
            end = endList.get(endList.size() - 1) + 1;
        }
        String level = nodeMapper.selectLevelById(node_id);
        String node_level = level + "." + end;
        nodeMapper.insertChildNode(node_level, node_name, node_desc, node_id);
        List<Integer> list = nodeMapper.selectIdByName(node_name);
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                nodeMapper.deleteNodeById(list.get(i));
            }
            return -1;
        } else {
            return list.get(0);
        }
    }

    /**
     * 更新节点
     *
     * @param node_name 节点的名称
     * @param node_desc 节点的描述
     * @param node_id   节点的id
     */
    @Override
    public void updateNodeById(String node_name, String node_desc, Integer node_id) {
        nodeMapper.updateNodeById(node_name, node_desc, node_id);
    }

    /**
     * 删除节点
     *
     * @param node_id 要删除的节点的id
     */
    @Override
    public void deleteNodeById(Integer node_id) {
        String node_level = nodeMapper.selectLevelById(node_id);
        String regex = "/^[0-9]+.*/";
        if (node_level.matches(regex)) {
            nodeMapper.deleteNodeById(node_id);
        } else {
            nodeMapper.updateStateById(node_id);
        }

    }


}
