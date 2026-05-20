package com.repair.repair.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repair.common.entity.RepairOrder;
import com.repair.common.vo.RepairOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    @Select("SELECT ro.*, u.real_name AS user_name, u.phone AS user_phone, u.building, u.unit_num, u.room, "
            + "c.name AS category_name, d.repairer_id, ur.real_name AS repairer_name, rv.rating "
            + "FROM repair_order ro "
            + "LEFT JOIN user u ON ro.user_id = u.id "
            + "LEFT JOIN category c ON ro.category_id = c.id "
            + "LEFT JOIN dispatch d ON ro.id = d.repair_order_id "
            + "LEFT JOIN user ur ON d.repairer_id = ur.id "
            + "LEFT JOIN review rv ON ro.id = rv.repair_order_id "
            + "WHERE ro.id = #{id}")
    RepairOrderVO selectVOById(@Param("id") Long id);

    @Select("<script>"
            + "SELECT ro.*, u.real_name AS user_name, u.phone AS user_phone, u.building, u.unit_num, u.room, "
            + "c.name AS category_name, d.repairer_id, ur.real_name AS repairer_name "
            + "FROM repair_order ro "
            + "LEFT JOIN user u ON ro.user_id = u.id "
            + "LEFT JOIN category c ON ro.category_id = c.id "
            + "LEFT JOIN dispatch d ON ro.id = d.repair_order_id "
            + "LEFT JOIN user ur ON d.repairer_id = ur.id "
            + "<where>"
            + "<if test='status != null and status != \"\"'>AND ro.status = #{status}</if>"
            + "<if test='userId != null'>AND ro.user_id = #{userId}</if>"
            + "</where>"
            + "ORDER BY ro.create_time DESC"
            + "</script>")
    List<RepairOrderVO> selectVOList(@Param("status") String status, @Param("userId") Long userId);
}
