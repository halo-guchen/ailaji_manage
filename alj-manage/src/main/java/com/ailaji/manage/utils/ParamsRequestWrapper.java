package com.ailaji.manage.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@SuppressWarnings("all")
public class ParamsRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<String, String[]>();

    public ParamsRequestWrapper(HttpServletRequest request) {
        // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
        super(request);
        // 将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    /**
     * 
     * @param request
     * @param addParams 新增参数
     * @param ModifyParams 修改原有参数(name一致即可)
     */
    public ParamsRequestWrapper(HttpServletRequest request, Map<String, Object> addParams,
            Map<String, Object> ModifyParams) {
        this(request);//执行默认构造方法
        if (ModifyParams != null)
            addParams.putAll(ModifyParams);//将修改参数和新增参数Map合并
        addAllParameters(addParams);
    }

    @Override
    public String getParameter(String name) {// 重写getParameter,代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {// 同上
        return params.get(name);
    }

    @Override
    public Map getParameterMap() {//同上
        return params;
    }

    public void addAllParameters(Map<String, Object> otherParams) {// 迭代循环增加多个参数
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    public void addParameter(String name, Object value) {// 新增参数
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[] { (String) value });
            } else {
                params.put(name, new String[] { String.valueOf(value) });
            }
        }
    }

}
