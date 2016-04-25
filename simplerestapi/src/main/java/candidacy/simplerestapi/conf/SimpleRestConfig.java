package candidacy.simplerestapi.conf;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "candidacy.simplerestapi" })
public class SimpleRestConfig extends WebMvcConfigurerAdapter{
	
    @Bean
    @Autowired
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(MappingJackson2HttpMessageConverter d){
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        List<HttpMessageConverter<?>> messageConverters = new LinkedList<HttpMessageConverter<?>>();
        messageConverters.add(d);
        requestMappingHandlerAdapter.setMessageConverters(messageConverters);
        return requestMappingHandlerAdapter;
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }


}
