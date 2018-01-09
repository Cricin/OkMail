package okmail.web;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import okmail.web.security.SecureFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan("okmail.web")
@EnableWebMvc
public class ServletConfig implements WebMvcConfigurer {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.forEach(converter -> {
      if (converter instanceof StringHttpMessageConverter) { //解决返回text时的中文乱码问题
        StringHttpMessageConverter stringConverter = (StringHttpMessageConverter) converter;
        stringConverter.setDefaultCharset(Charset.forName("UTF-8"));
      }
    });

    //用FastJson解析bean
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setParserConfig(new ParserConfig(true)); //解析非public字段
    fastJsonConfig.setSerializeConfig(new SerializeConfig(true)); //序列化非public字段
    fastJsonConfig.setWriteContentLength(true);
    fastJsonConfig.setCharset(Charset.forName("UTF-8"));

    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
    converter.setFastJsonConfig(fastJsonConfig);
    converters.add(converter);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
            .addResourceLocations("classpath:static/")
            .setCacheControl(CacheControl.noStore());
  }

  @Bean
  public SecureFilter secureFilter() {
    return SecureFilter.instance();
  }

}
