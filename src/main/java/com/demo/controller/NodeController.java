package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.demo.bean.Node;
import com.demo.service.NodeService;
import com.demo.util.ToTree;

import javax.validation.constraints.NotNull;

/**
 * @ClassName NodeController
 * @Description node控制器
 * @Author hyj
 * @Date 2019/4/8 10:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/node")
@Validated
public class NodeController {
    @Autowired
    private NodeService nodeService;

    /**
     * 查询主页面二级根节点
     */
    @GetMapping("/knowledgeTree")
    public List<Node> findNodeAll() {
        List<Node> list = this.nodeService.findNodeAll();
        return ToTree.listToTree(list);
    }

    /**
     * 通过名称查询
     * @param name 节点的名称
     * @return 查询到的节点的列表
     */
    @GetMapping("/knowledgeTree/{name}")
    public List<Node> selectByName(@PathVariable String name) {
        return this.nodeService.selectByName(name);
    }

    /**
     * 主界面增加节点
     *
     * @param node_name 节点姓名
     * @param node_desc 节点描述
     * @return 成功返回success，失败返回error
     */
    @PostMapping("/addNode")
    public String addNode(String node_name, String node_desc) {
        int ret = this.nodeService.addNode(node_name.trim(), node_desc.trim());
        if (ret == 1) {
            return "success";
        }
        return "error";
    }

    /**
     * 子界面通过id查询当前节点以及子节点
     * @param node_id 节点id
     * @return 返回查到的两层节点
     */
    @GetMapping("/selectChilds/{node_id}")
    public List<Node> selectChildNodesById(@PathVariable("node_id") Integer node_id) {
        List<Node> list = nodeService.selectChildNodesById(node_id);
        return ToTree.listToTree(list);
    }

    /**
     * 子界面通过id查询节点下一层的子节点
     *
     * @param node_id 节点id
     * @return 返回查询到的下一层子节点
     */
    @GetMapping("/selectChilds1/{node_id}")
    public List<Node> selectChildNodesById1(@PathVariable("node_id") Integer node_id) {
        return ToTree.listToTree(nodeService.selectChildNodesById1(node_id));
    }

    /**
     * 通过名字模糊查询节点
     * @param node_name 节点名称
     * @param node_id   节点id
     * @return  返回查询到的节点数组
     */
    @GetMapping(value = "/findNodes/{node_name}")
    public List<Node> findNodesByName(@PathVariable("node_name") String node_name, @NotNull Integer node_id) {
        List<Node> list = nodeService.findNodesByName(node_name, node_id);
        return ToTree.listToTree(list);
    }

    /**
     * 增加节点
     * @param node_id   父节点的id
     * @param node_name 节点名称
     * @param node_desc 节点描述
     * @return  增加成功返回success，失败返回error
     */
    @PostMapping(value = "/insertNode")
    public Integer insertChildNode(Integer node_id, String node_name, String node_desc) {
        try {
            return nodeService.insertChildNode(node_id, node_name.trim(), node_desc.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }

    }


    /**
     * 通过id来更新节点的名称和描述
     * @param node_name 节点名称
     * @param node_desc 节点描述
     * @param node_id   节点id
     * @return 增加成功返回success，失败返回error
     */
    @PutMapping("/updateNode")
    public String updateNodeById(String node_name, String node_desc, Integer node_id) {
        try {
            nodeService.updateNodeById(node_name.trim(), node_desc.trim(), node_id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 通过编号来删除节点
     *
     * @param node_id 节点id
     * @return 删除成功返回success，失败返回error
     */
    @DeleteMapping("/deleteNode/{node_id}")
    public String deleteNodeById(@PathVariable("node_id") Integer node_id) {
        try {
            nodeService.deleteNodeById(node_id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
