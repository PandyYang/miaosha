package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserDOExample;
import java.util.List;

public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int deleteByExample(UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    List<UserDO> selectByExample(UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    UserDO selectByPrimaryKey(Integer id);

    //根据手机号查询用户的详细信息
    UserDO selectByTelephone(String telephone);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Fri Apr 26 17:42:24 CST 2019
     */
    int updateByPrimaryKey(UserDO record);
}