package com.learnboot.springbootlearn.component;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author VHBin
 * @date 2021/12/07 - 16:03
 */
public class ThisLocalResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        // 获取请求参数
        String l = httpServletRequest.getParameter("l");
        // 获取默认的区域信息配置器
        Locale locale = Locale.getDefault();
        // 根据请求参数重新构建区域信息对象
        if (StringUtils.hasText(l)) {
            String[] s = l.split("_");
            locale = new Locale(s[0],s[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
    }
}
