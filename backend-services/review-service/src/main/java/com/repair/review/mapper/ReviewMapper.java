package com.repair.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repair.common.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("SELECT AVG(rating) FROM review WHERE repairer_id = #{repairerId}")
    Double getAvgRatingByRepairerId(@Param("repairerId") Long repairerId);

    @Select("SELECT r.*, u.real_name AS user_name FROM review r "
            + "LEFT JOIN user u ON r.user_id = u.id "
            + "WHERE r.repairer_id = #{repairerId} "
            + "ORDER BY r.create_time DESC")
    List<Map<String, Object>> selectByRepairerId(@Param("repairerId") Long repairerId);

    @Select("SELECT COUNT(*) AS total, AVG(rating) AS avg_rating FROM review WHERE repairer_id = #{repairerId}")
    Map<String, Object> selectStatsByRepairerId(@Param("repairerId") Long repairerId);
}
