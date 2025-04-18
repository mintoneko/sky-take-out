package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

  /**
   * 根据用户名查询员工
   *
   * @param username
   * @return
   */
  @Select("select * from employee where username = #{username}")
  Employee getByUsername(String username);

  /**
   * 新增员工数据
   *
   * @param employee
   */
  @Insert("INSERT INTO employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
          "VALUES " +
          "(#{id}, #{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
  void insert(Employee employee);


  /**
   * 员工分页查询
   *
   * @param employeePageQueryDTO
   * @return
   */
  // 因为SQL语句比较长，使用xml的方式
  Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

  /*
   * 根据id查询员工信息
   *
   * @param id
   * @return
   */
  @Select("select * from employee where id = #{id}")
  Employee getById(Long id);

  /**
   * 修改员工信息<-启用禁用员工账号
   *
   * @param employee
   * @return
   */
  void update(Employee employee);
}
