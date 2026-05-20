package com.repair.dispatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repair.common.entity.Dispatch;
import com.repair.common.vo.DispatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DispatchMapper extends BaseMapper<Dispatch> {

    @Select("SELECT d.*, ro.description AS repair_order_desc, ro.urgency, ro.user_id, "
            + "u.real_name AS user_name, u.phone AS user_phone, u.building, u.unit_num, u.room, "
            + "ur.real_name AS repairer_name, ur.phone AS repairer_phone, ur.skill_tag, "
            + "ua.real_name AS admin_name, c.name AS category_name "
            + "FROM dispatch d "
            + "LEFT JOIN repair_order ro ON d.repair_order_id = ro.id "
            + "LEFT JOIN user u ON ro.user_id = u.id "
            + "LEFT JOIN user ur ON d.repairer_id = ur.id "
            + "LEFT JOIN user ua ON d.admin_id = ua.id "
            + "LEFT JOIN category c ON ro.category_id = c.id "
            + "WHERE d.id = #{id}")
    DispatchVO selectVOById(@Param("id") Long id);

    @Select("<script>"
            + "SELECT d.*, ro.description AS repair_order_desc, ro.urgency, ro.user_id, "
            + "u.real_name AS user_name, u.phone AS user_phone, u.building, u.unit_num, u.room, "
            + "ur.real_name AS repairer_name, ur.phone AS repairer_phone, ur.skill_tag, "
            + "ua.real_name AS admin_name, c.name AS category_name "
            + "FROM dispatch d "
            + "LEFT JOIN repair_order ro ON d.repair_order_id = ro.id "
            + "LEFT JOIN user u ON ro.user_id = u.id "
            + "LEFT JOIN user ur ON d.repairer_id = ur.id "
            + "LEFT JOIN user ua ON d.admin_id = ua.id "
            + "LEFT JOIN category c ON ro.category_id = c.id "
            + "<where>"
            + "<if test='repairerId != null'>AND d.repairer_id = #{repairerId}</if>"
            + "<if test='status != null and status != \"\"'>AND d.status = #{status}</if>"
            + "</where>"
            + "ORDER BY d.dispatch_time DESC"
            + "</script>")
    List<DispatchVO> selectVOList(@Param("repairerId") Long repairerId, @Param("status") String status);
}
