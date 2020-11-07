/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bonelf.common.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义sql字段填充器,本类默认在default-config.properties中配置
 * <p>
 * 若实际项目中，字段名称不一样，可以新建一个此类，在yml配置中覆盖mybatis-plus.global-config.metaObject-handler配置即可
 * <p>
 * 注意默认获取的userId为空
 * @author fengshuonan
 * @date 2018/7/4 下午12:42
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		Object createTime = getFieldValByName("createTime", metaObject);
		if (createTime == null) {
			this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
		}
		Object updateTime = getFieldValByName("updateTime", metaObject);
		if (updateTime == null) {
			this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
		}
		//Object createUser = getFieldValByName("createUser", metaObject);
		//if (createUser == null) {
		//    this.strictInsertFill(metaObject,"createUser", Long.class,312L);
		//}
		//Object updateUser = getFieldValByName("updateUser", metaObject);
		//if (updateUser == null) {
		//    this.strictInsertFill(metaObject,"updateUser", Long.class,312L);
		//}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Object updateTime = getFieldValByName("updateTime", metaObject);
		if (updateTime == null) {
			this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
		}
		//Object updateUser = getFieldValByName("updateUser", metaObject);
		//if (updateUser == null) {
		//    this.strictUpdateFill(metaObject,"updateUser", Long.class, 1313L);
		//}
	}

}
