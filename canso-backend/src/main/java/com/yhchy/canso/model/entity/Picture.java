package com.yhchy.canso.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: yhengcan
 * @Date: 2023/8/12 17:35
 * @Description: 图片实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Picture implements Serializable {
    /**
     * 标题
     */
    private String title;
    /**
     * 链接
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
