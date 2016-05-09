package com.xmomen.module.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmomen.framework.mybatis.dao.MybatisDao;
import com.xmomen.framework.mybatis.page.Page;
import com.xmomen.framework.web.exceptions.ArgumentValidException;
import com.xmomen.module.base.mapper.ItemMapper;
import com.xmomen.module.base.model.CreateItem;
import com.xmomen.module.base.model.ItemChildModel;
import com.xmomen.module.base.model.ItemModel;
import com.xmomen.module.base.model.UpdateItem;
import com.xmomen.module.base.service.ItemService;
import com.xmomen.module.logger.Log;
@RestController
public class ItemController {
	@Autowired
	ItemService itemService;
	@Autowired
	ItemMapper itemMapper;
	@Autowired
    MybatisDao mybatisDao;
    /**
     * 查询产品信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @Log(actionName = "查询产品信息")
    public Page<ItemModel> getMemberList(@RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "offset") Integer offset,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sellStatus", required = false) Integer sellStatus,
            @RequestParam(value = "itemType", required = false) Integer itemType,
            @RequestParam(value = "exclude_ids", required = false) Integer[] exclude_ids){
		 Map map = new HashMap<String,Object>();
	     map.put("id", id);
	     map.put("keyword", keyword);
	     if(sellStatus != null){
	    	 map.put("sellStatus",sellStatus);
	     }
	     if(itemType != null){
	    	 map.put("itemType",itemType);
	     }
        if(exclude_ids != null){
            map.put("exclude_ids", exclude_ids);
        }
        return (Page<ItemModel>) mybatisDao.selectPage(ItemMapper.ItemMapperNameSpace + "getItemList", map, limit, offset);
    }
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    @Log(actionName = "新增产品")
    public void createCompany(@RequestBody @Valid CreateItem createItem, BindingResult bindingResult) throws ArgumentValidException {
        if(bindingResult != null && bindingResult.hasErrors()){
            throw new ArgumentValidException(bindingResult);
        }
        itemService.createItem(createItem);
    }
    
    /**
     *  修改
     * @param id
     */
    @RequestMapping(value = "/item/{id}", method = RequestMethod.PUT)
    @Log(actionName = "修改产品")
    public void updateMember(@PathVariable(value = "id") Integer id,
                                @RequestBody @Valid UpdateItem updateItem, BindingResult bindingResult) throws ArgumentValidException {
        if(bindingResult != null && bindingResult.hasErrors()){
            throw new ArgumentValidException(bindingResult);
        }
        itemService.updateItem(id, updateItem);
    }
    
    /**
     *  删除
     * @param id
     */
    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    @Log(actionName = "删除产品信息")
    public void deleteMember(@PathVariable(value = "id") Integer id){
    	itemService.delete(id);
    }
    
    /**
     * 查询子产品
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getChildItem", method = RequestMethod.GET)
	public List<ItemChildModel> getChildItem(@RequestParam(value = "parentId", required = false) Integer parentId){
    	List<ItemChildModel> childItems = new ArrayList<ItemChildModel>();
    	Map map = new HashMap<String,Object>();
	    map.put("parentId", parentId);
	    childItems = mybatisDao.getSqlSessionTemplate().selectList(ItemMapper.ItemMapperNameSpace + "getChildItemList", map);
		return childItems;
	}
}