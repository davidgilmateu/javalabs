package candidacy.simplerestapi.conf;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This configuration helps to pretty print 
 * @author david
 *
 */
@Configuration
@EnableWebMvc    
public class JacksonConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());
		return mappingJackson2HttpMessageConverter;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
	    ObjectMapper objMapper = new ObjectMapper();
	    objMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    return objMapper;
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	    super.configureMessageConverters(converters);        
	    converters.add(mappingJackson2HttpMessageConverter());
	}
}
