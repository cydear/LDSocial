#组件化框架搭建流程
1. gradle配置文件参数化，提取各组件公共配置到config.gradle文件，各模块gradle通过引用配置，统一管理，控制版本

2. 设置组件模式和集成开发模式，开发调试时将单一业务模块做成App启动

   com.android.library -> com.android.application
   
   ApplicationId  作为App使用时需要指定ApplicationId
   
   project.getName() 获取module名称

2. 解决在集成模式和组件模式下AndroidManifest问题

3. Application 各模块参数初始化

4. 组件化权限

    -> base  normal权限在base module中申请
    
    -> module  dangerous权限在各module中申请

#架构

App           Application

业务组件       Application or Library

基础组件base   library

基础组件       library

基础核心库     library
  

   
