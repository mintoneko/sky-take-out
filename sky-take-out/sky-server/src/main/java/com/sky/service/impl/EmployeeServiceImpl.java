package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  private EmployeeMapper employeeMapper;
  @Autowired
  private EmployeeService employeeService;

  /**
   * 员工登录
   *
   * @param employeeLoginDTO
   * @return
   */
  public Employee login(EmployeeLoginDTO employeeLoginDTO) {
    String username = employeeLoginDTO.getUsername();
    String password = employeeLoginDTO.getPassword();

    //1、根据用户名查询数据库中的数据
    Employee employee = employeeMapper.getByUsername(username);

    //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
    if (employee == null) {
      //账号不存在
      throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
    }

    //密码比对
    // md5加密算法比对数据库中的密码
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    if (!password.equals(employee.getPassword())) {
      //密码错误
      throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
    }

    if (employee.getStatus() == StatusConstant.DISABLE) {
      //账号被锁定
      throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
    }

    //3、返回实体对象
    return employee;
  }

  /**
   * 新增员工
   *
   * @param employeeDTO
   */
  @Override
  public void save(EmployeeDTO employeeDTO) {

    Employee employee = new Employee();

    // 利用BeanUtils.copyProperties()方法将employeeDTO中的属性值复制到employee中->属性拷贝
    BeanUtils.copyProperties(employeeDTO, employee);
    // 仔细看两个对象的属性，Employee多了几条属性需要自己设置（因为要往服务器传输这些属性，所以还是不能少）

    // 设置状态
    employee.setStatus(StatusConstant.ENABLE);

    // 设置密码
    employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

    // 设置创建时间
    employee.setCreateTime(LocalDateTime.now());

    // 设置更新时间
    employee.setUpdateTime(LocalDateTime.now());

    // 设置创建人
    employee.setCreateUser(BaseContext.getCurrentId());

    // 设置修改人
    employee.setUpdateUser(BaseContext.getCurrentId());

    employeeMapper.insert(employee);
    // 封装好数据，调用持久层mapper向数据库插入数据
  }

  @Override
  public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
    // select * from employee limit 1, 10
    PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
    // 就相当于总数/pageSize得到总共多少页，然后page取某一页即可
    Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
    long total = page.getTotal();
    List<Employee> employees = page.getResult();
    return new PageResult(total, employees);
  }


  /**
   * 启用禁用员工账号
   *
   * @param status
   * @param id
   * @return
   */
  @Override
  public void startOrStop(Integer status, Long id) {
    // update employee set status = ? where id = ?
    Employee employee = Employee.builder()
            .id(id)
            .status(status)
            .updateTime(LocalDateTime.now())
            .updateUser(BaseContext.getCurrentId()) // 通过线程传递修改人的ID
            .build();
    employeeMapper.update(employee);
  }

  /*
   * 根据id查询员工信息
   *
   * @param id
   * @return
   */
  @Override
  public Employee getById(Long id) {
    Employee employee = employeeMapper.getById(id);
    employee.setPassword("******");
    return employee;
  }

  /**
   * 修改员工信息
   *
   * @param employeeDTO
   * @return
   */
  @Override
  public void update(EmployeeDTO employeeDTO) {
    Employee employee = new Employee();
    BeanUtils.copyProperties(employeeDTO, employee);
    employee.setUpdateTime(LocalDateTime.now());
    employee.setUpdateUser(BaseContext.getCurrentId());
    employeeMapper.update(employee);
  }
}
