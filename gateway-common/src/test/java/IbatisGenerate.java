import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * 代码生成：快速生成
 * @author hel
 * @date 2022-07-16 14:53
 */
public class IbatisGenerate {

    public static final String PROJECT_PATH = "user.dir";
    public static void main(String[] args) {
        generate();
    }

    /**
     * 代码生成
     */
    public static void generate() {

        // 自定义路径
        Map<OutputFile, String> customPathInfo = new EnumMap<>(OutputFile.class);

        //模块名称
        String controllerModule = "gateway-gate";
        String serviceModule = "gateway-config";
        String entityModule = "gateway-common";
        String packageName = "node";
        String[] tableName =  new String[]{"cicd_node_info","cicd_node_user_role","cicd_settings_role_function"
                ,"cicd_settings_function","cicd_settings_role","cicd_settings_user"};
        preCustomPath(controllerModule,serviceModule,entityModule,packageName,customPathInfo);

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/cicd?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC",
                        "root",
                        "eadp2022")

                // 全局配置
                .globalConfig(builder -> {
                    builder
                            //.enableSwagger() // 是否启用swagger注解
                            .author("hel") // 作者名称
                            .dateType(DateType.ONLY_DATE) // 时间策略
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .outputDir(System.getProperty("user.dir")) // 输出目录
                            .fileOverride(); // 覆盖已生成文件
                             // .disableOpenDir(); // 生成后禁止打开所生成的系统目录
                })

                // 包配置
                .packageConfig(builder -> {
                    builder
                            .parent("com.ctg.cicd") // 父包名
                            //.moduleName("config") // 模块包名
//                            .entity("common.entity.config") // 实体类包名
                            .entity("config.entity") // 实体类包名
                            //.service("service") // service包名
                            //.serviceImpl("service.impl") // serviceImpl包名
                            .mapper("dao") // mapper包名
                            //.controller("controller") // controller包名
                            .pathInfo(customPathInfo);
                })

                // 策略配置
                .strategyConfig(builder -> {
                    builder
//                            .addTablePrefix("t_") // 增加过滤表前缀
//                            .addTablePrefix("p_cfg") // 增加过滤表前缀
//                            .addTablePrefix("p_config") // 增加过滤表前缀
//                            .addTablePrefix("p_ctg") // 增加过滤表前缀
//                            .addTableSuffix("_db") // 增加过滤表后缀
//                            .addFieldPrefix("t_") // 增加过滤字段前缀
//                            .addFieldSuffix("_field") // 增加过滤字段后缀
                            .addTablePrefix("cicd_") //增加过滤表前缀
                            .addInclude(tableName) // 表匹配

                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok() // 开启lombok
                            //.enableChainModel() // 链式
                            //.enableRemoveIsPrefix() // 开启boolean类型字段移除is前缀
                            //.enableTableFieldAnnotation() //开启生成实体时生成的字段注解
                            .versionColumnName("version") // 乐观锁数据库字段
                            .versionPropertyName("version") // 乐观锁实体类名称
                            //.logicDeleteColumnName("is_deleted") // 逻辑删除数据库中字段名
                            //.logicDeletePropertyName("deleted") // 逻辑删除实体类中的字段名
                            .naming(NamingStrategy.underline_to_camel) // 表名 下划线 -》 驼峰命名
                            .columnNaming(NamingStrategy.underline_to_camel) // 字段名 下划线 -》 驼峰命名
                            .idType(IdType.AUTO) // 主键生成策略 雪花算法生成id

                            .formatFileName("%s") // Entity 文件名称
                            //.addTableFills(new Column("create_time", FieldFill.INSERT)) // 表字段填充
                            //.addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE)) // 表字段填充
                            //.enableColumnConstant()
                            //.enableActiveRecord()

                            // Controller 策略配置
                            .controllerBuilder()
                            .enableRestStyle() // 开启@RestController
                            .formatFileName("%sController") // Controller 文件名称

                            // Service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service 文件名称
                            .formatServiceImplFileName("%sServiceImpl") // ServiceImpl 文件名称

                            // Mapper 策略配置
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启@Mapper
                            .enableBaseColumnList() // 启用 columnList (通用查询结果列)
                            .enableBaseResultMap() // 启动resultMap
                            .formatMapperFileName("%sDao") // Mapper 文件名称
                            .formatXmlFileName("%sMapper"); // Xml 文件名称
                }).execute();
    }

    private static void preCustomPath(String controllerModule, String serviceModule, String entityModule,String packageName, Map<OutputFile, String> customPathInfo) {
        String projectPath = System.getProperty(PROJECT_PATH);
        // src/main/java
        String srcMain = String.join(File.separator, "src", "main", "java");
        String srcRecource = String.join(File.separator, "src", "main", "resources","mapper",packageName);
        // 包名
        String controllerPackageName = "com.ctg.cicd.controller".replace('.', File.separatorChar);
        String entityPackageName = "com.ctg.cicd.common.entity".replace('.', File.separatorChar);
        String servicePackageName = ("com.ctg.cicd."+serviceModule.substring(serviceModule.indexOf("-")+1)+".service").replace('.', File.separatorChar);
        String serviceImplPackageName = ("com.ctg.cicd."+serviceModule.substring(serviceModule.indexOf("-")+1)+".service.impl").replace('.', File.separatorChar);
        String mapperPackageName = ("com.ctg.cicd."+serviceModule.substring(serviceModule.indexOf("-")+1)+".dao").replace('.', File.separatorChar);

        // 自定义控制器路径
        customPathInfo.put(OutputFile.controller, String.join(File.separator, projectPath,controllerModule, srcMain,controllerPackageName, packageName));
        customPathInfo.put(OutputFile.entity, String.join(File.separator, projectPath,entityModule,srcMain, entityPackageName, packageName));
        customPathInfo.put(OutputFile.service, String.join(File.separator, projectPath, serviceModule,srcMain,servicePackageName));
        customPathInfo.put(OutputFile.serviceImpl, String.join(File.separator, projectPath,serviceModule,srcMain, serviceImplPackageName));
        customPathInfo.put(OutputFile.mapperXml, String.join(File.separator, projectPath,serviceModule, srcRecource));
        customPathInfo.put(OutputFile.mapper, String.join(File.separator, projectPath,serviceModule,srcMain, mapperPackageName));


    }


}
