package com.ailaji.manage.jdbc.persistence;


import java.util.LinkedHashMap;
import java.util.Map;

public class AutoFieldUtils {

    /**
     * 构建操作的字段
     *
     * @param fieldName the field name
     * @param sqlOperator the sql operator
     * @param fieldOperator the field operator
     * @param type the type
     * @param values the values
     * @return auto field
     */
    public static AutoField buildAutoFields(String fieldName, String sqlOperator,
                                            String fieldOperator, int type, Object... values) {
        AutoField autoField = new AutoField();
        autoField.setName(fieldName);
        autoField.setSqlOperator(sqlOperator);
        autoField.setFieldOperator(fieldOperator);
        autoField.setValues(values);
        autoField.setType(type);

        return autoField;
    }

    /**
     * 构建只有主键的fieldMap
     * 
     * @param fieldName
     * @param value
     * @return
     */
	@SuppressWarnings("serial")
	public static Map<String, AutoField> buildPKMap(String fieldName, Object value) {
        final AutoField autoField = buildAutoFields(fieldName, null, null, AutoField.FIELD_WHERE,
            value);
        return new LinkedHashMap<String, AutoField>() {
			{
                put(autoField.getName(), autoField);
            }
        };
    }

}

